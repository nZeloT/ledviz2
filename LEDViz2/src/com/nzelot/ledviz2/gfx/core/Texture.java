package com.nzelot.ledviz2.gfx.core;

import static org.lwjgl.opengl.EXTTextureRectangle.GL_TEXTURE_RECTANGLE_EXT;
import static org.lwjgl.opengl.GL11.glBindTexture;

public class Texture {
    
    private static int lastBound = 0;
    
    private int width;
    private int heigth;
    
    private int texId;
    
    public Texture(int w, int h, int id) {
	this.width = w;
	this.heigth = h;
	this.texId = id;
    }
    
    public void bind(){
	if(lastBound == texId)
	    return;
	glBindTexture(GL_TEXTURE_RECTANGLE_EXT, texId);
	lastBound = texId;
    }
    
    public static void unbind(){
	glBindTexture(GL_TEXTURE_RECTANGLE_EXT, 0);
	lastBound = 0;
    }
    
    public int getWidth() {
	return width;
    }
    
    public int getHeigth() {
	return heigth;
    }
    
    public int getTexId() {
	return texId;
    }
}
