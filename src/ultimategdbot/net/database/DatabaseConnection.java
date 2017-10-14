package ultimategdbot.net.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Date;

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

	public static final long DB_CONNECTION_TIMEOUT = 44_000_000;

	private static volatile Connection conn = createInstance();
	private static long dateLastInstance;
	
	/**
	 * Returns the current connection instance. It first checks if the connection is still operational.
	 * If not, then the connection is reset before returning a new instance.
	 * @return the current Connection instance to database.
	 */
	public static synchronized Connection getInstance() {
		long currDate = new Date().getTime();

		if (currDate - dateLastInstance >= DB_CONNECTION_TIMEOUT || !isConnectionOK())
			conn = createInstance();

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
			if (conn != null) {
				try {
					conn.abort(r -> r.run());
				} catch (Exception e) {
					conn.close();
					AppTools.sendDebugPMToSuperadmin(
							"The operation `conn.abort(r -> r.run())` didn't work: " + e.getLocalizedMessage());
				}
			}
			conn = DriverManager.getConnection(Main.isTestEnvironment() ? AppParams.LOCAL_DB_HOST : AppParams.REMOTE_DB_HOST,
					System.getenv().get("DB_USERNAME"), System.getenv().get("DB_PASSWORD"));
			dateLastInstance = new Date().getTime();
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
			AppTools.sendDebugPMToSuperadmin(
					"Failed to create a connection instance to the database: " + "`" + e.getLocalizedMessage() + "`");
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
			conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
					.executeQuery("SELECT * FROM guild_settings WHERE guild_id = -1");
		} catch (Exception e) {
			AppTools.sendDebugPMToSuperadmin("Ping database failed: `" + e.getLocalizedMessage() + "`");
			return false;
		}

		return true;
	}
}
