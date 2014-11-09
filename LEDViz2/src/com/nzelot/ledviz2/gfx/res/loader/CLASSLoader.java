package com.nzelot.ledviz2.gfx.res.loader;

import com.nzelot.ledviz2.gfx.res.IResourceLoader;

public class CLASSLoader implements IResourceLoader {

	/**
	 * Pseudo loader to prevent .class files from being loaded as resources
	 */
	@Override
	public Object load(String file) {
		return null;
	}

}
