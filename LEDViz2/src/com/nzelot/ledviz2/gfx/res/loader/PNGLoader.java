package com.nzelot.ledviz2.gfx.res.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nzelot.ledviz2.gfx.core.Texture;
import com.nzelot.ledviz2.gfx.core.TextureLoader;
import com.nzelot.ledviz2.gfx.res.IResourceLoader;

public class PNGLoader implements IResourceLoader {

	private final Logger l = LoggerFactory.getLogger(PNGLoader.class);

	@Override
	public Texture load(String file) {
		Texture t = null;
		try {
			t = TextureLoader.loadTexture(TextureLoader.loadImage(file));
		} catch (Exception e) {
			l.error("Could not load resource: " + file, e);
		}
		return t;
	}

}
