package com.nzelot.mpd;



public class MPD {
	private static MPD inst = null;
	private org.bff.javampd.MPD mpd = null;
	private boolean connected;
		
	private MPD() {}
	
	public static org.bff.javampd.MPD getMPD() {
		return getMPD(true);
	}
	
	public static org.bff.javampd.MPD getMPD(boolean autoConnect) {
		if(!getConnector().isConnected() && autoConnect)
			getConnector().connect();
			
		return getConnector().getMpd();
	}
	
	public static MPD getConnector(){
		if(inst == null){
			inst = new MPD();
		}
		
		return inst;
	}
	
	public void connect(){
		try {
			mpd = new org.bff.javampd.MPD.Builder().server("192.168.2.102").port(6600).build();
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
	
	public synchronized org.bff.javampd.MPD getMpd() {
		return mpd;
	}
}
