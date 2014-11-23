package com.nzelot.ledviz2.sound.player.attached;

import org.bff.javampd.exception.MPDPlayerException;

import com.nzelot.ledviz2.sound.meta.METADataFetcher;
import com.nzelot.ledviz2.sound.meta.fetcher.MPDTagFetcher;
import com.nzelot.ledviz2.sound.player.AttachedPlayer;

public class MPDPlayer extends AttachedPlayer {

	@Override
	protected void attachedStartPlayback() {
		try {
			com.nzelot.mpd.MPD.getMPD().getPlayer().play();
		} catch (MPDPlayerException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void attachedPausePlayback() {
		try {
			com.nzelot.mpd.MPD.getMPD().getPlayer().pause();
		} catch (MPDPlayerException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void attachedStopPlayback() {
		try {
			com.nzelot.mpd.MPD.getMPD().getPlayer().stop();
		} catch (MPDPlayerException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected long duration() {
		try {
			return com.nzelot.mpd.MPD.getMPD().getPlayer().getTotalTime();
		} catch (MPDPlayerException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	@Override
	protected long position() {
		try {
			return com.nzelot.mpd.MPD.getMPD().getPlayer().getElapsedTime();
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
