package org.simonworks.cryptalgorithms.commons;

import org.simonworks.cacheworks.api.AbstractCryptCache;

public abstract class AbstractCrypCacheAlgorithm extends AbstractCryptCache {
	
	@Override
	public void store(String container, String source, String crypted) {
		// Do Nothing here :-)
	}

	@Override
	public void update(String container, String source, String crypted) {
		// Do Nothing here :-)
	}

	@Override
	public void clearContainer(String container) {
		// Do Nothing here :-)
	}

	@Override
	public void clear() {
		// Do Nothing here :-)
	}

	@Override
	public void close() {
		// Do Nothing here :-)
	}

}
