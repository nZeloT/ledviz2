package com.nzelot.ledviz2.sound.meta;

import java.awt.image.BufferedImage;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class METADataFetcher implements Runnable {
	
	private final Logger l = LoggerFactory.getLogger(METADataFetcher.class);
	
	private METADataFetcherUpdateEvent evt;
	private METAData d;
	private String fn;
	
    protected abstract Map<String, String>  fetchMetaStrings(String fn);
    protected abstract BufferedImage fetchAlbum(Map<String, String> tags);
    
    public void init(String fn){
    	init(fn, null);
    }
    
    public void init(String fn, METADataFetcherUpdateEvent evt){
    	this.fn  = fn;
    	this.evt = evt;
    	
    	new Thread(this).start();
    }
    
    @Override
    public void run() {
    	Map<String, String> data = fetchMetaStrings(fn);
    	d = new METAData( data.get("file") );
    	d.setArtist( data.get("artist") );
    	d.setAlbum( data.get("album") );
    	d.setTitle( data.get("title") );
    	
    	l.info(d.toString());
    	
    	if(evt != null)
    		evt.update(d);
    	
    	d.setAlbumCover( fetchAlbum(data) );
    	
    	l.info(d.toString());
    	
    	if(evt != null)
    		evt.update(d);
    	
    }
    
}
