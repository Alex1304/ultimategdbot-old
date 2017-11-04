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

	/**
	 * Discord client (represents the bot itself)
	 */
	public static IDiscordClient client;
	
	// Superadmin and beta testers info
	public static IUser superadmin;
	public static IGuild betaTestersGuild;
	public static IRole betaTestersRole;
	public static final String BETA_TESTERS_GUILD_INVITE_LINK = "https://discord.gg/VpVdKvg";
	
	private static List<Thread> threadList = new ArrayList<>();

	/**
	 * Starts the program Creates the client, registers events and then logs in
	 * the client.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (!AppParams.checkEnvVariables()) {
			System.err.println("You need to define all of the following system environnement variables for this "
					+ "program to work:\n"
					+ "BOT_TOKEN - Authentication token of the bot's Discord account\n"
					+ "DB_USERNAME and DB_PASSWORD - Credentials to connect to database.\n"
					+ "GD_ACCOUNT_GJP - The GJP of the Geometry Dash bot account.");
			return;
		}
		System.out.println("Test environment? " + isTestEnvironment());
		// Building client
		client = AppTools.createClient(AppParams.BOT_TOKEN, false);
		
		startThreads();
		
		// Registering events
		client.getDispatcher().registerListener(new DiscordCommandHandler());
		client.getDispatcher().registerListener(new DiscordEvents());
		
		// Let's start!
		client.login();
	}
	
	private static void startThreads() {
		// Registering threads that are ONLY supposed to run in test environment
		if (isTestEnvironment()) {
			// Nothing here yet
		}
		
		// Registering other threads
		threadList.add(new Thread(new LoopRequestNewAwardedLevels()));
		threadList.add(new Thread(new Runnable() { // Fetches the IUser instance for superadminID when client is ready
			@Override
			public void run() {
				while (!client.isReady()) {} // Waits for the client
				
				superadmin = client.fetchUser(AppParams.SUPERADMIN_ID);
				betaTestersGuild = client.getGuildByID(AppParams.BETA_TESTERS_GUILD_ID);
				betaTestersRole = betaTestersGuild != null ?
						betaTestersGuild.getRoleByID(AppParams.BETA_TESTERS_ROLE_ID) : null;
				if (superadmin == null || betaTestersRole == null)
					throw new RuntimeException("Failed to fetch superadmin and beta testers info");
				System.out.println("Superadmin and beta testers info succesfully fetched!");
			}
		}));
		
		// Start all
		for (Thread t : threadList)
			t.start();
	}
	
	public static boolean isTestEnvironment() {
		return System.getenv().containsKey("UGDB_TEST_ENV");
	}
}
