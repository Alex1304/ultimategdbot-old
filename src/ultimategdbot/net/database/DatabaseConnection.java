package ultimategdbot.net.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import ultimategdbot.app.AppParams;
import ultimategdbot.app.Main;
import ultimategdbot.util.AppTools;

/**
 * This class is used to get an instance of the connection to the database using
 * the Singleton Pattern
 * 
 * @author Alex1304
 *
 */
public class DatabaseConnection {

	private static volatile Connection conn = createInstance();
	
	/**
	 * Returns the current connection instance. It first checks if the connection is still operational.
	 * If not, then the connection is reset before returning a new instance.
	 * @return the current Connection instance to database.
	 */
	public static synchronized Connection getInstance() {
		if (!isConnectionOK()) {
			conn = createInstance();
			AppTools.sendDebugPMToSuperadmin(":white_check_mark: A new database connection instance has been successfully created!");
		}

		return conn;
	}

	/**
	 * Returns a unique instance of the database connection. Credentials are
	 * read from the system environment variables for security reasons
	 * 
	 * @return the Connection instance created, or null if instance failed.
	 */
	private static Connection createInstance() {
		try {
			if (conn != null) 
				conn.close();
			conn = DriverManager.getConnection((Main.isTestEnvironment() ? AppParams.LOCAL_DB_HOST
					: AppParams.REMOTE_DB_HOST) + "?reconnect=true",
					System.getenv().get("DB_USERNAME"), System.getenv().get("DB_PASSWORD"));
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(2);
		}

		return null;
	}

	/**
	 * Attempts to make a query to the database using the current connection. If
	 * any exception is thrown during the process, false is returned.
	 * 
	 * @return false if the test query throws any exception or if the current
	 *         connection is null, true otherwise.
	 */
	public static boolean isConnectionOK() {
		if (conn == null)
			return false;
		
		try {
			return conn.isValid(10);
		} catch (SQLException e) {
			return false;
		}
	}
}
