package ultimategdbot.net.database.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import ultimategdbot.net.database.DatabaseConnection;

/**
 * Provides a default method to execute a SQL statement meant to perform
 * modifications on a relational database.
 * 
 * @author Alex1304
 *
 */
public interface SQLUpdateExecutor {
	
	/**
	 * Executes a SQL query that is supposed to perform modifications on the database
	 * (e.g INSERT, UPDATE, DELETE).
	 * 
	 * @param query - the DML statement to execute
	 * @param preparator - preparator for the query to execute
	 * @return the number of rows modified, or -1 if the query failed.
	 */
	default int executeUpdate(String query, StatementPreparator preparator) {
		try {
			PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(query);
			preparator.prepare(ps);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
}
