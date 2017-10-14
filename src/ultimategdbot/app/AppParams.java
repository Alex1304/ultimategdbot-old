package ultimategdbot.app;

/**
 * Parameters necessary for the bot to operate.
 * If you wish to fork this project, make sure to replace all of the values here by yours!
 * 
 * @author Alex1304
 *
 */
public class AppParams {
	// Bot info
	public static final long CLIENT_ID = 358598636436979713L;
	public static final long CLIENT_TEST_ID = 359406519227383818L;
	public static final String BOT_TOKEN = System.getenv().get("BOT_TOKEN");
	
	// Hierarchy info
	public static final long SUPERADMIN_ID = 272872694473687041L;
	public static final long BETA_TESTERS_GUILD_ID = 357655103768887297L;
	public static final long BETA_TESTERS_ROLE_ID = 368084326019235851L;
	
	// Database info
	public static final String DB_USERNAME = System.getenv().get("DB_USERNAME");
	public static final String DB_PASSWORD = System.getenv().get("DB_PASSWORD");
	public static final String LOCAL_DB_HOST = "jdbc:mysql://localhost/ultimategdbot";
	public static final String REMOTE_DB_HOST = "jdbc:mysql://mysql-alex1304.alwaysdata.net/alex1304_ultimategdbot";
	
	public static boolean checkEnvVariables() {
		return BOT_TOKEN != null && DB_USERNAME != null && DB_PASSWORD != null;
	}
}
