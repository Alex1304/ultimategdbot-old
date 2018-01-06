package ultimategdbot.net.database.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ultimategdbot.net.database.DatabaseConnection;

/**
 * Provides default methods to execute a SQL query that is supposed to return a set of results.
 * 
 * @param <T> - the type of the entities built according to the query results.
 * 
 * @author Alrex1304
 *
 */
public interface SQLQueryExecutor<T> {

	/**
	 * Executes a SQL query that is supposed to return a ResultSet (e.g SELECT)
	 * with a prepared statement.
	 * 
	 * @param query
	 *            - the SELECT query to execute
	 * @param preparator
	 *            - preparator for the query to execute
	 * @param resultInstanceBuilder
	 *            - function that builds and returns one instance of the entity
	 *            according to the given result.
	 * @return a List containing the entity instances of all results, or null if
	 *         the query failed. If the query returned an empty result, it will
	 *         return an empty list.
	 */
	default List<T> executeQuery(String query, StatementPreparator preparator,
			ResultInstanceBuilder<T> resultInstanceBuilder) {
		List<T> results = new ArrayList<>();
		
		try {
			PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(query);
			preparator.prepare(ps);
			ResultSet rset = ps.executeQuery();
			
			while (rset.next()) {
				T resultInstance = resultInstanceBuilder.build(rset);
				if (resultInstance != null)
					results.add(resultInstance);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return results;
	}
	
	/**
	 * Executes a SQL query that is supposed to return a ResultSet (e.g SELECT)
	 * and that doesn't require a prepared statement.
	 * 
	 * @param query
	 *            - the SELECT query to execute
	 * @param resultInstanceBuilder
	 *            - function that builds and returns one instance of the entity
	 *            according to the given result.
	 * @return a List containing the entity instances of all results, or null if
	 *         the query failed. If the query returned an empty result, it will
	 *         return an empty list.
	 */
	default List<T> executeQuery(String query, ResultInstanceBuilder<T> resultInstanceBuilder) {
		return executeQuery(query, ps -> { /* NO-OP */ }, resultInstanceBuilder);
	}
}
