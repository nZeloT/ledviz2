package com.nzelot.ledvizfx.ui.elements;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import com.nzelot.ledvizfx.gfx.res.ResourceManager;

@SuppressWarnings("deprecation")
public class Text extends UIElement {
    
    private String text;
    
    private Font font;
    private TrueTypeFont ttf;
    
    

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
    
    public void setTextsize(int s){
	setHeigth(s);
    }
    
    @Override
    public void setHeigth(int heigth) {
        super.setHeigth(heigth);
        
        font = ResourceManager.getResource("fonts/simple_light").<Font>getData().deriveFont(heigth+0f);
        ttf = new TrueTypeFont(font, true);
    }

    @Override
    public void draw() {
//	DrawUtils.drawText(x, y, text, font, fgColor);
	ttf.drawString(x, y, text, new Color(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue()));
    }
}
