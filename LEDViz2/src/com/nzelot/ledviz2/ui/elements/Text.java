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
	
	setHeigth(textSize);
	this.text = text;
    }
    
    public String getText() {
	return text;
    }
    
    public void setText(String text) {
	this.text = text;
    }
    
    public int getTextSize(){
	return getHeigth();
    }
    
    public void setTextSize(int s){
	setHeigth(s);
    }
    
    public int getFontRenderHeigth(){
	return glFont.getHeigth();
    }
    
    @Override
    public void setHeigth(int heigth) {
        super.setHeigth(heigth);
        
        font = ResourceManager.getResource("res/fonts/simple_light").<Font>getData().deriveFont(heigth+0f);
        glFont = GLFont.getFont(font, fgColor);
    }

    @Override
    public void draw() {
	glFont.drawString(x, y, text);
    }
}
