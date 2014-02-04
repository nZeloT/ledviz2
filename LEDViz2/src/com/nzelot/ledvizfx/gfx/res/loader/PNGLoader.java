package com.nzelot.ledvizfx.gfx.res.loader;

import com.nzelot.ledviz2.gfx.core.Texture;
import com.nzelot.ledviz2.gfx.core.TextureLoader;
import com.nzelot.ledvizfx.gfx.res.ResourceLoader;

public class PNGLoader implements ResourceLoader {

    @Override
    public Texture load(String file) {
	Texture t = null;
	try {
	    t = TextureLoader.loadTexture(TextureLoader.loadImage(file));
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return t;
    }

}
