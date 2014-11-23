package com.nzelot.ledviz2.sound.player.attached;

import org.json.JSONObject;

public interface AttachedSoundProvider {
	public boolean init(int updateInterval, JSONObject specific);
	public boolean exit();
	public boolean load();
	public boolean start();
	public boolean stop();
	public float[] getSpectrumData();
	
	public boolean hasNewData();
	public boolean isLoaded();
}
