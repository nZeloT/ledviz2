package com.nzelot.ledvizfx.gfx.res.loader;

import java.awt.Font;
import java.io.FileInputStream;


import com.nzelot.ledvizfx.gfx.res.ResourceLoader;

public class TTFLoader implements ResourceLoader {

    @Override
    public Font load(String file) {
	Font f = null;
	
	try {
	    f = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(file));
	} catch (Exception e) {
	    e.printStackTrace();
	}
    
	return f;
    }

}
