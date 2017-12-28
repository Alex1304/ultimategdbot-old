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
	public static final String BOT_TOKEN = System.getenv().get(Main.isTestEnvironment() ? "TEST_BOT_TOKEN" : "BOT_TOKEN");
	
	// Hierarchy info
	public static final long SUPERADMIN_ID = 272872694473687041L;
	public static final long OFFICIAL_DEV_GUILD_ID = 357655103768887297L;
	public static final long BETA_TESTERS_ROLE_ID = 368084326019235851L;
	public static final long MODERATORS_ROLE_ID = 368106954289184769L;
	
	// Database info
	public static final String DB_USERNAME = System.getenv().get("DB_USERNAME");
	public static final String DB_PASSWORD = System.getenv().get("DB_PASSWORD");
	public static final String LOCAL_DB_HOST = "jdbc:mysql://localhost/ultimategdbot";
	public static final String REMOTE_DB_HOST = "jdbc:mysql://us-cdbr-iron-east-05.cleardb.net/heroku_1f8c3734368a235";
	
	// Geometry Dash bot account info
	public static final long GD_ACCOUNT_ID = 7753855L;
	public static final String GD_ACCOUNT_GJP = System.getenv().get("GD_ACCOUNT_GJP");
	
	public static boolean checkEnvVariables() {
		return BOT_TOKEN != null && DB_USERNAME != null && DB_PASSWORD != null && GD_ACCOUNT_GJP != null;
	}
}
