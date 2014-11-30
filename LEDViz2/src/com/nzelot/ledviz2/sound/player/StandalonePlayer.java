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

	@Override
	public boolean playSong(String url){
		this.url = new File(url).getAbsolutePath();

		if(load()){
			updateMetaData();
			return true;
		}else
			return false;
	}

	@Override
	public boolean hasNewData(){
		return hasNewData;
	}

	@Override
	public boolean isLoaded() {
		return loaded;
	}

	@Override
	public boolean isPlaying() {
		return playing;
	}
	
	@Override
	public PlayerType getPlayerType() {
		return PlayerType.STANDALONE;
	}
}
