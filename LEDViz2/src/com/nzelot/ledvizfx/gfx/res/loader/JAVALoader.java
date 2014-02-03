package com.nzelot.ledvizfx.gfx.res.loader;

import com.nzelot.ledvizfx.gfx.res.ResourceLoader;

/**
 * Pseudo loader to not add JAVA files as resources
 * @author Leon
 *
 */
public class JAVALoader implements ResourceLoader {

    @Override
    public Object load(String file) {
	return null;
    }

}
