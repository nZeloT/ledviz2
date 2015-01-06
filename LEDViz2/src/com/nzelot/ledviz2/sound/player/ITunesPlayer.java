package com.nzelot.ledviz2.sound.player;

import org.json.JSONObject;

import com.apple.itunes.com.ClassFactory;
import com.apple.itunes.com.IiTunes;
import com.nzelot.ledviz2.meta.METADataFetcher;
import com.nzelot.ledviz2.meta.fetcher.ITunesTagFetcher;
import com.nzelot.ledviz2.sound.Player;

public class ITunesPlayer extends Player {

	private IiTunes itunes;
	
	@Override
	public boolean init(JSONObject specific) {
		super.init(specific);
		
		itunes = ClassFactory.createiTunesApp();

		return true;
	}

	@Override
	protected long duration() {
		return itunes.currentTrack().duration();
	}

	@Override
	protected long position() {
		return itunes.playerPosition();
	}

	@Override
	public METADataFetcher getFetcher() {
		return new ITunesTagFetcher();
	}
}
