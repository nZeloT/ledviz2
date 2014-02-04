package com.nzelot.ledvizfx.ui.elements;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV_MODE;
import static org.lwjgl.opengl.GL11.glTexEnvi;

import java.awt.Font;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.TextureImpl;

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
	GL11.glEnable(GL11.GL_TEXTURE_2D);
	TextureImpl.unbind();
	glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
	ttf.drawString(x, y, text, new Color(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue()));
	GL11.glDisable(GL11.GL_TEXTURE_2D);
    }
}
