package ultimategdbot.app;

import static ultimategdbot.util.AppTools.createClient;

import java.util.ArrayList;
import java.util.List;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IUser;
import ultimategdbot.commands.DiscordCommandHandler;
import ultimategdbot.discordevents.DiscordEvents;
import ultimategdbot.events.observable.impl.LoopRequestNewAwardedLevels;

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
	public static final String CMD_PREFIX = "g!";

	/**
	 * Discord client (represents the bot itself)
	 */
	public static IDiscordClient client;
	
	public static long superadminID;
	
	public static IUser superadmin;
	
	private static List<Thread> threadList = new ArrayList<>();

	/**
	 * Starts the program Creates the client, registers events and then logs in
	 * the client.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 2)
			throw new RuntimeException("You must specify the bot token and the superadmin userID!");
		
		String botToken = args[0];
		
		try {
			superadminID = Long.parseLong(args[1]);
		} catch (NumberFormatException e) {
			throw new RuntimeException("The given superadmin ID is not valid.");
		}
		
		// Building client
		client = createClient(botToken, false);
		
		
		startThreads();
		
		// Registering events
		client.getDispatcher().registerListener(new DiscordCommandHandler());
		client.getDispatcher().registerListener(new DiscordEvents());
		
		// Let's start!
		client.login();
	}
	
	private static void startThreads() {
		// Registering threads
		threadList.add(new Thread(new LoopRequestNewAwardedLevels()));
		threadList.add(new Thread(new Runnable() { // Fetches the IUser instance for superadminID when client is ready
			@Override
			public void run() {
				while (!client.isReady()) {
					try {
						Thread.sleep(800);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				superadmin = client.fetchUser(superadminID);
				if (superadmin == null)
					throw new RuntimeException("The superadmin user with ID " + superadminID + " could not be found.");
				System.out.println("Superadmin user succesfully fetched!");
			}
		}));
		
		// Start all
		for (Thread t : threadList)
			t.start();
	}
}
