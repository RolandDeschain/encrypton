package org.simonworks.smartsequences.commons.er.h2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.simonworks.smartsequences.commons.er.AbstractErCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class H2CryptCache extends AbstractErCache {

	private static final Logger LOGGER = LoggerFactory.getLogger(H2CryptCache.class);
	
	private static final List<String> chars = Arrays.asList( 
			"J", "K", "Y", "X", "W", 
			"A", "B", "C", "D", "E", 
			"F", "G", "H", "I", "L", 
			"M", "N", "O", "P", "Q", 
			"R", "S", "T", "U", "V", "Z", 
			"0", "1", "2", "3", "4", 
			"5", "6", "7", "8", "9" );
	
	private Map<String, Connection> map = new HashMap<String, Connection>();
	private Properties configuration;

	@Override
	public void init() {
		configuration = getConfiguration();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void close() {
		for (Entry<String, Connection> entry : map.entrySet()) {
			try {
				entry.getValue().close();
			} catch (SQLException e) {
				LOGGER.error("Cannot close connection for " + entry.getKey());
			}
		}
	}

	@Override
	public String getName() {
		return "h2cache";
	}

	@Override
	protected Connection getConnection(String container, String source) {

		String multicontainer = configuration.getProperty("h2.data.multicontainer");
		String dbName;
		if (!"SEQUENCES_VALUES".equals(container) && Boolean.parseBoolean(multicontainer)) {

			String firstCharacter = Character.toString(source.toCharArray()[ source.length()-1 ]).toUpperCase();
			firstCharacter = source == null || source.trim().isEmpty() ? StringUtils.EMPTY : firstCharacter;

			firstCharacter = !chars.contains(firstCharacter) ? "_MISC" : "_" + firstCharacter;

			dbName = container + firstCharacter;

		} else {
			dbName = container;			
		}

		try {
			if (!map.containsKey(dbName)) {
				LOGGER.info("Check db {}", dbName);
				String dataFolder = configuration.getProperty("h2.data.folder");

				Connection connection = openConnection("jdbc:h2:file:" + dataFolder + dbName, "sa", null, "org.h2.Driver");
				checkTable(connection);
				map.put(dbName, connection);
			}

			return map.get(dbName);
		} catch (Exception e) {
			throw new RuntimeException("Cannot open connection for container " + dbName, e);
		}
	}

	@Override
	public boolean contains(String container, String source) {
		// TODO Stub di metodo generato automaticamente
		return false;
	}

}
