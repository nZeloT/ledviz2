package com.nzelot.ledviz2.sound.player.attached;

import org.json.JSONObject;

import com.apple.itunes.com.ClassFactory;
import com.apple.itunes.com.IiTunes;
import com.nzelot.ledviz2.sound.meta.METADataFetcher;
import com.nzelot.ledviz2.sound.meta.fetcher.ITunesTagFetcher;
import com.nzelot.ledviz2.sound.player.AttachedPlayer;

public class ITunesPlayer extends AttachedPlayer {

	private IiTunes itunes;
	
	@Override
	public boolean init(JSONObject specific) {
		super.init(specific);
		
		itunes = ClassFactory.createiTunesApp();

		return true;
	}

	@Override
	public void attachedStartPlayback() {
		itunes.play();
	}

	@Override
	protected void attachedPausePlayback() {
		itunes.pause();
	}

	@Override
	public void attachedStopPlayback() {
		if(isPlaying())
			pause();
		if(isLoaded()){
			itunes.stop();
		}
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
	protected METADataFetcher getFetcher() {
		return new ITunesTagFetcher();
	}

}
