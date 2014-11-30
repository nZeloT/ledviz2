package com.nzelot.ledviz2.sound.meta.fetcher;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.apple.itunes.com.ClassFactory;
import com.apple.itunes.com.IITTrack;
import com.apple.itunes.com.IiTunes;
import com.nzelot.ledviz2.sound.meta.METADataFetcher;

public class ITunesTagFetcher extends METADataFetcher {

	@Override
	protected Map<String, String> fetchMetaStrings(String fn) {
		Map<String, String> data = new HashMap<String, String>();

		IiTunes itunes = ClassFactory.createiTunesApp();

		IITTrack track = itunes.currentTrack();
		data.put("album", track.album());
		data.put("artist", track.artist());
		data.put("title", track.name());

		return data;
	}

	@Override
	protected BufferedImage fetchAlbum(Map<String, String> tags) {
		//		if(track.artwork().count() > 0){
		//	    String cover = System.getProperty("user.dir");
		//
		//	    track.artwork(0).saveArtworkToFile(cover);
		//	    
		//	    try {
		//		meta.setAlbumCover(ImageIO.read(new File(cover)));
		//	    } catch (IOException e) {
		//		e.printStackTrace();
		//	    }
		//	}
		return null;
	}

}
