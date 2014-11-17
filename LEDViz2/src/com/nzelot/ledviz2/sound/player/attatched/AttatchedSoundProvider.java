package com.nzelot.ledviz2.sound.player.attatched;

public interface AttatchedSoundProvider {
	public boolean init(int bands, int updateInterval);
	public boolean exit();
	public boolean load();
	public float[] getSpectrumData();
	
	public boolean hasNewData();
	public boolean isLoaded();
}
