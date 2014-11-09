package com.nzelot.ledviz2.gfx.res.loader;

import java.awt.Font;
import java.io.FileInputStream;





import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nzelot.ledviz2.gfx.res.IResourceLoader;

public class TTFLoader implements IResourceLoader {

	private final Logger l = LoggerFactory.getLogger(TTFLoader.class);

	@Override
	public Font load(String file) {
		Font f = null;
		try {
			f = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(file));
		} catch (Exception e) {
			l.error("Could not load resource: " + file, e);
		}
		return f;
	}

}
