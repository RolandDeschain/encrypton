package it.oasi.crypter.engine.cache;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;

/**
 * <p>
 * Questa implementazione permette di gestire la cache attraverso una Collection
 * su un db Mongo
 * </p>
 * 
 * <p>
 * Il Client MongoDB si collega ad un server mongo db. I parametri di
 * connessione vengono letti da un file di properties mongocache.properties, la
 * lista di parametri del file sono
 * </p>
 * 
 * <ul>
 * <li><b>db.url</b>: l'url di connessione a mongo db</li>
 * <li><b>db.dbname</b>: Il nome del db nel quale scrivere la cache</li>
 * </ul>
 * 
 * @author simone.stranieri
 * @author g.maestro
 *
 */
public class MongoDBCryptCacheImpl extends AbstractCryptCache implements CryptCache {

	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CryptCache.class);

	private MongoClient client;
	private MongoDatabase database;
	private Map<String, MongoCollection<Document>> cryptCachePool = new HashMap<String, MongoCollection<Document>>();

	private static final UpdateOptions updateOptions = new UpdateOptions().upsert(true);

	public MongoDBCryptCacheImpl() {
	}

	@Override
	public boolean contains(String container, String source) {
		Document first = doFind(container, source);
		return first != null;
	}

	@Override
	public String recover(String container, String source) {
		Document first = doFind(container, source);
		String result = null;
		if (first != null) {
			result = first.getString("value");
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("recover({},{})=<{}>", container, source, result);
			}	
		}
		return result;
	}

	private Document doFind(String container, String source) {
		Document _id = new Document("_id", source);
		MongoCollection<Document> collection = lookupCollection(container);
		FindIterable<Document> find = collection.find(_id);
		Document first = find.first();
		return first;
	}

	@Override
	public void store(String container, String source, String value) {
		Document elem = createDocumentToStore(source, value);
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("store({},{},{})", container, source, value);
		}
		lookupCollection(container).insertOne(elem);
	}

	@Override
	public void update(String container, String source, String value) {
		Document query = new Document("_id", source);
		Document update = new Document("$set", new Document("value", value));
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("update({},{},{})", container, source, value);
			LOGGER.info("fire upsert, query <{}>, update <{}>", query.toJson(new JsonWriterSettings(JsonMode.SHELL)), update.toJson(new JsonWriterSettings(JsonMode.SHELL)));
		}
		lookupCollection(container).updateOne(query, update, updateOptions);
	}

	@Override
	public void clearContainer(String container) {
		LOGGER.warn("Clearing (dropping) collection <{}>");
		lookupCollection(container).drop();
	}

	@Override
	public void clear() {
		database.drop();
	}

	MongoCollection<Document> lookupCollection(String container) {
		MongoCollection<Document> result = cryptCachePool.get(container);
		if (result == null) {
			result = database.getCollection(container);
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("caching just obtained collection <{}>", result);
			}
			cryptCachePool.put(container, result);
		}
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("lookupCollection({})=<{}>", container, result);
		}
		return result;
	}

	@Override
	public void close() {
		client.close();
	}

	public Document createDocumentToStore(String arg0, String arg1) {
		return new Document("_id", arg0).append("value", arg1);
	}

	@Override
	public void init() {

		String dbUrl = getConfiguration().getProperty("db.url");
		String dbName = getConfiguration().getProperty("db.dbname");

		client = new MongoClient(new MongoClientURI(dbUrl));
		database = client.getDatabase(dbName);
		LOGGER.info("Started MongoDB based Cache");

	}

	@Override
	public String getName() {
		return "mongocache";
	}

}
