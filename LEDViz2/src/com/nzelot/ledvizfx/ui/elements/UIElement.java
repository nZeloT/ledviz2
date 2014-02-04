package com.nzelot.ledvizfx.ui.elements;

import java.awt.Color;

import com.nzelot.ledviz2.gfx.core.Drawable;

abstract class UIElement implements Drawable {
    
    protected int x;
    protected int y;
    
    protected int width;
    protected int heigth;
    
    protected Color bgColor;
    protected Color fgColor;
    
    public UIElement(int x, int y, int width, int heigth) {
	super();
	setX(x);
	setY(y);
	setWidth(width);
	setHeigth(heigth);
	
	setBgColor(Color.BLACK);
	setFgColor(Color.WHITE);
    }
    
    public int getX() {
	return x;
    }
    
    public int getY() {
	return y;
    }
    
    public int getWidth() {
	return width;
    }
    
    public int getHeigth() {
	return heigth;
    }
    
    public Color getFgColor() {
	return fgColor;
    }
    
    public Color getBgColor() {
	return bgColor;
    }
    
    public void setX(int x) {
	this.x = x;
    }
    
    public void setY(int y) {
	this.y = y;
    }
    
    public void setWidth(int width) {
	this.width = width;
    }
    
    public void setHeigth(int heigth) {
	this.heigth = heigth;
    }
    
    public void setBgColor(Color bgColor) {
	this.bgColor = bgColor;
    }
    
    public void setFgColor(Color fgColor) {
	this.fgColor = fgColor;
    }
}
