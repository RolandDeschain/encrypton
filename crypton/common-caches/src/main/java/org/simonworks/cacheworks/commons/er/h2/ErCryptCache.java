package org.simonworks.cacheworks.commons.er.h2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import org.simonworks.cacheworks.api.CryptCache;
import org.simonworks.cacheworks.commons.er.AbstractErCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErCryptCache extends AbstractErCache implements CryptCache {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(ErCryptCache.class);

	Connection connection;

	@Override
	public String getName() {
		return "ercache";
	}

	protected Connection getConnection(String container,String source) {

		return connection;
	}

	@Override
	public void clear() {

		PreparedStatement ps = null;
		try {
			ps = getConnection(null,null).prepareStatement(QUERY_CLEAR);
			ps.executeUpdate();
		} catch (Exception e) {
			throw new IllegalStateException("Cannot executes queries", e);
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// DO NOT STOP THE BATCH
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void close() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new IllegalStateException("Cannot close connection", e);
			}
		}

	}

	@Override
	public void init() {
		try {
			
			Properties properties = getConfiguration();

			String url = properties.getProperty("jdbc.url");
			String user = properties.getProperty("jdbc.user");
			String password = properties.getProperty("jdbc.password");
			String classDriver = properties.getProperty("jdbc.driver");
			connection = openConnection(url,user,password,classDriver);
			checkTable(connection);
		} catch (Exception e) {
			throw new IllegalArgumentException("Cannot Init", e);
		}
	}

	@Override
	public boolean contains(String container, String source) {
		return false;
	}


}
