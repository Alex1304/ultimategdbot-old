package ultimategdbot.net.database.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ultimategdbot.net.database.DatabaseConnection;
import ultimategdbot.util.AppTools;

/**
 * Provides default methods to count SQL query results.
 * 
 * @author Alex1304
 *
 */
public interface SQLCountQueryExecutor {
	
	/**
	 * Executes a SQL query that counts rows in a table (e.g via SELECT COUNT(*)...)
	 * Allows to specify a filter condition and uses a prepared statement.
	 * 
	 * @param table - the name of the table to count rows in
	 * @param whereCondition - the "WHERE" clause of the statement (without the WHERE keyword)
	 * @param preparator - preparator for the prepared statement
	 * @return the count result
	 */
	default int executeCountQuery(String table, String whereCondition, StatementPreparator preparator) {
		try {
			String statement = "SELECT COUNT(*) FROM " + table;
			if (whereCondition != null)
				statement += "WHERE " + whereCondition;
			
			PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(statement);
			preparator.prepare(ps);
			ResultSet rset = ps.executeQuery();
			return rset.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
			AppTools.sendDebugPMToSuperadmin("Exception related to database thrown: `"
					+ e.getLocalizedMessage() + "`");
			return -1;
		}
	}
	
	/**
	 * Executes a SQL query that counts rows in a table (e.g via SELECT COUNT(*)...)
	 * Allows to specify a filter condition.
	 * 
	 * @param table - the name of the table to count rows in
	 * @param whereCondition - the "WHERE" clause of the statement (without the WHERE keyword)
	 * @return the count result
	 */
	default int executeCountQuery(String table, String whereCondition) {
		return executeCountQuery(table, whereCondition, ps -> { /* NO-OP */ });
	}
	
	/**
	 * Executes a SQL query that counts rows in a table (e.g via SELECT COUNT(*)...)
	 * 
	 * @param table - the name of the table to count rows in
	 * @return the count result
	 */
	default int executeCountQuery(String table) {
		return executeCountQuery(table, null, ps -> { /* NO-OP */ });
	}

}
