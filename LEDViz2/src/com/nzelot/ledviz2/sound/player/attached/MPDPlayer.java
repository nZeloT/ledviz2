package com.nzelot.ledviz2.sound.player.attached;

import java.util.Timer;
import java.util.TimerTask;

import org.bff.javampd.player.MPDPlayerException;
import org.bff.javampd.song.MPDSong;
import org.json.JSONObject;

import com.nzelot.ledviz2.sound.meta.METADataFetcher;
import com.nzelot.ledviz2.sound.meta.fetcher.MPDTagFetcher;
import com.nzelot.ledviz2.sound.player.AttachedPlayer;
import com.nzelot.mpd.MPD;

public class MPDPlayer extends AttachedPlayer {

	private Timer mpdUpdateTimer;

	@Override
	public boolean init(JSONObject specific) {
		if(super.init(specific)){
			int mpdUpdate = specific.optInt("mpdUpdateInterval", 500);
			
			mpdUpdateTimer = new Timer();
			mpdUpdateTimer.schedule(new TimerTask() {

				private int songID = -1;

				@Override
				public void run() {
					if(isLoaded()){
						int newID = -1;

						try {
							newID = MPD.getMPD().getPlayer().getCurrentSong().getId();
						} catch (MPDPlayerException e) {
							e.printStackTrace();
						}
						
						if(songID != newID){
							songID = newID;
							updateMetaData();
						}
					}
				}
			}, mpdUpdate, mpdUpdate);
		}

		return false;
	}
	
	@Override
	public void stop() {
		mpdUpdateTimer.cancel();
		mpdUpdateTimer = null;
		super.stop();
	}

	@Override
	protected void attachedPlaySong() {
		try {
			int nr 			= Integer.parseInt(url);
			MPDSong target 	= MPD.getMPD().getPlaylist().getSongList().get(nr);

			MPD.getMPD().getPlayer().playId(target);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void attachedStartPlayback() {
		try {
			MPD.getMPD().getPlayer().play();
		} catch (MPDPlayerException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void attachedPausePlayback() {
		try {
			MPD.getMPD().getPlayer().pause();
		} catch (MPDPlayerException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void attachedStopPlayback() {
		attachedPausePlayback();
	}

	@Override
	protected long duration() {
		try {
			return MPD.getMPD().getPlayer().getTotalTime();
		} catch (MPDPlayerException e) {
			e.printStackTrace();
		}

		return 0;
	}

	@Override
	protected long position() {
		try {
			return MPD.getMPD().getPlayer().getElapsedTime();
		} catch (MPDPlayerException e) {
			e.printStackTrace();
		}

		return 0;
	}

	@Override
	protected METADataFetcher getFetcher() {
		return new MPDTagFetcher();
	}

}
