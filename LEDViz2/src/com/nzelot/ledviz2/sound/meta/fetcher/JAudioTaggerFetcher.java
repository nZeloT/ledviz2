package com.nzelot.ledviz2.sound.meta.fetcher;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import com.nzelot.ledviz2.sound.meta.METAData;
import com.nzelot.ledviz2.sound.meta.METADataFetcher;

import fm.last.musicbrainz.coverart.CoverArt;
import fm.last.musicbrainz.coverart.CoverArtArchiveClient;
import fm.last.musicbrainz.coverart.impl.DefaultCoverArtArchiveClient;

public class JAudioTaggerFetcher implements METADataFetcher {

	@Override
	public METAData fetch(String fn) {
		METAData meta = new METAData(fn);
		AudioFile f = null;

		try {
			f = AudioFileIO.read(new File(fn));
		} catch (Exception e) {
			e.printStackTrace();
		}

		Tag t = f.getTag();

		meta.setArtist(t.getFirst(FieldKey.ARTIST));
		meta.setAlbum(t.getFirst(FieldKey.ALBUM));
		meta.setTitle(t.getFirst(FieldKey.TITLE));

		try {
			if(t.getFirstArtwork() != null && t.getFirstArtwork().getBinaryData() != null)
				meta.setAlbumCover(ImageIO.read(new ByteArrayInputStream(t.getFirstArtwork().getBinaryData())));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		if(meta.getAlbumCover() == null && !t.getFirst(FieldKey.MUSICBRAINZ_DISC_ID).isEmpty()){
			CoverArtArchiveClient client = new DefaultCoverArtArchiveClient();
			UUID mbid = UUID.fromString(t.getFirst(FieldKey.MUSICBRAINZ_DISC_ID));

			BufferedImage img = null;

			try {
				CoverArt coverArt = client.getByMbid(mbid);
				if (coverArt != null && coverArt.getFrontImage() != null) {
					img = ImageIO.read(coverArt.getFrontImage().getImage());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			meta.setAlbumCover(img);
		}

		return meta;
	}

}
