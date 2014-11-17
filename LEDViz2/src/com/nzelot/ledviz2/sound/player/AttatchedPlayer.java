package com.nzelot.ledviz2.sound.player;

import com.nzelot.ledviz2.sound.Player;
import com.nzelot.ledviz2.sound.PlayerType;
import com.nzelot.ledviz2.sound.player.attatched.AttatchedSoundProvider;


public abstract class AttatchedPlayer extends Player {
	
	protected boolean playing;

	protected AttatchedSoundProvider player;
	
	private Class<?> providerClass;

	public AttatchedPlayer(Class<?> provider) {
		providerClass = provider;
	}

	public boolean init(int bands, int updateInterval){
		try {
			player = (AttatchedSoundProvider) providerClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		if(!player.init(bands, updateInterval))
			return false;
		
		playing = false;
		
		load();
		
		return true;
	}

	public boolean exit(){
		stop();
		
		player.exit();
		
		return true;
	}
	
	public boolean load(String url){
		return load();
	}

	public boolean load(){
		if(isLoaded())
			exit();
		
		return player.load();
	}

	public float[] getSpectrumData(){
		return player.getSpectrumData();
	}

	public boolean hasNewData(){
		return player.hasNewData();
	}

	public boolean isLoaded() {
		return player.isLoaded();
	}
	
	@Override
	public PlayerType getPlayerType() {
		return PlayerType.ATTACHED;
	}
}
