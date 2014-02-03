package com.nzelot.ledvizfx.ui.elements;

import java.awt.Color;
import java.util.ArrayList;

import com.nzelot.ledvizfx.ui.fx.DrawUtils;

public class List<E> extends UIElement {

    private ArrayList<E> entries;
    
    private int rowHeigth;
    
    private int selIdx;
    
    private Text txt;
    
    public List(int x, int y, int width, int heigth, int rH) {
	super(x, y, width, heigth);
	
	entries = new ArrayList<E>();
	
	rowHeigth = rH;
	
	selIdx = 0;
	
	txt = new Text(0, 0, rowHeigth);
    }
    
    @SuppressWarnings("unchecked")
    public void addEntries(E... en){
	if(en != null && en.length > 0){
	    for(int i = 0; i < en.length; i++)
		entries.add(en[i]);
	}
    }
    
    public ArrayList<E> getEntries() {
	return entries;
    }
    
    public void setEntries(ArrayList<E> entries) {
	this.entries = entries;
    }
    
    public int getRowHeigth() {
	return rowHeigth;
    }
    
    public void setRowHeigth(int rowHeigth) {
	this.rowHeigth = rowHeigth;
	txt.setHeigth(rowHeigth);
    }
    
    public void setSelectedIdx(int selIdx) {
	if(selIdx >= 0 && selIdx < entries.size())
	    this.selIdx = selIdx;
    }

    public int getSelectedIdx() {
	return selIdx;
    }
    
    public E getSelectedEntry(){
	return entries.get(selIdx);
    }

    public void increseSelectedIdx(){
	setSelectedIdx(selIdx+1);
    }

    public void decreaseSelectedIdx(){
	setSelectedIdx(selIdx-1);
    }
    
    @Override
    public void setFgColor(Color fgColor) {
        super.setFgColor(fgColor);
        if(txt != null)
            txt.setFgColor(fgColor);
    }

    @Override
    public void draw() {
	int drawCount = (int) ((heigth+1.0f) / rowHeigth);
	if(drawCount % 2 != 1)
	    drawCount --;

	int sym = (drawCount-1)/2;

	for(int i = selIdx-sym; i <= selIdx+sym; i++){
	    if(i >= 0 && i < entries.size()){
		if((i-selIdx+sym) % 2 == 0 || i == selIdx){
		    Color c = new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), (i == selIdx ? bgColor.getAlpha() : bgColor.getAlpha()/4));
		    DrawUtils.drawColorRect(x, y+(i-selIdx+sym)*rowHeigth, width, rowHeigth, c);
		}

		txt.setX(x+10);
		txt.setY(y + (i-selIdx+sym)*rowHeigth-2);
		txt.setText(entries.get(i).toString());
		txt.draw();
	    }
	}
    }

}
