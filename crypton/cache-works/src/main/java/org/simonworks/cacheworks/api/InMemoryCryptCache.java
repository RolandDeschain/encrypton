package org.simonworks.cacheworks.api;

import java.util.HashMap;
import java.util.Map;

import org.simonworks.cacheworks.util.MemoryManager;

/**
 * L'istanza di questa classe implementa una politica di cache che agisce unicamente in memory
 * 
 * @author s.stranieri
 *
 */
public class InMemoryCryptCache implements CryptCache {
	
	private Map<String, Map<String, String>> cache = new HashMap<>();
	
	public boolean contains(String container, String source) {
		boolean result = false;		
		Map<String, String> secondLevelCache = cache.get( container );
		if( secondLevelCache != null ) {			
			result = secondLevelCache.containsKey(source);
		}
		return result;
	}

	public void store(String container, String source, String crypted) {
		Map<String, String> secondLevelCache = cache.computeIfAbsent(container, k -> cache.put( container, new HashMap<String, String>() ));
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
		cache = new HashMap<>();
	}

	@Override
	public String getName() {
		return "in-memory-cache";
	}

}
