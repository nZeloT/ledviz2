package com.nzelot.ledviz2.sound.player;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Timer;

import com.nzelot.ledviz2.sound.Player;
import com.nzelot.ledviz2.sound.PlayerType;


public abstract class StandalonePlayer extends Player {

	protected Timer timer;
	protected int updInterval;

	protected boolean loaded;

	protected boolean playing;

	protected ByteBuffer buffer;
	protected int bufferSize;

	protected boolean hasNewData;

	protected String path;
	
	public boolean load(String url){
		path = new File(url).getAbsolutePath();

		if(load()){
			meta = getFetcher().fetch(path);
			return true;
		}else
			return false;
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
	
	@Override
	public PlayerType getPlayerType() {
		return PlayerType.STANDALONE;
	}
}
