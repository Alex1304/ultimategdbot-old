package ultimategdbot.app;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.RequestBuffer;
import ultimategdbot.commands.DiscordCommandHandler;
import ultimategdbot.discordevents.DiscordEvents;
import ultimategdbot.gdevents.dispatcher.GDEventDispatcher;
import ultimategdbot.gdevents.listeners.AwardedLevelListeners;
import ultimategdbot.gdevents.listeners.GDModeratorsListeners;
import ultimategdbot.gdevents.listeners.TimelyLevelListeners;
import ultimategdbot.loops.LoopRequestNewAwardedLevels;
import ultimategdbot.loops.LoopRequestNewTimelyLevels;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.KillableThreadManager;

/**
 * Main class of the program. Contains everything required for the bot to work
 * 
 * @author Alex1304
 *
 */
public class Main {

	/**
	 * Prefix for all bot commands
	 */
	public static final String CMD_PREFIX = "u!";
	
	/**
	 * Whether the bot is running in a test environment
	 */
	private static boolean testEnv = true;
	
	/**
	 * Will permanently switch to true once the client is fully initlalized
	 */
	private static boolean clientInitialized = false;
	
	/**
	 * Object containing Discord environment info (client, main guild, etc)
	 */
	public static final DiscordEnvironment DISCORD_ENV = new DiscordEnvironment();
	
	/**
	 * Customized thread management to have control over all threads running in the program.
	 */
	public static final KillableThreadManager THREADS = new KillableThreadManager();
	
	/**
	 * Dispatcher for Geometry Dash specific events.
	 */
	public static final GDEventDispatcher GD_EVENT_DISPATCHER = new GDEventDispatcher();
	
	/**
	 * Permanent Discord invite link to the official bot development server.
	 */
	public static final String BETA_TESTERS_GUILD_INVITE_LINK = "https://discord.gg/VpVdKvg";

	public static void main(String[] args) {
		launch(false);
	}

	/**
	 * Starts the program. Creates the client, registers events and then logs in
	 * the client.
	 * 
	 * @param test - Tells whether the bot should be running in test environment
	 */
	public static void launch(boolean test) {
		testEnv = test;
		System.out.println("Test environment? " + isTestEnvironment());
		
		if (!AppParams.checkEnvVariables()) {
			System.err.println("You need to define all of the following system environnement variables for this "
					+ "program to work:\n"
					+ "BOT_TOKEN - Authentication token of the bot's Discord account\n"
					+ "DB_USERNAME and DB_PASSWORD - Credentials to connect to database.\n"
					+ "GD_ACCOUNT_GJP - The GJP of the Geometry Dash bot account.");
			System.err.println("Defined environment variables: " + System.getenv().keySet());
			return;
		}
		// Building client
		DISCORD_ENV.client = AppTools.createClient(AppParams.BOT_TOKEN, false);
		
		registerThreads();
		
		// Registering Discord events
		DISCORD_ENV.client.getDispatcher().registerListener(new DiscordCommandHandler());
		DISCORD_ENV.client.getDispatcher().registerListener(new DiscordEvents());
		
		// Registering Geometry Dash events
		GD_EVENT_DISPATCHER.addAllListeners(AwardedLevelListeners.getListeners());
		GD_EVENT_DISPATCHER.addAllListeners(GDModeratorsListeners.getListeners());
		GD_EVENT_DISPATCHER.addAllListeners(TimelyLevelListeners.getListeners());
		
		// Let's start!
		DISCORD_ENV.client.login();
	}
	
	/**
	 * Loads and starts the main threads of the bot
	 */
	private static void registerThreads() {
		// Registering threads that are ONLY supposed to run in test environment
		if (isTestEnvironment()) {
			
		}
		
		// Registering other threads
		THREADS.addThread("loop_newawarded", new LoopRequestNewAwardedLevels());
		THREADS.addThread("loop_timely", new LoopRequestNewTimelyLevels());
		THREADS.addThread("fetch_hierarchy_info", (thread) -> {
			while (DISCORD_ENV.client == null || !DISCORD_ENV.client.isReady()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
			
			int attempt = 1;
			while (attempt <= 5 && !DISCORD_ENV.init()) {
				System.out.println("Failed attempt " + attempt + " to fetch hierarchy info");
				attempt++;
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			if (attempt >= 5) {
				System.err.println("Unable to load users and roles necessary for the bot to work. "
						+ "Please make sure you have provided the correct hierarchy info in AppParams.java");
				System.exit(1);
			}
			
			System.out.println("Hierarchy info successfully fetched!");
			RequestBuffer.request(() -> 
				DISCORD_ENV.client.changePlayingText("Geometry Dash | " + CMD_PREFIX + "help")
			);
			clientInitialized = true;
		});
		
		THREADS.startAllNew();
	}
	
	/**
	 * Tells whether the bot is running in test environment
	 * 
	 * @return boolean
	 */
	public static boolean isTestEnvironment() {
		return testEnv;
	}
	
	/**
	 * Tells whether the client is ready to use
	 * 
	 * @return boolean
	 */
	public static boolean isReady() {
		return DISCORD_ENV.client != null && DISCORD_ENV.client.isReady() && clientInitialized;
	}
	
	public static class DiscordEnvironment {
		private IDiscordClient client;
		private IUser superadmin;
		private IGuild officialDevGuild;
		private IRole betaTestersRole;
		private IRole moderatorsRole;
		
		public IDiscordClient getClient() {
			return client;
		}
		
		public IUser getSuperadmin() {
			return superadmin;
		}
		
		public IGuild getOfficialDevGuild() {
			return officialDevGuild;
		}
		
		public IRole getBetaTestersRole() {
			return betaTestersRole;
		}
		
		public IRole getModeratorsRole() {
			return moderatorsRole;
		}
		
		/**
		 * Initializes the Discord environment using the values provided in AppParams.
		 * @return true if all fields are successfully initialized, false otherwise.
		 */
		public boolean init() {
			if (client == null || !client.isReady())
				return false;
			
			superadmin = client.fetchUser(AppParams.SUPERADMIN_ID);
			officialDevGuild = client.getGuildByID(AppParams.OFFICIAL_DEV_GUILD_ID);
			
			if (officialDevGuild == null)
				return false;
			
			betaTestersRole = officialDevGuild.getRoleByID(AppParams.BETA_TESTERS_ROLE_ID);
			moderatorsRole = officialDevGuild.getRoleByID(AppParams.MODERATORS_ROLE_ID);
			
			if (superadmin == null || betaTestersRole == null || moderatorsRole == null) {
				return false;
			}
			
			return true;
		}
	}
}
