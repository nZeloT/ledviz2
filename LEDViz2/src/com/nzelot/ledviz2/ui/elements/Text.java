package com.nzelot.ledviz2.ui.elements;

import static org.lwjgl.opengl.EXTTextureRectangle.GL_TEXTURE_RECTANGLE_EXT;
import static org.lwjgl.opengl.GL11.GL_DECAL;
import static org.lwjgl.opengl.GL11.GL_MODULATE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV_MODE;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glTexEnvi;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.TextureImpl;

import com.nzelot.ledviz2.gfx.core.Texture;
import com.nzelot.ledviz2.gfx.res.ResourceManager;

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
        
        font = ResourceManager.getResource("res/fonts/simple_light").<Font>getData().deriveFont(heigth+0f);
        ttf = new TrueTypeFont(font, true);
    }

    @Override
    public void draw() {
	Texture.unbind();
	glDisable(GL_TEXTURE_RECTANGLE_EXT);
	glEnable(GL_TEXTURE_2D);
	TextureImpl.unbind();
	glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
	ttf.drawString(x, y, text, new Color(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue()));
	glDisable(GL_TEXTURE_2D);
	glEnable(GL_TEXTURE_RECTANGLE_EXT);
	glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_DECAL);
    }
}
