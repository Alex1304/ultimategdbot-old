package ultimategdbot.net.database.dao.impl;

/**
 * Provides unique instances of DAO implementations
 * 
 * @author Alex1304
 *
 */
public abstract class DAOFactory {
	
	// Unique instances
	private static final AwardedLevelDAO AWARDED_LEVEL_DAO = new AwardedLevelDAO();
	private static final GDModlistDAO GD_MOD_LIST_DAO = new GDModlistDAO();
	private static final GuildSettingsDAO GUILD_SETTINGS_DAO = new GuildSettingsDAO();
	private static final UserSettingsDAO USER_SETTINGS_DAO = new UserSettingsDAO();
	private static final TimelyLevelDAO TIMELY_LEVEL_DAO = new TimelyLevelDAO();
	private static final StatGrindEventDAO STAT_GRIND_EVENT_DAO = new StatGrindEventDAO();
	private static final JoinSGEDAO JOIN_SGE_DAO = new JoinSGEDAO();
	
	/**
	 * Gets the DAO instance for awarded levels
	 * 
	 * @return a DAO instance
	 */
	public static AwardedLevelDAO getAwardedLevelDAO() {
		return AWARDED_LEVEL_DAO;
	}
	
	/**
	 * Gets the DAO instance for GD moderator list
	 * 
	 * @return a DAO instance
	 */
	public static GDModlistDAO getGDModlistDAO() {
		return GD_MOD_LIST_DAO;
	}
	
	/**
	 * Gets the DAO instance for guild settings
	 * 
	 * @return a DAO instance
	 */
	public static GuildSettingsDAO getGuildSettingsDAO() {
		return GUILD_SETTINGS_DAO;
	}
	
	/**
	 * Gets the DAO instance for user settings
	 * 
	 * @return a DAO instance
	 */
	public static UserSettingsDAO getUserSettingsDAO() {
		return USER_SETTINGS_DAO;
	}
	
	/**
	 * Gets the DAO instance for timely levels
	 * 
	 * @return a DAO instance
	 */
	public static TimelyLevelDAO getTimelyLevelDAO() {
		return TIMELY_LEVEL_DAO;
	}
	
	/**
	 * Gets the DAO instance for stat grind events
	 * 
	 * @return a DAO instance
	 */
	public static StatGrindEventDAO getStatGrindEventDAO() {
		return STAT_GRIND_EVENT_DAO;
	}
	
	/**
	 * Gets the DAO instance for users joining stat grind events
	 * 
	 * @return a DAO instance
	 */
	public static JoinSGEDAO getJoinSGEDAO() {
		return JOIN_SGE_DAO;
	}
}
