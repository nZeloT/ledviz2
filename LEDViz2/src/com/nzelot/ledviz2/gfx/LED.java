package com.nzelot.ledviz2.gfx;

import java.awt.Color;

public class LED{

    public static final int ANIM_COUNT = 99;

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
