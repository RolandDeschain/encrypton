package org.simonworks.cacheworks.api;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum CryptCacheFactory {
	
	INSTANCE;
	
	private static final Logger LOGGER = LoggerFactory.getLogger( CryptCacheFactory.class );
	
	public static final String NOOP_CACHE = "noopCache";

	private final Map<String, CryptCache> cachesPool;
	private final Properties caches;
	private final Object mutex = new Object();
	
	CryptCacheFactory() {
		cachesPool = new HashMap<>();
		cachesPool.put( NOOP_CACHE, new NoopCryptCache() );
		
		caches = new Properties();
		try {
			caches.load( CryptCacheFactory.class.getResourceAsStream("/caches.factory.properties")  );
		} catch (Exception e) {
			throw new IllegalArgumentException( "Can't load file caches.factory.properties from etc dir" , e );
		}
	}
	
	public CryptCache getCache(String cacheId) {
		return getCache(cacheId, false);
	}
	
	public CryptCache getCache(String cacheId, boolean useFirstLevelCache) {
		if(!cachesPool.containsKey(cacheId)) {
			String className = caches.getProperty(cacheId);
			if(className == null) {
				throw new IllegalArgumentException( String.format("Unknown crypt cache id %s", cacheId) );
			}
			synchronized( mutex ) {
				try {
					Constructor<?> constructor = Class.forName(className).getConstructor();
					CryptCache result = (CryptCache) constructor.newInstance();
					LOGGER.debug("Generated instance for class {} with cacheId {}", className, cacheId);
					
					if(useFirstLevelCache) {
						cachesPool.put(cacheId, new FirstLevelCache(result));
					} else {
						cachesPool.put(cacheId, result);
					}
				} catch (	NoSuchMethodException 	|
							SecurityException 		| 
							ClassNotFoundException  |
							InstantiationException  |
							IllegalAccessException 	|
							IllegalArgumentException|
							InvocationTargetException e) {
					throw new IllegalArgumentException( String.format("Can't instantiate class %s", className), e );
				}
			}
		}
		return cachesPool.get(cacheId);
	}
	
	public Set<Object> listCaches() {
		return caches.keySet();
	}

}
