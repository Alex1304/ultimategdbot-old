package ultimategdbot.app;

/**
 * Parameters necessary for the bot to operate.
 * 
 * @author Alex1304
 *
 */
public class AppParams {
	
	// Bot info
	private long clientID;
	private String botToken;
	
	// Hierarchy/Ownership info
	private long superadminID;
	private long officialDevGuildID;
	private long betaTestersRoleID;
	private long moderatorsRoleID;
	
	// Database info
	private String dbHost;
	private String dbUsername;
	private String dbPassword;
	
	// Bot's Geometry Dash account info
	private long gdAccountID;
	private String gdAccountPassword;
	
	/**
	 * Constructs an AppParams instance with all of the following fields provided:
	 * 
	 * @param clientID
	 * @param botToken
	 * @param superadminID
	 * @param officialDevGuildID
	 * @param betaTestersRoleID
	 * @param moderatorsRoleID
	 * @param dbHost
	 * @param dbUsername
	 * @param dbPassword
	 * @param gdAccountID
	 * @param gdAccountPassword
	 */
	public AppParams(long clientID, String botToken, long superadminID, long officialDevGuildID, long betaTestersRoleID,
			long moderatorsRoleID, String dbHost, String dbUsername, String dbPassword, long gdAccountID,
			String gdAccountPassword) {
		this.clientID = clientID;
		this.botToken = botToken;
		this.superadminID = superadminID;
		this.officialDevGuildID = officialDevGuildID;
		this.betaTestersRoleID = betaTestersRoleID;
		this.moderatorsRoleID = moderatorsRoleID;
		this.dbHost = dbHost;
		this.dbUsername = dbUsername;
		this.dbPassword = dbPassword;
		this.gdAccountID = gdAccountID;
		this.gdAccountPassword = gdAccountPassword;
	}

	/**
	 * @return the clientID
	 */
	public long getClientID() {
		return clientID;
	}

	/**
	 * @return the botToken
	 */
	public String getBotToken() {
		return botToken;
	}

	/**
	 * @return the superadminID
	 */
	public long getSuperadminID() {
		return superadminID;
	}

	/**
	 * @return the officialDevGuildID
	 */
	public long getOfficialDevGuildID() {
		return officialDevGuildID;
	}

	/**
	 * @return the betaTestersRoleID
	 */
	public long getBetaTestersRoleID() {
		return betaTestersRoleID;
	}

	/**
	 * @return the moderatorsRoleID
	 */
	public long getModeratorsRoleID() {
		return moderatorsRoleID;
	}

	/**
	 * @return the dbHost
	 */
	public String getDbHost() {
		return dbHost;
	}

	/**
	 * @return the dbUsername
	 */
	public String getDbUsername() {
		return dbUsername;
	}

	/**
	 * @return the dbPassword
	 */
	public String getDbPassword() {
		return dbPassword;
	}

	/**
	 * @return the gdAccountID
	 */
	public long getGdAccountID() {
		return gdAccountID;
	}

	/**
	 * @return the gdAccountPassword
	 */
	public String getGdAccountPassword() {
		return gdAccountPassword;
	}
	
	
}
