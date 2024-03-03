package org.simonworks.cacheworks.commons.mongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.simonworks.cacheworks.api.CryptCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BulkWritesMongoDBCryptCacheImpl implements CryptCache {

	private static final Logger LOGGER = LoggerFactory.getLogger(CryptCache.class);

	private final CryptCache delegate;
	private static final int BULK_SIZE = 5000;

	private final Map<String, Document> bulkBuffer;

	public BulkWritesMongoDBCryptCacheImpl() {
		delegate = new MongoDBCryptCacheImpl();
		bulkBuffer = new HashMap<>();
	}

	@Override
	public boolean contains(String container, String source) {
		// To be implemented
		return false;
	}

	@Override
	public void store(String container, String source, String crypted) {

		if (!bulkBuffer.containsKey(source)) {
			bulkBuffer.put(source, ((MongoDBCryptCacheImpl) delegate).createDocumentToStore(source, crypted));
		}

		if (bulkBuffer.size() >= BULK_SIZE) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Starting bulk writes...");
			}

			((MongoDBCryptCacheImpl) delegate).lookupCollection(container)
					.insertMany(new ArrayList<>(bulkBuffer.values()));
			bulkBuffer.clear();
		}
	}

	@Override
	public String recover(String container, String source) {
		return null;
	}

	@Override
	public void update(String container, String source, String crypted) {
		// Do Nothing
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
