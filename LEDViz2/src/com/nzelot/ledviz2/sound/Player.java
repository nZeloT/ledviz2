package com.nzelot.ledviz2.sound;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Timer;

import com.nzelot.ledviz2.sound.meta.METAData;
import com.nzelot.ledviz2.sound.meta.METADataFetcher;


public abstract class Player {

	protected static PlayerType type;

	protected int updInterval;

	protected Timer timer;

	protected boolean loaded;

	protected boolean playing;

	protected ByteBuffer buffer;
	protected int bufferSize;

	protected boolean hasNewData;

	private METAData meta;

	protected String path;

	public Player() {
		type = PlayerType.STANDALONE;
	}

	public abstract boolean init(int bands, int updateInterval);

	public abstract boolean exit();

	public boolean load(String url){
		path = new File(url).getAbsolutePath();

		if(load()){
			meta = getFetcher().fetch(path);
			return true;
		}else
			return false;
	}

	protected abstract boolean load();

	public abstract void play();

	public abstract void pause();

	public abstract void stop();

	public long getDuration(){
		if(!loaded)
			return -1;
		else
			return duration();
	}

	protected abstract long duration();

	public long getPosition(){
		if(!loaded)
			return -1;
		else
			return position();
	}

	protected abstract long position();

	public abstract float[] getSpectrumData();

	protected abstract METADataFetcher getFetcher();

	public METAData getMetaData(){
		if(!loaded)
			return null;
		else
			return meta;
	}

	public boolean hasNewData(){
		return hasNewData;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public boolean isPlaying() {
		return playing;
	}

	public static PlayerType playerType(){
		return type;
	}
}
