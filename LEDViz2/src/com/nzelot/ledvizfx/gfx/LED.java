package com.nzelot.ledvizfx.gfx;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glDisable;

import java.awt.Color;

import com.nzelot.ledvizfx.ui.fx.DrawUtils;
import com.nzelot.ledvizfx.ui.fx.Drawable;

public class LED implements Drawable{

    public static final int ANIM_COUNT = 99;
    private static final String BASE_LOCATION = "textures/led/";

    /**
     * Fields to hold the LED position
     */
    private int x;
    private int y;

    /**
     * Field to hold the LED size
     */
    private int size;


    /**
     * the Value of this LED (0 - ANIM_COUNT)
     */
    private int value;

    /**
     * the Color of this LED
     */
    private Color col;

    /**
     * 
     * @param x
     * @param y
     * @param size
     * @param c
     */
    public LED(int x, int y, int size, Color c) {
	this.x = x;
	this.y = y;
	this.size = size;
	setValue(0);
	col = c;
    }


    /**
     * Draws the LED
     */
    public void draw(){	

	glDisable(GL_TEXTURE_2D);

	//Draw Background
//	DrawUtils.drawColorRect(x, y, size, col);

	//Draw LED Texture
	DrawUtils.drawColoredTextureRect(x, y, size, size, col, BASE_LOCATION + value);
    }
    
    public int getX() {
	return x;
    }

    public int getY() {
	return y;
    }

    public int getSize() {
	return size;
    }

    public int getValue() {
	return value;
    }

    public void setX(int x) {
	this.x = x;
    }

    public void setY(int y) {
	this.y = y;
    }

    public void setPos(int x, int y){
	this.x = x;
	this.y = y;
    }

    public void setSize(int size) {
	this.size = size;
    }

    /**
     * Set the LED value (with bound checking)
     * @param value
     */
    public void setValue(int value) {
	if(value < 0)
	    value = 0;
	if(value > ANIM_COUNT)
	    value = ANIM_COUNT;
	this.value = value;
    }

    public Color getCol() {
	return col;
    }

    public void setCol(Color col) {
	this.col = col;
    }
}
