package com.nzelot.ledviz2.meta.fetcher;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.nzelot.ledviz2.meta.METADataFetcher;

public class DummyTagFetcher extends METADataFetcher {

	@Override
	protected Map<String, String> fetchMetaStrings() {
		Map<String, String> data = new HashMap<String, String>();
		
		data.put("album", "");
		data.put("artist", "");
		data.put("title", "");
		data.put("file", "");
		
		return null;
	}

	@Override
	protected BufferedImage fetchAlbum(Map<String, String> tags) {
		return null;
	}

}
