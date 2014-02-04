package com.nzelot.ledviz2.ui;

import java.util.ArrayList;

import com.nzelot.ledviz2.gfx.core.Drawable;
import com.nzelot.ledviz2.ui.elements.UIElement;

public class UI implements Drawable {

    private boolean visible;

    private ArrayList<UIElement> elements;

    public UI() {
	visible = true;
	elements = new ArrayList<UIElement>();
    }
    
    @Override
    public void draw() {
	if(!visible)
	    return;


	for(int i = 0; i < elements.size(); i++)
	    elements.get(i).draw();
    }

    public void addElements(UIElement... e){
	if(e != null && e.length > 0){
	    for(int i = 0; i < e.length; i++)
		elements.add(e[i]);
	}
    }

    public ArrayList<UIElement> getElements() {
	return elements;
    }

    public void setElements(ArrayList<UIElement> elements) {
	this.elements = elements;
    }

    public boolean isVisible() {
	return visible;
    }

    public void setVisible(boolean visible) {
	this.visible = visible;
    }

}
