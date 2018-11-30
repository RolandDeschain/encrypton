package it.oasi.crypter.engine.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BulkWritesMongoDBCryptCacheImpl implements CryptCache {
	
	private static final Logger LOGGER = LoggerFactory.getLogger( CryptCache.class );
	
	private CryptCache delegate;
	private static final int BULK_SIZE = 5000;
	
	private Map<String, Document> bulkBuffer;
	
	public BulkWritesMongoDBCryptCacheImpl() {
		delegate = new MongoDBCryptCacheImpl();
		bulkBuffer = new HashMap<String, Document>();
	}

	@Override
	public boolean contains(String container, String source) {
		// TODO To be implemented
		return false;
	}

	@Override
	public void store(String container, String source, String crypted) {
		
		if( ! bulkBuffer.containsKey(source) ) {
			bulkBuffer.put(source, ((MongoDBCryptCacheImpl) delegate).createDocumentToStore( source, crypted ));
		}
		
		if( bulkBuffer.size() >= BULK_SIZE ) {
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("Starting bulk writes...");
			}
			
			((MongoDBCryptCacheImpl) delegate).lookupCollection(container).insertMany( new ArrayList<Document>(bulkBuffer.values()) );
			bulkBuffer.clear();
		}
	}

	@Override
	public String recover(String container, String source) {
		return null;
	}
	
	@Override
	public void update(String container, String source, String crypted) {
		/**
		 * 
		 */
	}
	
	@Override
	public void clearContainer(String container) {
		delegate.clearContainer(container);
	}

	@Override
	public void clear() {
		delegate.clear();
	}

	@Override
	public void close() {
		delegate.close();
	}

	@Override
	public void init() {
		delegate.init();
	}

	@Override
	public String getName() {		
		return "mongobulkcache";
	}

}
