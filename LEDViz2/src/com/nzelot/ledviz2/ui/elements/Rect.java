package com.nzelot.ledviz2.ui.elements;

import java.awt.Color;

public class Rect extends UIElement {
    
    private Color[] col;
    
    public Rect(int x, int y, int width, int heigth) {
	this(x, y, width, heigth, new Color[]{Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK});
    }

    public Rect(int x, int y, int width, int heigth, Color[] col) {
	super(x, y, width, heigth);
	
	if(col.length != 4)
	    this.col = new Color[]{Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK};
	else
	    this.col = col;
    }
    
    public Color[] getColors() {
	return col;
    }
    
    public void setColors(Color[] col) {
	this.col = col;
    }
    
    public Color getColor(int idx) {
	if(idx < 0 || idx >= col.length)
	    return null;
	return col[idx];
    }
    
    public void setColor(int idx, Color col) {
	if(idx >= 0 && idx < this.col.length)
	    this.col[idx] = col;
    }

    @Override
    public void draw() {
	RenderUtils.drawColoredRactangle(x, y, width, heigth, col);
    }
}
