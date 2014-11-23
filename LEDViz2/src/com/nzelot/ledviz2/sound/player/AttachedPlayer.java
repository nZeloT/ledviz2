package com.nzelot.ledviz2.sound.player;

import org.json.JSONObject;

import com.nzelot.ledviz2.LEDViz2;
import com.nzelot.ledviz2.sound.Player;
import com.nzelot.ledviz2.sound.PlayerType;
import com.nzelot.ledviz2.sound.player.attached.AttachedSoundProvider;


public abstract class AttachedPlayer extends Player {
	
	private boolean playing;

	private AttachedSoundProvider player;

	public boolean init(JSONObject specific){
		
		try {
			int updateInterval	=	specific.optInt("updInterval", 25);
			String className	=	specific.getJSONObject("provider").getString("class");
			JSONObject provider	=	specific.getJSONObject("provider").getJSONObject("specific");
			Class<?> clazz		= 	LEDViz2.class.getClassLoader().loadClass(className);
			player = (AttachedSoundProvider) clazz.newInstance();
			
			if(!player.init(updateInterval, provider))
				return false;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		playing = false;
		
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
		
		if(player.load()){
			meta = getFetcher().fetch("");
			return true;
		}
		
		return false;
	}
	
	@Override
	protected void startPlayback() {
		player.start();
		attachedStartPlayback();
		playing = true;
	}
	
	protected abstract void attachedStartPlayback();
	
	@Override
	protected void pausePlayback() {
		attachedPausePlayback();
		player.stop();
		playing = false;
	}
	
	protected abstract void attachedPausePlayback();

	
	@Override
	public void stop() {
		attachedStopPlayback();
		player.stop();
		playing = false;
	}
	
	protected abstract void attachedStopPlayback();


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
	public boolean isPlaying() {
		return playing;
	}
	
	@Override
	public PlayerType getPlayerType() {
		return PlayerType.ATTACHED;
	}
}
