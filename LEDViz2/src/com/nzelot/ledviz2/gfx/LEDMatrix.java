package com.nzelot.ledviz2.gfx;

import java.awt.Color;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nzelot.ledviz2.gfx.core.Drawable;
import com.nzelot.ledviz2.gfx.core.Sprite;

public class LEDMatrix implements Drawable {

	private int width;
	private int height;

	private int ledSize;

	private LED[][] matrix;

	private Sprite sprite;
	
	private Color def;

	public LEDMatrix(int width, int height, JSONObject specific){
		
		this.ledSize 	= 	specific.optInt("LEDSize", 8);
		JSONArray color	= 	specific.getJSONArray("BaseColor");
		this.width 		= 	width  / ledSize;
		this.height 	= 	height / ledSize;
		this.def		= 	new Color(color.getInt(0), color.getInt(1), color.getInt(2));
		
		initMatrix();

		setColor(def);

		sprite = new Sprite("res/textures/led", 10, 10, 64);
	}

	@Override
	public void draw() {
		sprite.beginRender();
		for(int i = 0; i < height; i++){
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
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				matrix[i][j].setValue(val);
	}

	public void setColor(Color c){
		Color[][] colors = new Color[height][width];

		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				colors[i][j] = c;

		setColor(colors);
	}

	public void setColor(Color[][] colors){
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				matrix[i][j].setCol(colors[i][j]);
	}

	public Color[][] getColor(){
		Color[][] colors = new Color[height][width];

		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				colors[i][j] = matrix[i][j].getCol();

		return colors;
	}
	
	public void setColorToDef(){
		setColor(def);
	}

	private void initMatrix() {
		matrix = new LED[height][width];

		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				matrix[i][j] = new LED(j*ledSize, i*ledSize, ledSize, Color.BLACK);
			}
		}
	}
}
