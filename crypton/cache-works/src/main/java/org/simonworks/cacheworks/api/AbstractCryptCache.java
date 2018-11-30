package it.oasi.crypter.engine.cache;

import java.util.Properties;

/**
 * 
 * Una implementazione astratta della cache che fornisce servizi base
 * 
 * @author g.maestro
 *
 */
public abstract class AbstractCryptCache implements CryptCache {

	/**
	 * Carica le properties leggendo dal classpath nel pachkage base il file
	 * <code>{@link CryptCache#getName()}.properties</code>
	 * 
	 * @return le properties dela cache
	 */
	protected Properties getConfiguration() {
		Properties result = new Properties();
		try {
			result.load(CryptCacheFactory.class.getResourceAsStream("/" + getName() + ".properties"));
			return result;
		} catch (Exception e) {
			throw new IllegalArgumentException("Can't load file caches.factory.properties from etc dir", e);
		}
	}

}
