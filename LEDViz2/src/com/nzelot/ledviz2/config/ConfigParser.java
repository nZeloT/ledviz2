package com.nzelot.ledviz2.config;

import java.io.BufferedReader;
import java.io.FileReader;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nzelot.ledviz2.LEDViz2;

public class ConfigParser {
	
	private final Logger l = LoggerFactory.getLogger(LEDViz2.class);
	
	private static ConfigParser inst;
	private ConfigParser(){}
	
	public static ConfigParser get(){
		if(inst == null)
			inst = new ConfigParser();
		
		return inst;
	}
	
	//////////////////////////////////////////
	
	private JSONObject root;
	
	public void init(){
		String json = "";
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader("config.json"));
			String in = "";
			
			while((in = reader.readLine()) != null)
				json += in;
			
			reader.close();
		} catch (Exception e) {
			l.error(e.toString());
		}
		
		root = new JSONObject(json);
	}
	
	public JSONObject visualization(){
		return root.getJSONObject("visualization");
	}
	
	public JSONObject controller(){
		return root.getJSONObject("controller");
	}
	
	public JSONObject ui(){
		return root.getJSONObject("ui");
	}
	
	public JSONObject overall(){
		return root.getJSONObject("overall");
	}
	
	public JSONObject special(){
		return root.getJSONObject("special");
	}
}
