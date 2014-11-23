package com.nzelot.ledviz2.ui;

import java.util.ArrayList;

import com.nzelot.ledviz2.gfx.core.Drawable;

public class UI implements Drawable {

	private boolean visible;

	private ArrayList<Layer> elements;

	public UI() {
		visible = true;
		elements = new ArrayList<Layer>();
	}

	@Override
	public void draw() {
		if(!visible)
			return;


		for(int i = 0; i < elements.size(); i++)
			elements.get(i).draw();
	}

	public void addElements(Layer... e){
		if(e != null && e.length > 0){
			for(int i = 0; i < e.length; i++)
				elements.add(e[i]);
		}
	}

	public ArrayList<Layer> getElements() {
		return elements;
	}

	public void setElements(ArrayList<Layer> elements) {
		this.elements = elements;
	}

	public Layer getLayer(int idx){
		if(idx > -1 && idx < elements.size())
			return elements.get(idx);
		else
			return null;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}
