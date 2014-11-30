package com.nzelot.mpd;

import com.nzelot.ledviz2.config.ConfigParser;



public class MPD {
	private static MPD inst = null;
	private static String libLoc;
	private org.bff.javampd.server.MPD mpd = null;
	private boolean connected;
	
	private MPD() {}
	
	public static org.bff.javampd.server.MPD getMPD() {
		if(!getConnector().isConnected())
			getConnector().connect();
			
		return getConnector().getMpd();
	}
	
	public static String getLibLoc(){
		if(!getConnector().isConnected())
			getConnector().connect();
		
		return libLoc;
	}
	
	private static MPD getConnector(){
		if(inst == null){
			inst = new MPD();
		}
		
		return inst;
	}
	
	public void connect(){
		try {
			
			String ip	=	ConfigParser.get().special().getJSONObject("mpd_server").optString("ip", "localhost");
			int port	=	ConfigParser.get().special().getJSONObject("mpd_server").optInt("port", 6600);
			libLoc		= ConfigParser.get().special().getJSONObject("mpd_server").getString("mpdLibLoc");
						
			mpd = new org.bff.javampd.server.MPD.Builder().server(ip).port(port).build();
			connected = true;
			
		} catch (Exception e) {
			connected = false;
			e.printStackTrace();
		}
	}

	public void disconnect(){
		try {
			mpd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		connected = true;
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	public synchronized org.bff.javampd.server.MPD getMpd() {
		return mpd;
	}
}
