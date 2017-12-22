package ultimategdbot.net.database.util;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Functional interface used to prepare SQL statements
 * 
 * @param <T>
 *            - The type of the result to build
 * 
 * @author Alex1304
 */
@FunctionalInterface
public interface ResultInstanceBuilder<T> {
	
	/**
	 * Builds a new instance of the entity of type T according to the database
	 * query result contained in the ResultSet.
	 * 
	 * @param rset
	 *            - The ResultSet representing the result of the previously
	 *            executed database query
	 * @return the new instance of the entity built according to the result
	 * @throws SQLException
	 *             this method should propagate the potential SQLExceptions
	 *             thrown while manipulating the ResultSet.
	 */
	T build(ResultSet rset) throws SQLException;
}
