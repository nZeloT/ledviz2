package com.nzelot.ledviz2.sound;

import org.json.JSONObject;

import com.nzelot.ledviz2.LEDViz2;
import com.nzelot.ledviz2.meta.METADataFetcher;


public abstract class Player {

	private boolean running;
	private AttachedSoundProvider player;

	protected abstract long duration();
	protected abstract long position();
	
	public abstract METADataFetcher getFetcher();
	
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
		
		player.load();
		running = false;

		return true;
	}
	
	public boolean exit(){
		stop();
		return player.exit();
	}
	
	public void stop(){
		player.stop();
		running = false;
	}
	
	public void start(){
		if(isRunning() || !isLoaded())
			return;
		
		player.start();
		running = true;
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

	public boolean hasNewData(){
		return player.hasNewData();
	}
	
	public boolean isLoaded(){
		return player.isLoaded();
	}
	
	public boolean isRunning(){
		return running;
	}
	
	public float[] getSpectrumData(){
		return player.getSpectrumData();
	}
}
