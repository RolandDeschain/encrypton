package it.oasi.crypter.engine.cache;

import java.util.HashMap;
import java.util.Map;

import it.oasi.crypter.engine.cache.CryptCache;
import it.oasi.crypter.engine.util.MemoryManager;

/**
 * L'istanza di questa classe implementa una politica di cache che agisce unicamente in memory
 * 
 * @author s.stranieri
 *
 */
public class InMemoryCryptCache implements CryptCache {
	
	private Map<String, Map<String, String>> cache = new HashMap<String, Map<String, String>>();
	
	public boolean contains(String container, String source) {
		boolean result = false;		
		Map<String, String> secondLevelCache = cache.get( container );
		if( secondLevelCache != null ) {			
			result = secondLevelCache.containsKey(source);
		}
		return result;
	}

	public void store(String container, String source, String crypted) {
		Map<String, String> secondLevelCache = cache.get( container );
		if( secondLevelCache == null ) {
			secondLevelCache = new HashMap<String, String>();
			cache.put( container, secondLevelCache );
		}
		secondLevelCache.put( source, crypted );
	}
	
	@Override
	public void update(String container, String source, String crypted) {
		store(container, source, crypted);
	}
	
	public String recover(String container, String source) {
		Map<String, String> secondLevelCache = cache.get(container);
		return secondLevelCache.get(source);
	}
	
	@Override
	public void clear() {
		if(MemoryManager.usedMemory() > 70) {
			cache.clear();
		}
	}
	
	@Override
	public void close() {
		clear();
	}

	@Override
	public void clearContainer(String container) {
		cache.get(container).clear();
	}

	@Override
	public void init() {
		cache = new HashMap<String, Map<String,String>>();
	}

	@Override
	public String getName() {
		return "in-memory-cache";
	}

}
