package com.nzelot.ledviz2.sound.player.attatched;

import com.apple.itunes.com.ClassFactory;
import com.apple.itunes.com.IiTunes;
import com.nzelot.ledviz2.sound.meta.METADataFetcher;
import com.nzelot.ledviz2.sound.meta.fetcher.ITunesTagFetcher;
import com.nzelot.ledviz2.sound.player.AttatchedPlayer;

public class ITunesPlayer extends AttatchedPlayer {

	private IiTunes itunes;
	
	public ITunesPlayer(Class<?> provider) {
		super(provider);
	}
	
	@Override
	public boolean init(int bands, int updateInterval) {
		super.init(bands, updateInterval);
		
		itunes = ClassFactory.createiTunesApp();

		return true;
	}

	@Override
	public void startPlayback() {
		itunes.play();
		playing = true;
	}

	@Override
	protected void pausePlayback() {
		playing = false;
		itunes.pause();
	}

	@Override
	public void stop() {
		if(playing)
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

	@Override
	public boolean isPlaying() {
		return false;
	}
}
