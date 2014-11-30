package com.nzelot.ledviz2.sound.player;

import org.json.JSONObject;

import com.nzelot.ledviz2.LEDViz2;
import com.nzelot.ledviz2.sound.Player;
import com.nzelot.ledviz2.sound.PlayerType;
import com.nzelot.ledviz2.sound.player.attached.AttachedSoundProvider;


public abstract class AttachedPlayer extends Player {

	private boolean playing;
	private AttachedSoundProvider player;
	
	protected abstract void attachedPlaySong();
	protected abstract void attachedStartPlayback();
	protected abstract void attachedPausePlayback();
	protected abstract void attachedStopPlayback();


	@Override
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

	@Override
	public boolean exit(){
		stop();
		return player.exit();
	}

	@Override
	public boolean playSong(String url){
		if(isLoaded() || (!isLoaded() && load())){
			this.url = url;
			attachedPlaySong();
			return true;
		}
		
		return false;
	}
	
	@Override
	protected boolean load(){
		if(isLoaded())
			exit();

		return player.load();
	}

	@Override
	protected void startPlayback() {
		player.start();
		attachedStartPlayback();
		playing = true;
	}

	@Override
	protected void pausePlayback() {
		attachedPausePlayback();
		player.stop();
		playing = false;
	}

	@Override
	public void stop() {
		attachedStopPlayback();
		player.stop();
		playing = false;
	}

	@Override
	public float[] getSpectrumData(){
		return player.getSpectrumData();
	}

	@Override
	public boolean hasNewData(){
		return player.hasNewData();
	}

	@Override
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
