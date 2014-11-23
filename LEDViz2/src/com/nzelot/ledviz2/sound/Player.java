package com.nzelot.ledviz2.sound;

import org.json.JSONObject;

import com.nzelot.ledviz2.sound.meta.METAData;
import com.nzelot.ledviz2.sound.meta.METADataFetcher;


public abstract class Player {

	protected METAData meta;

	public abstract boolean init(JSONObject specific);

	public abstract boolean exit();

	public abstract boolean load(String url);
	protected abstract boolean load();
	
	public void play(){
		if(isPlaying() || !isLoaded())
			return;
		startPlayback();
	}
	
	protected abstract void startPlayback();
	
	public void pause(){
		if(!isPlaying() || !isLoaded())
			return;
		pausePlayback();
	}

	protected abstract void pausePlayback();

	public abstract void stop();

	public long getDuration(){
		if(!isLoaded())
			return -1;
		else
			return duration();
	}

	protected abstract long duration();

	public long getPosition(){
		if(!isLoaded())
			return -1;
		else
			return position();
	}

	protected abstract long position();

	public abstract float[] getSpectrumData();

	protected abstract METADataFetcher getFetcher();

	public METAData getMetaData(){
		if(!isLoaded())
			return null;
		else
			return meta;
	}

	public abstract boolean hasNewData();

	public abstract boolean isLoaded();

	public abstract boolean isPlaying();

	public abstract PlayerType getPlayerType();
}
