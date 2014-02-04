package com.nzelot.ledvizfx.ui;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class LWJGL {

    /**
     * Tries to Initialize the LWJGL library using a window of the given size
     * @param width
     * @param heigth
     * @param fullscreen
     */
    public static void init(int width, int heigth, boolean fullscreen) {
	// Display settings

	try {
	    setDisplayMode(width, heigth, fullscreen);

	    Display.create();

	} catch (LWJGLException shit) {
	    shit.printStackTrace();
	}

	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	glOrtho(0, width, heigth, 0, 1, -1);
	glMatrixMode(GL_MODELVIEW);

	//Textures using Alpha Channel
	glEnable(GL_BLEND);
	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }


    private static void setDisplayMode(int width, int height, boolean fullscreen) {

	// return if requested DisplayMode is already set
	if ((Display.getDisplayMode().getWidth() == width) && 
		(Display.getDisplayMode().getHeight() == height) && 
		(Display.isFullscreen() == fullscreen)) {
	    return;
	}

	try {
	    DisplayMode targetDisplayMode = null;

	    if (fullscreen) {
		DisplayMode[] modes = Display.getAvailableDisplayModes();
		int freq = 0;

		for (int i=0;i<modes.length;i++) {
		    DisplayMode current = modes[i];

		    if ((current.getWidth() == width) && (current.getHeight() == height)) {
			if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
			    if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
				targetDisplayMode = current;
				freq = targetDisplayMode.getFrequency();
			    }
			}

			// if we've found a match for bpp and frequence against the 
			// original display mode then it's probably best to go for this one
			// since it's most likely compatible with the monitor
			if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) &&
				(current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
			    targetDisplayMode = current;
			    break;
			}
		    }
		}
	    } else {
		targetDisplayMode = new DisplayMode(width,height);
	    }

	    if (targetDisplayMode == null) {
		System.out.println("Failed to find value mode: "+width+"x"+height+" fs="+fullscreen);
		return;
	    }

	    Display.setDisplayMode(targetDisplayMode);
	    Display.setFullscreen(fullscreen);

	} catch (LWJGLException e) {
	    System.out.println("Unable to setup mode "+width+"x"+height+" fullscreen="+fullscreen + e);
	}
    }

}
