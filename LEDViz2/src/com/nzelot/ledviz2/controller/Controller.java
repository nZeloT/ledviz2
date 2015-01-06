package com.nzelot.ledviz2.controller;

import org.json.JSONObject;

import com.nzelot.ledviz2.LEDViz2;
import com.nzelot.ledviz2.meta.METAData;
import com.nzelot.ledviz2.meta.METADataFetcherUpdateEvent;
import com.nzelot.ledviz2.sound.Player;
import com.nzelot.ledviz2.sound.PlayerUpdateEvent;

public class Controller {
	
	protected Player p;
	
	private METAData meta;
	private PlayerUpdateEvent update;
	
	private ChangeListener l;
	private Thread listenerThread;
	
	public Controller() {
		meta = new METAData("");
	}
	
	public boolean init(JSONObject specific){
		try {
			String className 	  =	specific.getJSONObject("player").getString("class");
			JSONObject obj	  	  =	specific.getJSONObject("player").getJSONObject("specific");
			Class<?> clazz		  =	LEDViz2.class.getClassLoader().loadClass(className);
			p = (Player) clazz.newInstance();

			if(!p.init(obj))
				return false;
			
			className		= specific.getJSONObject("listener").getString("class");
			obj	  	  		= specific.getJSONObject("listener").getJSONObject("specific");
			clazz		  	= LEDViz2.class.getClassLoader().loadClass(className);
			l = (ChangeListener) clazz.newInstance();
			l.init(obj);
			l.setEvent(e -> {
				switch (e) {
				case CET_START:
					p.start();
					updateMetaData();
					break;
				
				case CET_STOP:
					p.stop();
					break;
					
				case CET_TITLE_CHANGED:
					updateMetaData();
					break;
					
				default:
					break;
				}
			});
			listenerThread = new Thread(l);
			listenerThread.start();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean exit(){
		if(listenerThread != null){
			listenerThread.interrupt();
			listenerThread = null;
		}
		return p.exit();
	}
	
	//Handle all the MEATData stuff
	public METAData getMetaData(){
		if(!p.isLoaded())
			return null;
		else
			return getMETA();
	}
	
	protected void updateMetaData(){
		p.getFetcher().init(new METADataFetcherUpdateEvent() {
			@Override
			public void update(METAData d) {
				update.update(d);
				setMETAData(d);
			}
		});		
	}
	
	private void setMETAData(METAData d){
		synchronized (meta) {
			meta = new METAData(d);
		}
	}
	
	private METAData getMETA(){
		METAData d = null;
		synchronized (meta) {
			d = new METAData(meta);
		}
		return d;
	}
	
	protected void firePlayUpdateEvent(){
		update.update(getMETA());
	}
	
	public void setUpdate(PlayerUpdateEvent update) {
		this.update = update;
	}
	
	//Map some of the key player functionality
	public long getDuration(){
		return p.getDuration();
	}

	public long getPosition(){
		return p.getPosition();
	}

	public boolean hasNewData(){
		return p.hasNewData();
	}
	
	public boolean isLoaded(){
		return p.isLoaded();
	}
	
	public boolean isRunning(){
		return p.isRunning();
	}
	
	public float[] getSpectrumData(){
		return p.getSpectrumData();
	}
}
