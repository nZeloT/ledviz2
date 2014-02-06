package com.nzelot.ledviz2.gfx;

import java.awt.Color;

import com.nzelot.ledviz2.gfx.core.Drawable;
import com.nzelot.ledviz2.gfx.core.Sprite;

public class LEDMatrix implements Drawable {

    private int width;
    private int heigth;

    private int ledSize;

    private LED[][] matrix;
    
    private Sprite sprite;

    public LEDMatrix(int width, int heigth, int ledSize, Color col){
	super();
	this.width = width;
	this.heigth = heigth;
	this.ledSize = ledSize;
	initMatrix();

	setColor(col);
	
	sprite = new Sprite("res/textures/led", 10, 10, 64);
    }

    public LEDMatrix(int width, int heigth, int ledSize, Color[][] colors) {
	super();
	this.width = width;
	this.heigth = heigth;
	this.ledSize = ledSize;

	initMatrix();
	setColor(colors);
    }

    @Override
    public void draw() {
	sprite.beginRender();
	for(int i = 0; i < heigth; i++){
	    for(int j = 0; j < width; j++){
		sprite.renderFrame(matrix[i][j].getX(), matrix[i][j].getY(), ledSize, matrix[i][j].getValue(), matrix[i][j].getCol());
	    }
	}
	sprite.endRender();
    }

    public LED[][] getMatrix() {
	return matrix;
    }

    public int getLedSize() {
	return ledSize;
    }

    public void setOverallValue(int val){
	for(int i = 0; i < heigth; i++)
	    for(int j = 0; j < width; j++)
		matrix[i][j].setValue(val);
    }

    public void setColor(Color c){
	Color[][] colors = new Color[heigth][width];

	for(int i = 0; i < heigth; i++)
	    for(int j = 0; j < width; j++)
		colors[i][j] = c;

	setColor(colors);
    }

    public void setColor(Color[][] colors){
	for(int i = 0; i < heigth; i++)
	    for(int j = 0; j < width; j++)
		matrix[i][j].setCol(colors[i][j]);
    }

    public Color[][] getColor(){
	Color[][] colors = new Color[heigth][width];

	for(int i = 0; i < heigth; i++)
	    for(int j = 0; j < width; j++)
		colors[i][j] = matrix[i][j].getCol();

	return colors;
    }

    private void initMatrix() {
	matrix = new LED[heigth][width];

	for(int i = 0; i < heigth; i++){
	    for(int j = 0; j < width; j++){
		matrix[i][j] = new LED(j*ledSize, i*ledSize, ledSize, Color.BLACK);
	    }
	}
    }
}
