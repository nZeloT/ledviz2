package com.nzelot.ledviz2.gfx.core;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.opengl.Display;

import com.nzelot.ledviz2.ui.utils.RenderUtils;

public class Painter{
    private ArrayList<Drawable> elements;
    
    private Color baseColor;
    
    public Painter() {
	elements = new ArrayList<Drawable>();
	baseColor = Color.CYAN;
    }
    
    public void repaint() {
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	
	//Draw Background
	RenderUtils.drawColoredRectangle(0, 0, Display.getWidth(), Display.getHeight(), baseColor);

	//Draw Elements
	for(int i = 0; i < elements.size(); i++)
	    elements.get(i).draw();
    }
    
    public void addElements(Drawable... d){
	if(d != null && d.length > 0){
	    for(int i = 0; i < d.length; i++)
		elements.add(d[i]);
	}
    }

    public void setElements(ArrayList<Drawable> elements) {
	this.elements = elements;
    }
    
    public ArrayList<Drawable> getElements() {
	return elements;
    }
    
    public Color getBaseColor() {
	return baseColor;
    }
    
    public void setBaseColor(Color baseColor) {
	this.baseColor = baseColor;
    }
}
