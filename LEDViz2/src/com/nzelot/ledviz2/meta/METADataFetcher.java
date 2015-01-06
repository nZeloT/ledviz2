package com.nzelot.ledviz2.meta;

import java.awt.image.BufferedImage;
import java.util.Map;

public abstract class METADataFetcher implements Runnable {
	
	private METADataFetcherUpdateEvent evt;
	private METAData d;
	
    protected abstract Map<String, String>  fetchMetaStrings();
    protected abstract BufferedImage fetchAlbum(Map<String, String> tags);
    
    public void init(){
    	init(null);
    }
    
    public void init(METADataFetcherUpdateEvent evt){
    	this.evt = evt;
    	
    	new Thread(this).start();
    }
    
    @Override
    public void run() {
    	Map<String, String> data = fetchMetaStrings();
    	d = new METAData( data.get("file") );
    	d.setArtist( data.get("artist") );
    	d.setAlbum( data.get("album") );
    	d.setTitle( data.get("title") );
    	
    	if(evt != null)
    		evt.update(d);
    	
    	d.setAlbumCover( fetchAlbum(data) );
    	
    	if(evt != null)
    		evt.update(d);
    	
    }
    
}
