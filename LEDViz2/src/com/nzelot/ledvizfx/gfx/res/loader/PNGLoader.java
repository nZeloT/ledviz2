package com.nzelot.ledvizfx.gfx.res.loader;

import java.io.FileInputStream;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import com.nzelot.ledvizfx.gfx.res.ResourceLoader;

public class PNGLoader implements ResourceLoader {

    @Override
    public Texture load(String file) {
	Texture t = null;
	try {
	    t = TextureLoader.getTexture("PNG", new FileInputStream(file), GL11.GL_NEAREST);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return t;
    }

}
