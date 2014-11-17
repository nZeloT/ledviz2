package com.nzelot.ledviz2.sound.player.attatched;

import com.nzelot.ledviz2.sound.meta.METADataFetcher;
import com.nzelot.ledviz2.sound.player.AttatchedPlayer;

public class MPDPlayer extends AttatchedPlayer {

	public MPDPlayer(Class<?> provider) {
		super(provider);
	}

	@Override
	protected void startPlayback() {
		
	}

	@Override
	protected void pausePlayback() {
		
	}

	@Override
	public void stop() {
		
	}

	@Override
	protected long duration() {
		return 0;
	}

	@Override
	protected long position() {
		return 0;
	}

	@Override
	protected METADataFetcher getFetcher() {
		return null;
	}

	@Override
	public boolean isPlaying() {
		return false;
	}

}
