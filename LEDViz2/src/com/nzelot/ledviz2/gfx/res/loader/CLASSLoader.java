package com.nzelot.ledviz2.gfx.res.loader;

import com.nzelot.ledviz2.gfx.res.ResourceLoader;

public class CLASSLoader implements ResourceLoader {

    /**
     * Pseudo loader to prevent .class files from beeing loaded as resources
     */
    @Override
    public Object load(String file) {
	return null;
    }

}
