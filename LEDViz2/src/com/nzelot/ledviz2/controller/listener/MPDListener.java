package com.nzelot.ledviz2.controller.listener;

import org.bff.javampd.player.MPDPlayerException;
import org.json.JSONObject;

import com.nzelot.ledviz2.controller.ChangeListener;
import com.nzelot.ledviz2.controller.ControllerEventType;
import com.nzelot.mpd.MPD;

public class MPDListener extends ChangeListener {

	private int songID 		= -1;
	private boolean playing = false;

	@Override
	public void run() {
		while(true){

			int newID = -1;
			boolean newPlaying = false;

			try {
				newID = MPD.getMPD().getPlayer().getCurrentSong().getId();
			} catch (MPDPlayerException e) {
				e.printStackTrace();
				return;
			}

			try {
				newPlaying = !MPD.getMPD().getMonitor().isStopped();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

			if(songID != newID){
				songID = newID;

				fireEvent(ControllerEventType.CET_TITLE_CHANGED);
			}

			if(newPlaying){
				if(!playing){
					fireEvent(ControllerEventType.CET_START);
					playing = newPlaying;
				}
			}else{
				if(playing){
					fireEvent(ControllerEventType.CET_STOP);
					playing = newPlaying;
				}
			}


			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}

		}
	}

	@Override
	protected boolean init(JSONObject specific) {
		return true;
	}
}
