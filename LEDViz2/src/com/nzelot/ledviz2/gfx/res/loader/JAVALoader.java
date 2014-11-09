package com.nzelot.ledviz2.gfx.res.loader;

import com.nzelot.ledviz2.gfx.res.IResourceLoader;

/**
 * Pseudo loader to not add JAVA files as resources
 * @author Leon
 *
 */
public class JAVALoader implements IResourceLoader {

	@Override
	public Object load(String file) {
		return null;
	}

}
