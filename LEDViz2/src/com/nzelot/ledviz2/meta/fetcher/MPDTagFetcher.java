package com.nzelot.ledviz2.meta.fetcher;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.bff.javampd.player.MPDPlayerException;
import org.bff.javampd.server.MPD;
import org.bff.javampd.song.MPDSong;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.musicbrainz.MBWS2Exception;
import org.musicbrainz.controller.Release;
import org.musicbrainz.model.searchresult.ReleaseResultWs2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;
import com.nzelot.ledviz2.meta.METADataFetcher;

import fm.last.musicbrainz.coverart.CoverArt;
import fm.last.musicbrainz.coverart.CoverArtArchiveClient;
import fm.last.musicbrainz.coverart.impl.DefaultCoverArtArchiveClient;

public class MPDTagFetcher extends METADataFetcher {

	private final Logger l = LoggerFactory.getLogger(MPDTagFetcher.class);

	@Override
	protected Map<String, String> fetchMetaStrings() {
		Map<String, String> data = new HashMap<String, String>();

		MPD mpd = com.nzelot.mpd.MPD.getMPD();
		if(mpd != null){
			MPDSong song = null;

			try {
				song = mpd.getPlayer().getCurrentSong();
			} catch (MPDPlayerException e) {
				e.printStackTrace();
			}

			data.put("file", com.nzelot.mpd.MPD.getLibLoc() + song.getFile());
			data.put("artist", song.getArtistName());
			data.put("album", song.getAlbumName());
			data.put("title", song.getTitle());

		}

		return data;
	}

	@Override
	protected BufferedImage fetchAlbum(Map<String, String> tags) {
		BufferedImage img = null;

		//1. First run is JAudioTagger Library
		AudioFile f = null;
		try {
			f = AudioFileIO.read(new File(tags.get("file")));
		} catch (Exception e) {
			l.info("Couldn't read file!");
		}
		Tag t = null;

		if(f != null){
			t = f.getTag();

			try {
				if(t.getFirstArtwork() != null && t.getFirstArtwork().getBinaryData() != null)
					img = ImageIO.read(new ByteArrayInputStream(t.getFirstArtwork().getBinaryData()));
			} catch (IOException e1) {
				l.info("Failed to read Cover from file");
			}
		}

		//success?
		if(img != null)
			return img;

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
						l.info("Failed to read Cover from Directory");
						e.printStackTrace();
					}
				}
			}
		}

		//success?
		if(img != null)
			return img;

		UUID mbid = null;

		if(t != null && !t.getFirst(FieldKey.MUSICBRAINZ_DISC_ID).isEmpty())
			mbid = UUID.fromString(t.getFirst(FieldKey.MUSICBRAINZ_DISC_ID));

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

		//3.
		//If we somehow got an mbid
		//try to find a cover in cover art archive
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
