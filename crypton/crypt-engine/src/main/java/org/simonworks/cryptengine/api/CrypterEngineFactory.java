package org.simonworks.cryptengine.api;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.simonworks.cacheworks.api.CryptCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum CrypterEngineFactory {
	
	INSTANCE;
	
	private static final Logger LOGGER = LoggerFactory.getLogger( CrypterEngineFactory.class );

	private Map<String, CrypterEngine> enginesPool;
	private Properties engines;
	private Object mutex = new Object();
	
	private CrypterEngineFactory() {
		enginesPool = new HashMap<>();
		
		engines = new Properties();
		try {
			engines.load( CrypterEngineFactory.class.getResourceAsStream( "/engines.factory.properties" )) ;
		} catch (Exception e) {
			throw new IllegalArgumentException( "Can't load file engines.factory.properties from etc dir" , e );
		}
	}
	
	public CrypterEngine getInstance(String engineId, CryptCache cryptCache) {
		CrypterEngine result = enginesPool.get(engineId);
		if(result == null) {
			String className = engines.getProperty(engineId);
			if(className == null) {
				throw new IllegalArgumentException( String.format("Unknown crypter engine id %s", engineId) );
			}
			synchronized( mutex ) {
				try {
					Constructor<?> constructor = Class.forName(className).getConstructor(CryptCache.class);
					result = (CrypterEngine) constructor.newInstance( cryptCache );
					LOGGER.debug("Generated instance for class {} with engineId {}", className, engineId);
					enginesPool.put(engineId, result);
				} catch (NoSuchMethodException | 
						SecurityException | 
						ClassNotFoundException | 
						InstantiationException |
						IllegalAccessException |
						IllegalArgumentException |
						InvocationTargetException e) {
					throw new IllegalArgumentException( String.format("Can't instantiate class %s with parameter %s", className, cryptCache), e );
				}
			}
		}
		return result;
	}
	
	public Set<Object> listEngines() {
		return engines.keySet();
	}

	
}
