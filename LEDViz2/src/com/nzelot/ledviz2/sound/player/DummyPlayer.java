package com.nzelot.ledviz2.sound.player;

import com.nzelot.ledviz2.meta.METADataFetcher;
import com.nzelot.ledviz2.meta.fetcher.DummyTagFetcher;
import com.nzelot.ledviz2.sound.Player;

public class DummyPlayer extends Player {
	@Override
	public METADataFetcher getFetcher() {
		return new DummyTagFetcher();
	}

	@Override
	protected long duration() {
		return 0;
	}

	@Override
	protected long position() {
		return 0;
	}

}
