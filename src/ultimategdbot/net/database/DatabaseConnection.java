package ultimategdbot.net.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

/**
 * This class is used to get an instance of the connection to the database using
 * the Singleton Pattern
 * 
 * @author Alex1304
 *
 */
public class DatabaseConnection {

	private static final String LOCAL_DB_HOST = "jdbc:mysql://localhost/ultimategdbot";
	private static final String REMOTE_DB_HOST = "jdbc:mysql://mysql-alex1304.alwaysdata.net/alex1304_ultimategdbot";

	private static Connection conn = createInstance();
	private static long dateLastInstance;

	public static Connection getInstance() {
		long currDate = new Date().getTime();
		boolean closed;
		
		try {
			closed = conn.isClosed();
		} catch (SQLException e) {
			closed = true;
		}

		if (closed || currDate - dateLastInstance >= 44_000_000)
			conn = createInstance();

		return conn;
	}

	/**
	 * Returns a unique instance of the database connection. Credentials are
	 * read from the system environment variables for security reasons
	 * 
	 * @return
	 */
	private static Connection createInstance() {
		try {
			if (conn != null)
				conn.close();
			conn = DriverManager.getConnection(
					System.getenv().containsKey("UGDB_TEST_ENV") ? LOCAL_DB_HOST : REMOTE_DB_HOST,
					System.getenv().get("DB_USERNAME"), System.getenv().get("DB_PASSWORD"));
			dateLastInstance = new Date().getTime();
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
