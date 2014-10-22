package com.nzelot.ledviz2.ui.elements;

import com.nzelot.ledviz2.ui.utils.RenderUtils;

public class Image extends UIElement {
    
    protected String tex;

    public Image(int x, int y, int width, int heigth, String tex) {
	super(x, y, width, heigth);
	this.tex = tex;
    }
    
    public void setTex(String tex) {
	this.tex = tex;
    }
    
    public String getTex() {
	return tex;
    }

    @Override
    public void draw() {
	RenderUtils.drawTextureRectangle(x, y, width, heigth, tex);
    }
}
