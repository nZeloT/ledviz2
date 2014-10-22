package com.nzelot.ledviz2.ui.elements;

import com.nzelot.ledviz2.ui.utils.RenderUtils;

public class ImageButton extends Image {

    public ImageButton(int x, int y, int width, int heigth, String tex) {
	super(x, y, width, heigth, tex);
    }
    
    @Override
    public void draw() {
	RenderUtils.drawColoredRectangle(x, y, width, heigth, bgColor);
	RenderUtils.drawTextureRectangle(x+2, y+2, width-4, heigth-4, tex);
    }
}
