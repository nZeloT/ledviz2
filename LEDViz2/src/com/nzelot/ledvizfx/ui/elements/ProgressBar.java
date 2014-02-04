package com.nzelot.ledvizfx.ui.elements;

import java.text.DecimalFormat;

public class ProgressBar extends UIElement{

    private double progress;

    private boolean showText;
    
    private Text txt;

    public ProgressBar(int x, int y, int width, int heigth) {
	super(x, y, width, heigth);

	this.progress = 0;
	this.showText = false;
	
	txt = new Text((int) (x+width/2f), (int) (y), heigth);
    }

    public double getProgress() {
	return progress;
    }

    public void setProgress(double progress) {
	if(progress < 0)
	    progress = 0;
	if(progress > 1)
	    progress = 1;
	this.progress = progress;
    }
    
    public void setShowText(boolean showText) {
	this.showText = showText;
    }
    
    public boolean getShowText() {
	return showText;
    }
    
    @Override
    public void draw() {
	//Progress
	RenderUtils.drawColoredRectangle(x, y, (int) (width*progress), heigth, bgColor);

	//Text
	if(showText){
	    String text = DecimalFormat.getPercentInstance().format(progress);
	    txt.setText(text);
	    txt.draw();
	}
    }
}
