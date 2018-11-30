package org.simonworks.cacheworks.commons.er;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.simonworks.cacheworks.api.AbstractCryptCache;
import org.simonworks.cacheworks.api.CryptCache;
import org.simonworks.cacheworks.commons.er.h2.ErCryptCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractErCache extends AbstractCryptCache implements CryptCache {
	
	private static final String CANNOT_EXEC_QUERY = "Cannot executes queries";

	private static final String CREATE_TABLE_QUERY = "CREATE TABLE CACHE_TABLE ( CONTAINER_COL VARCHAR(20) , SOURCE_COL VARCHAR(70) , CRYPTED_COL VARCHAR(70) , PRIMARY KEY (SOURCE_COL, CONTAINER_COL)  );";

	private static final String CREATION_OF_TABLE_CACHE_TABLE_MESSAGE = "Creation of table CACHE_TABLE";

	protected static final String INIT_QUERY = "SELECT TOP 1 * FROM CACHE_TABLE";

	private static final Logger LOGGER = LoggerFactory.getLogger(ErCryptCache.class);
	
	private static final String QUERY_CLEAR_CONTAINER = "DELETE FROM CACHE_TABLE  WHERE CONTAINER_COL = ? ";
	private static final String QUERY_RECOVER = "SELECT TOP 1 CRYPTED_COL FROM  CACHE_TABLE  WHERE SOURCE_COL= ? AND CONTAINER_COL = ?";
	private static final String QUERY_UPDATE = "UPDATE CACHE_TABLE SET CRYPTED_COL = ? WHERE SOURCE_COL= ? AND CONTAINER_COL = ?";
	private static final String QUERY_INSERT = "INSERT INTO CACHE_TABLE (CONTAINER_COL, SOURCE_COL, CRYPTED_COL) VALUES (?, ? ,?)";
	protected static final String QUERY_CLEAR = "DELETE FROM CACHE_TABLE ";
	
	protected abstract Connection getConnection(String container,String source);
	
	@Override
	public void store(String container, String source, String crypted) {
		
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		try {
			ps = getConnection(container,source).prepareStatement(QUERY_INSERT);
			ps.setString(1, container);
			ps.setString(2, source);
			ps.setString(3, crypted);
			ps.executeUpdate();			
		} catch (Exception e) {
			throw new IllegalStateException(CANNOT_EXEC_QUERY, e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					// DO NOT STO THE BATCH
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// DO NOT STO THE BATCH
					e.printStackTrace();
				}
			}
		}

	}

	

	@Override
	public void update(String container, String source, String crypted) {
		
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		try {
			ps = getConnection(container,source).prepareStatement(QUERY_UPDATE);
			ps.setString(1, crypted);			
			ps.setString(2, source);
			ps.setString(3, container);
			int update  = ps.executeUpdate();
			if (update == 0) {
				store(container, source, crypted);
			} else if (update > 1) {
				LOGGER.error("Update di piu' campi");
			}
		} catch (Exception e) {
			throw new IllegalStateException(CANNOT_EXEC_QUERY, e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					// DO NOT STO THE BATCH
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// DO NOT STO THE BATCH
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public String recover(String container, String source) {

		String result = null;
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		try {
			ps = getConnection(container,source).prepareStatement(QUERY_RECOVER);
			ps.setString(1, source);
			ps.setString(2, container);
			resultSet = ps.executeQuery();
			if (resultSet.next()) {
				return resultSet.getString("CRYPTED_COL");
			}
		} catch (Exception e) {
			throw new IllegalStateException(CANNOT_EXEC_QUERY, e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					// DO NOT STO THE BATCH
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// DO NOT STO THE BATCH
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	@Override
	public void clearContainer(String container) {

		PreparedStatement ps = null;
		try {
			ps = getConnection(container,null).prepareStatement(QUERY_CLEAR_CONTAINER);
			ps.setString(1, container);
			ps.executeUpdate();
		} catch (Exception e) {
			throw new IllegalStateException(CANNOT_EXEC_QUERY, e);
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


	protected Connection openConnection(String url, String user,String password, String classDriver) throws SQLException, ClassNotFoundException {
		Connection connection;
		Class.forName(classDriver);
		LOGGER.info("Opening Connection url" + url);
		connection = DriverManager.getConnection(url, user, password);
		connection.setAutoCommit(true);
		LOGGER.info("Connection is open");
		return connection;
	}

	


	private void createTable(Connection connection) throws SQLException {
		LOGGER.info(CREATION_OF_TABLE_CACHE_TABLE_MESSAGE);
		Statement insert = null;
		try {
			insert = connection.createStatement();
			insert.executeUpdate(CREATE_TABLE_QUERY);
		} catch (Exception exc) {
			throw new RuntimeException("Cannot create CACHE_TABLE", exc);
		} finally {
			if (insert != null) {
				insert.close();
			}
		}
	}


	protected void checkTable(Connection connection) throws SQLException {
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(INIT_QUERY);
			statement.executeQuery();
			LOGGER.info("CACHE_TABLE esistente");
		} catch (Exception e) {
			createTable(connection);
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
	}


}
