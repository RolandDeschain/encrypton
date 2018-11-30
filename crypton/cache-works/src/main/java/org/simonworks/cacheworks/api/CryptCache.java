package it.oasi.crypter.engine.cache;

import java.io.Closeable;

public interface CryptCache extends Closeable {

	/**
	 * Perform the initialization operation
	 */
	void init();

	boolean contains(String container, String source);
	
	void store(String container, String source, String crypted);

	void update(String container, String source, String crypted);

	/**
	 * Recover a previous crypted value from container for source String
	 * 
	 * @param container
	 * @param source
	 * @return
	 */
	String recover(String container, String source);

	void clearContainer(String container);

	void clear();

	void close();

	/**
	 * Il nome dell'implementazione della cache, viene utilizzato come nome del
	 * file di properties da cercare nel folder con le configurazioni
	 * 
	 * @return the name cache
	 */
	String getName();

}
