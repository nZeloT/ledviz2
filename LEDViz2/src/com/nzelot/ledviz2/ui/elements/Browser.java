package com.nzelot.ledviz2.ui.elements;

import java.awt.Color;

import org.json.JSONObject;

import com.nzelot.ledviz2.sound.meta.METAData;

public abstract class Browser extends UIElement {

	protected List<String> content;

	protected Text heading;

	protected Filter filter;

	public void init(int x, int y, int width, int heigth, JSONObject specific){
		
		super.setX(x);
		super.setY(y);
		super.setWidth(width);
		super.setHeigth(heigth);

		super.setBgColor(Color.BLACK);
		super.setFgColor(Color.WHITE);
		
		content = new List<String>(x, y+40, width, heigth-40, 15);
		heading = new Text(x, y, 18);
		
		init(specific);
	}
	
	public abstract void init(JSONObject specific);

	public void increseSelectedIdx(){
		content.increseSelectedIdx();
	}

	public void decreaseSelectedIdx(){
		content.decreaseSelectedIdx();
	}

	public abstract void updateSelection(METAData d);
	public abstract String getSelection();
	
	public abstract void enterSelection();
	
	@Override
	public void setBgColor(Color bgColor) {
		super.setBgColor(bgColor);
		if(content != null)
			content.setBgColor(bgColor);
	}

	public abstract boolean isPlayableSelected();

	@Override
	public void draw() {
		heading.draw();
		content.draw();
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public static class Filter {
		public static final Filter NO_FILTER = new Filter(){
			public boolean check(String end) {
				return true;
			};
		};

		private String filter;

		private Filter(){}

		public Filter(String... end) {
			filter = "";
			addEnd(end);
		}

		public void addEnd(String... end){
			if(end != null && end.length > 0)
				for(String s : end)
					filter += s + ";";
		}

		public String[] getEnds(){
			return filter.split(";");
		}

		public boolean check(String end){
			return filter.contains(end);
		}
	}
}
