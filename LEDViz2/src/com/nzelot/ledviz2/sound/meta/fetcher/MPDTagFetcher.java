package com.nzelot.ledviz2.sound.meta.fetcher;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.bff.javampd.MPD;
import org.bff.javampd.exception.MPDPlayerException;
import org.bff.javampd.objects.MPDSong;
import org.musicbrainz.MBWS2Exception;
import org.musicbrainz.controller.Release;
import org.musicbrainz.model.searchresult.ReleaseResultWs2;

import com.nzelot.ledviz2.sound.meta.METAData;
import com.nzelot.ledviz2.sound.meta.METADataFetcher;

import fm.last.musicbrainz.coverart.CoverArt;
import fm.last.musicbrainz.coverart.CoverArtArchiveClient;
import fm.last.musicbrainz.coverart.impl.DefaultCoverArtArchiveClient;

public class MPDTagFetcher implements METADataFetcher {

	@Override
	public METAData fetch(String fn) {
		MPD mpd = com.nzelot.mpd.MPD.getMPD();
		METAData data = null;
		if(mpd != null){
			MPDSong song = null;

			try {
				song = mpd.getPlayer().getCurrentSong();
			} catch (MPDPlayerException e) {
				e.printStackTrace();
			}
			
			data = new METAData(song.getFile());

			data.setAlbum(song.getAlbumName());
			data.setArtist(song.getArtistName());
			data.setTitle(song.getTitle());

			Release r = new Release();
			r.getSearchFilter().setLimit((long) 30);
			r.search(data.getAlbum() + " AND creditname:" + data.getArtist());
			List<ReleaseResultWs2> results = null;

			try {
				results = r.getFullSearchResultList();
			} catch (MBWS2Exception e1) {
				e1.printStackTrace();
			}

			if(results.size() > 0){
				String id = results.get(0).getRelease().getId();

				CoverArtArchiveClient client = new DefaultCoverArtArchiveClient();
				UUID mbid = UUID.fromString(id);

				BufferedImage img = null;

				try {
					CoverArt coverArt = client.getByMbid(mbid);
					if (coverArt != null && coverArt.getFrontImage() != null) {
						img = ImageIO.read(coverArt.getFrontImage().getImage());
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				data.setAlbumCover(img);
			}

		}
		return data;
	}

}
