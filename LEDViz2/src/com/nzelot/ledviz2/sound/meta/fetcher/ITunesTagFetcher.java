package com.nzelot.ledviz2.sound.meta.fetcher;

import com.apple.itunes.com.ClassFactory;
import com.apple.itunes.com.IITTrack;
import com.apple.itunes.com.IiTunes;
import com.nzelot.ledviz2.sound.meta.METAData;
import com.nzelot.ledviz2.sound.meta.METADataFetcher;

public class ITunesTagFetcher implements METADataFetcher {

    @Override
    public METAData fetch(String fn) {
	METAData meta = new METAData(fn);
	IiTunes itunes = ClassFactory.createiTunesApp();
	
	IITTrack track = itunes.currentTrack();
	meta.setAlbum(track.album());
	meta.setArtist(track.artist());
	meta.setTitle(track.name());
	
//	if(track.artwork().count() > 0){
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
	
	return meta;
    }

}
