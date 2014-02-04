package com.nzelot.ledvizfx.gfx.res.loader;

import com.nzelot.ledvizfx.gfx.res.ResourceLoader;
import com.nzelot.ledvizfx.gfx.res.TextureLoader;

public class PNGLoader implements ResourceLoader {

    @Override
    public Integer load(String file) {
	Integer t = null;
	try {
	    t = TextureLoader.loadTexture(TextureLoader.loadImage(file));
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return t;
    }

}
