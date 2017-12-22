package ultimategdbot.net.database.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Functional interface used to prepare SQL statements
 * 
 * @author Alex1304
 */
@FunctionalInterface
public interface StatementPreparator {
	
	/**
	 * This method lets the programmer manipulate freely the PreparedStatement
	 * object, in order to bind parameters for example. It is not recommended to
	 * execute the statement here though, as this method is not made to return a
	 * result.
	 * 
	 * @param statement
	 *            - the statement to prepare
	 * @throws SQLException
	 *             this method should propagate the potential SQLExceptions
	 *             thrown while preparing the statement.
	 */
	void prepare(PreparedStatement statement) throws SQLException;
}
