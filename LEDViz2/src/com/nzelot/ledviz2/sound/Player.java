package com.nzelot.ledviz2.sound;

import org.json.JSONObject;

import com.nzelot.ledviz2.sound.meta.METAData;
import com.nzelot.ledviz2.sound.meta.METADataFetcher;
import com.nzelot.ledviz2.sound.meta.METADataFetcherUpdateEvent;


public abstract class Player {

	private METAData meta;
	private PlayerUpdateEvent update;
	
	protected String url;
	

	public abstract boolean init(JSONObject specific);
	public abstract boolean exit();
	public abstract boolean playSong(String url);
	public abstract void stop();
	public abstract boolean hasNewData();
	public abstract boolean isLoaded();
	public abstract boolean isPlaying();
	public abstract PlayerType getPlayerType();
	public abstract float[] getSpectrumData();
	
	protected abstract boolean load();
	protected abstract void startPlayback();
	protected abstract void pausePlayback();
	protected abstract long duration();
	protected abstract long position();
	protected abstract METADataFetcher getFetcher();

	public Player() {
		meta = new METAData("");
	}
	
	public void play(){
		if(isPlaying() || !isLoaded())
			return;
		startPlayback();
	}
	
	public void pause(){
		if(!isPlaying() || !isLoaded())
			return;
		pausePlayback();
	}

	public long getDuration(){
		if(!isLoaded())
			return -1;
		else
			return duration();
	}

	public long getPosition(){
		if(!isLoaded())
			return -1;
		else
			return position();
	}

	public void setUpdate(PlayerUpdateEvent update) {
		this.update = update;
	}
	
	protected void firePlayUpdateEvent(){
		update.update(getMETA());
	}
	
	protected void updateMetaData(){
		getFetcher().init(url, new METADataFetcherUpdateEvent() {
			@Override
			public void update(METAData d) {
				update.update(d);
				setMETAData(d);
			}
		});		
	}
	
	private void setMETAData(METAData d){
		synchronized (meta) {
			meta = new METAData(d);
		}
	}
	
	private METAData getMETA(){
		METAData d = null;
		synchronized (meta) {
			d = new METAData(meta);
		}
		return d;
	}
	
	public METAData getMetaData(){
		if(!isLoaded())
			return null;
		else
			return getMETA();
	}
}
