package com.nzelot.ledviz2.gfx.core;

import static org.lwjgl.opengl.GL11.glColor4f;

import java.awt.Color;

import com.nzelot.ledviz2.gfx.res.ResourceManager;

public class Sprite {

    private Texture tex;

    private int cols;
    private int rows;

    private int frameSize;

    public Sprite(String tex, int cols, int rows, int frameSize) {
	super();
	this.tex = ResourceManager.getResource(tex).<Texture>getData();
	this.cols = cols;
	this.rows = rows;
	this.frameSize = frameSize;
    }

    public void renderFrame(int x, int y, int size, int nr, Color... c){
	if(c != null && c.length > 0){
	    glColor4f(c[0].getRed()/255.0f, c[0].getGreen()/255.0f, c[0].getBlue()/255.0f, c[0].getAlpha()/255.0f);
	}

	tex.bind();

	int t1 = 0;
	int s1 = 0;
	int t2 = frameSize;
	int s2 = frameSize;

	if(nr < cols){
	    s1 = frameSize * nr;
	    s2 = frameSize * (nr+1);
	}else{
	    s1 = frameSize * (nr % cols);
	    t1 = frameSize * ((int)(nr / (cols+0f)));
	    s2 = frameSize * (nr % 10 +1);
	    t2 = frameSize * ((int)(nr / (cols+0f)) + 1);
	}
	
	DrawUtils.drawQuad(x, y, size, size, new int[][]{{s1, s2},{t1, t2}});
    }

    public int getCols() {
	return cols;
    }

    public int getRows() {
	return rows;
    }

    public int getFrameSize() {
	return frameSize;
    }

    public void setCols(int cols) {
	this.cols = cols;
    }

    public void setRows(int rows) {
	this.rows = rows;
    }

    public void setFrameSize(int frameSize) {
	this.frameSize = frameSize;
    }
}
