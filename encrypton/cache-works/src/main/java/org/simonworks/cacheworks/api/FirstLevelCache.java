package org.simonworks.cacheworks.api;


/**
 * Proxy implementation di Cache che agisce come cache di primo livello e che delega operazioni effettive di caching verso un'altra istanza della stessa interface
 * 
 * @author s.stranieri
 *
 */
class FirstLevelCache extends AbstractCryptCache {
	
	private final CryptCache delegate;
	
	private final CryptCache inMemoryCache = new InMemoryCryptCache();
	
	public FirstLevelCache(CryptCache delegate) {
		this.delegate = delegate;
	}

	@Override
	public void init() {
		inMemoryCache.init();
		delegate.init();
	}
	
	@Override
	public boolean contains(String container, String source) {
		if(!inMemoryCache.contains(container, source)) {
			return delegate.contains(container, source);
		}
		return true;
	}

	@Override
	public void store(String container, String source, String crypted) {
		inMemoryCache.store(container, source, crypted);
		delegate.store(container, source, crypted);
	}

	@Override
	public void update(String container, String source, String crypted) {
		inMemoryCache.update(container, source, crypted);
		delegate.update(container, source, crypted);
	}

	@Override
	public String recover(String container, String source) {
		String cryptedValue;

		if (inMemoryCache.contains(container, source)) {
			cryptedValue = inMemoryCache.recover(container, source);
		} else {
			cryptedValue = delegate.recover(container, source);
			if ( cryptedValue != null) {
				inMemoryCache.store(container, source, cryptedValue);
			}
		}
		
		// Don't worry, in-memory-cache knows if to clear or not ;-)
		inMemoryCache.clear();

		return cryptedValue;
	}

	@Override
	public void clearContainer(String container) {
		inMemoryCache.clearContainer(container);
		delegate.clearContainer(container);
	}

	@Override
	public void clear() {
		inMemoryCache.clear();
		delegate.clear();
	}

	@Override
	public void close() {
		inMemoryCache.close();
		delegate.close();
	}

	@Override
	public String getName() {
		return "proxy.of." + delegate.getName();
	}

}
