package com.nzelot.ledviz2.sound.player;

import org.bff.javampd.player.MPDPlayerException;

import com.nzelot.ledviz2.meta.METADataFetcher;
import com.nzelot.ledviz2.meta.fetcher.MPDTagFetcher;
import com.nzelot.ledviz2.sound.Player;
import com.nzelot.mpd.MPD;

public class MPDPlayer extends Player {

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
	public METADataFetcher getFetcher() {
		return new MPDTagFetcher();
	}

}
