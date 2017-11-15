package ultimategdbot.app;

import java.util.ArrayList;
import java.util.List;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import ultimategdbot.commands.DiscordCommandHandler;
import ultimategdbot.discordevents.DiscordEvents;
import ultimategdbot.events.observable.impl.LoopRequestNewAwardedLevels;
import ultimategdbot.util.AppTools;

/**
 * Main class of the program Contains everything required for the bot to work
 * 
 * @author Alex1304
 *
 */
public class Main {

	/**
	 * Prefix for all bot commands
	 */
	public static final String CMD_PREFIX = "u!";
	
	private static boolean testEnv = false;
	
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
	
	public static final DiscordEnvironment DISCORD_ENV = new DiscordEnvironment();
	public static final String BETA_TESTERS_GUILD_INVITE_LINK = "https://discord.gg/VpVdKvg";
	
	private static List<Thread> threadList = new ArrayList<>();

	public static void main(String[] args) {
		launch(false);
	}

	/**
	 * Starts the program Creates the client, registers events and then logs in
	 * the client.
	 * 
	 * @param test - Tells whether you are running in test environment
	 */
	public static void launch(boolean test) {
		if (!AppParams.checkEnvVariables()) {
			System.err.println("You need to define all of the following system environnement variables for this "
					+ "program to work:\n"
					+ "BOT_TOKEN - Authentication token of the bot's Discord account\n"
					+ "DB_USERNAME and DB_PASSWORD - Credentials to connect to database.\n"
					+ "GD_ACCOUNT_GJP - The GJP of the Geometry Dash bot account.");
			return;
		}
		testEnv = test;
		System.out.println("Test environment? " + isTestEnvironment());
		// Building client
		DISCORD_ENV.client = AppTools.createClient(AppParams.BOT_TOKEN, false);
		
		startThreads();
		
		// Registering events
		DISCORD_ENV.client.getDispatcher().registerListener(new DiscordCommandHandler());
		DISCORD_ENV.client.getDispatcher().registerListener(new DiscordEvents());
		
		// Let's start!
		DISCORD_ENV.client.login();
	}
	
	private static void startThreads() {
		// Registering threads that are ONLY supposed to run in test environment
		if (isTestEnvironment()) {
			
		}
		
		// Registering other threads
		threadList.add(new Thread(new LoopRequestNewAwardedLevels()));
		threadList.add(new Thread(() -> {
			while (DISCORD_ENV.client == null || !DISCORD_ENV.client.isReady()) {}
			
			if (!DISCORD_ENV.init()) {
				System.err.println("Unable to load users and roles necessary for the bot to work. "
						+ "Please make sure you have provided the correct hierarchy info in AppParams.java");
			} else
				System.out.println("Hierarchy info successfully fetched!");
				
		}));
		
		// Start all
		for (Thread t : threadList)
			t.start();
	}
	
	public static boolean isTestEnvironment() {
		return testEnv;
	}
}
