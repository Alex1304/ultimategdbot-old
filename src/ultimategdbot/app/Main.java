package ultimategdbot.app;

import java.util.Base64;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import ultimategdbot.discordevents.DiscordEvents;
import ultimategdbot.gdevents.dispatcher.GDEventDispatcher;
import ultimategdbot.loops.LoopRequestNewAwardedLevels;
import ultimategdbot.loops.LoopRequestNewTimelyLevels;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.KillableThreadManager;
import ultimategdbot.util.robtopsweakcrypto.RobTopsWeakCrypto;

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
	
	private static AppParams params;

	public static void main(String[] args) {
		try {
			params = checkArgs(args);
			launch();
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public static AppParams checkArgs(String[] args) throws IllegalArgumentException {
		IllegalArgumentException excpt = new IllegalArgumentException("The arguments provided are invalid."
				+ " You need to provide the following arguments in this order, separated by spaces:\n"
				+ "-> Discord Client ID\n"
				+ "-> Discord Bot Token\n"
				+ "-> Bot owner's Discord account ID\n"
				+ "-> The official dev Discord server's ID\n"
				+ "-> The ID of the Beta-testers role in the dev server\n"
				+ "-> The ID of the moderators role in the dev server\n"
				+ "-> The database hostname or IP\n"
				+ "-> The database username\n"
				+ "-> The database password\n"
				+ "-> The bot's Geometry Dash account ID\n"
				+ "-> The bot's Geometry Dash account password\n");
		
		if (args.length < 11)
			throw excpt;
		
		try {
			return new AppParams(Long.parseLong(args[0]),
					args[1],
					Long.parseLong(args[2]),
					Long.parseLong(args[3]),
					Long.parseLong(args[4]),
					Long.parseLong(args[5]),
					args[6],
					args[7],
					args[8],
					Long.parseLong(args[9]),
					Base64.getUrlEncoder().encodeToString(RobTopsWeakCrypto.getAccountGJPXORCipher().cipher(args[10]).getBytes())
			);
		} catch (NumberFormatException e) {
			throw excpt;
		}
	}

	/**
	 * Starts the program. Creates the client, registers events and then logs in
	 * the client.
	 * 
	 * @param test - Tells whether the bot should be running in test environment
	 */
	public static void launch() {
		// Building client
		DISCORD_ENV.client = AppTools.createClient(params.getBotToken(), false);
		
		// Registering Discord events
		DISCORD_ENV.client.getDispatcher().registerListener(new DiscordEvents());
		
		// Let's start!
		DISCORD_ENV.client.login();
		
		// The program will continue when the Ready event will be dispatched.
		System.out.println("Waiting for all guilds to be available, this can take a while...");
	}
	
	/**
	 * Loads and starts the main threads of the bot
	 */
	public static void registerThreads() {
		THREADS.addThread("loop_newawarded", new LoopRequestNewAwardedLevels());
		THREADS.addThread("loop_timely", new LoopRequestNewTimelyLevels());
		
		THREADS.startAllNew();
	}
	
	/**
	 * Tells whether the client is ready to use
	 * 
	 * @return boolean
	 */
	public static boolean isReady() {
		return DISCORD_ENV.client != null && DISCORD_ENV.client.isReady();
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
		
		public boolean fetchSuperadmin() {
			superadmin = client.fetchUser(params.getSuperadminID());
			System.out.println("Superadmin: " + (superadmin != null ? AppTools.formatDiscordUsername(superadmin) : null));
			return superadmin != null;
		}
		
		/**
		 * Initializes the Discord environment using the values provided in AppParams.
		 * @return true if all fields are successfully initialized, false otherwise.
		 */
		public boolean init() {
			int attempt = 1;
			boolean res = false;
			
			while (attempt <= 10 && !(res = init0())) {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
				}
				attempt++;
			}
			
			return res;
		}
		
		private boolean init0() {
			officialDevGuild = client.getGuildByID(params.getOfficialDevGuildID());
			System.out.println("Official dev guild: " + (officialDevGuild != null ? officialDevGuild.getName() : null));
			
			if (officialDevGuild == null)
				return false;
			
			betaTestersRole = officialDevGuild.getRoleByID(params.getBetaTestersRoleID());
			moderatorsRole = officialDevGuild.getRoleByID(params.getModeratorsRoleID());

			System.out.println("Beta-testers role: " + (betaTestersRole != null ? betaTestersRole.getName() : null));
			System.out.println("Moderators role: " + (moderatorsRole != null ? moderatorsRole.getName() : null));
			
			if (superadmin == null || betaTestersRole == null || moderatorsRole == null) {
				return false;
			}
			
			return true;
		}
	}

	/**
	 * @return the app params
	 */
	public static AppParams getParams() {
		return params;
	}
}
