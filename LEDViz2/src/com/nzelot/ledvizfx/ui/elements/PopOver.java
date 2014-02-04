package com.nzelot.ledvizfx.ui.elements;


public class PopOver extends UIElement {
    
    private int duration;
    
    private int currentFrame;
    
    private String tex;
    
    public PopOver(int x, int y, int width, int heigth, int duration, String tex) {
	super(x, y, width, heigth);
	
	this.duration = duration;
	this.currentFrame = -1;
	this.tex = tex;
    }
    
    public void setTex(String tex) {
	this.tex = tex;
    }
    
    public String getTex() {
	return tex;
    }
    
    public int getDuration() {
	return duration;
    }
    
    public void setDuration(int duration) {
	this.duration = duration;
    }
    
    public int getCurrentFrame() {
	return currentFrame;
    }
    
    public void reset(){
	currentFrame = -1;
    }
    
    public void popOut(){
	if(currentFrame == -1)
	    currentFrame = 0;
    }

    @Override
    public void draw() {
	if(currentFrame == -1)
	    return;
	
	if(currentFrame <= duration/2){
	    //increase alpha
	    RenderUtils.drawAlphaTextureRectangle(x, y, width, heigth, currentFrame/(duration/2f), tex);
	}else{
	    //Decrease alpha
	    RenderUtils.drawAlphaTextureRectangle(x, y, width, heigth, 1-(currentFrame-duration/2f)/(duration/2f), tex);
	}
	
	currentFrame++;
	if(currentFrame == duration)
	    currentFrame = -1;
    }
}
