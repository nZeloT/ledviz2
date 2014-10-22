package com.nzelot.ledviz2.ui.elements;

import java.awt.Font;

import com.nzelot.ledviz2.gfx.core.GLFont;
import com.nzelot.ledviz2.gfx.res.ResourceManager;

public class Text extends UIElement {

    private String text;

    private GLFont glFont;
    private Font font;

    public Text(int x, int y, int textSize) {
	this(x, y, textSize, "");
    }

    public Text(int x, int y, int textSize, String text) {
	super(x, y, 0, textSize);

	setFontSize(textSize);
	setText(text);
    }

    public String getText() {
	return text;
    }

    public void setText(String text) {
	this.text = text;
    }

    /**
     * Unused; Does not affect anything
     */
    @Override
    public void setHeigth(int heigth) {}

    public int getFontSize(){
	return glFont.getSize();
    }

    public void setFontSize(int size){
	font = ResourceManager.getResource("res/fonts/simple_light").<Font>getData().deriveFont(size+0f);
	glFont = GLFont.getFont(font, fgColor);
	heigth = glFont.getHeigth();
    }

    @Override
    public void draw() {
	glFont.drawString(x, y, text);
    }
}
