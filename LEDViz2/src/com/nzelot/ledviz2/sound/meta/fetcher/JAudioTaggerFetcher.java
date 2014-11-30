package com.nzelot.ledviz2.sound.meta.fetcher;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.musicbrainz.MBWS2Exception;
import org.musicbrainz.controller.Release;
import org.musicbrainz.model.searchresult.ReleaseResultWs2;

import com.google.common.io.Files;
import com.nzelot.ledviz2.sound.meta.METADataFetcher;

import fm.last.musicbrainz.coverart.CoverArt;
import fm.last.musicbrainz.coverart.CoverArtArchiveClient;
import fm.last.musicbrainz.coverart.impl.DefaultCoverArtArchiveClient;

public class JAudioTaggerFetcher extends METADataFetcher {

	private BufferedImage album = null;
	private UUID mbid = null;

	@Override
	protected Map<String, String> fetchMetaStrings(String fn) {
		Map<String, String> data = new HashMap<String, String>();

		AudioFile f = null;

		try {
			f = AudioFileIO.read(new File(fn));
		} catch (Exception e) {
			e.printStackTrace();
		}

		Tag t = f.getTag();

		data.put("file", fn);
		data.put("artist", t.getFirst(FieldKey.ARTIST));
		data.put("album", t.getFirst(FieldKey.ALBUM));
		data.put("title", t.getFirst(FieldKey.TITLE));

		try {
			if(t.getFirstArtwork() != null && t.getFirstArtwork().getBinaryData() != null)
				album = ImageIO.read(new ByteArrayInputStream(t.getFirstArtwork().getBinaryData()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		if(!t.getFirst(FieldKey.MUSICBRAINZ_DISC_ID).isEmpty())
			mbid = UUID.fromString(t.getFirst(FieldKey.MUSICBRAINZ_DISC_ID));


		return data;
	}

	@Override
	protected BufferedImage fetchAlbum(Map<String, String> tags) {

		BufferedImage img = null;

		//1. JAudioTagger
		//success?
		if(album != null)
			return album;

		//2. Is there a picture in the album dir?
		File file = new File(tags.get("file"));
		File dir = file.getParentFile();

		if(dir.isDirectory()){
			String[] files = dir.list();
			for(String s : files){
				String ext = Files.getFileExtension(s);
				if(ext.equals("png") || ext.equals("jpg") || ext.equals("jpeg")){
					try{
						img = ImageIO.read(new File(dir.getAbsolutePath() +"/"+ s));
					}catch(Exception e){
					}
				}
			}
		}

		//success?
		if(img != null)
			return img;

		//Could not read MBID from file
		//so try to fetch it online
		if(mbid == null){
			Release r = new Release();
			r.getSearchFilter().setLimit((long) 5);
			r.search(tags.get("album") + " AND creditname:" + tags.get("artist"));
			List<ReleaseResultWs2> results = null;

			try {
				results = r.getFullSearchResultList();
			} catch (MBWS2Exception e1) {
				e1.printStackTrace();
			}

			if(results.size() > 0){
				String id = results.get(0).getRelease().getId();
				mbid = UUID.fromString(id);
			}
		}

		//If we somehow got an mbid
		//try to find a cover
		if(mbid != null){
			CoverArtArchiveClient client = new DefaultCoverArtArchiveClient();

			try {
				CoverArt coverArt = client.getByMbid(mbid);
				if (coverArt != null && coverArt.getFrontImage() != null) {
					img = ImageIO.read(coverArt.getFrontImage().getImage());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return img;
	}

}
