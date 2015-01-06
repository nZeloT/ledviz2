package com.nzelot.ledviz2.controller;

import org.json.JSONObject;

public abstract class ChangeListener implements Runnable {
	
	private ControllerEvent e;
	
	protected abstract boolean init(JSONObject specific);
	
	protected void fireEvent(ControllerEventType t){
		System.out.println(t);
		e.event(t);
	}
	
	public void setEvent(ControllerEvent e){
		synchronized(e){
			this.e = e;
		}
	}
}
