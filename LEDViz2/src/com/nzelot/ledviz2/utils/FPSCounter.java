package com.nzelot.ledviz2.utils;

import org.lwjgl.Sys;

public class FPSCounter {
    
    /** frames per second */
    private int fps;
    /** last fps time */
    private long lastFPS;
    
    private String s;
    
    public FPSCounter() {
	lastFPS = getTime(); // call before loop to initialise fps timer
    }
    
    /**
     * Get the accurate system time
     * 
     * @return The system time in milliseconds
     */
    public long getTime() {
	return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    /**
     * Calculate the FPS and set it in the title bar
     */
    public String updateFPS() {
	if (getTime() - lastFPS > 1000) {
	    s = "FPS: " + fps;
	    fps = 0;
	    lastFPS += 1000;
	}
	fps++;
	
	return s;
    }
}
