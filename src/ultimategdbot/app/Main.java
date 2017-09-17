package ultimategdbot.app;

import static ultimategdbot.app.AppTools.createClient;

import sx.blah.discord.api.IDiscordClient;
import ultimategdbot.commands.CommandHandler;

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

	/**
	 * Starts the program Creates the client, registers events and then logs in
	 * the client.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String botToken = System.getenv().get("BOT_TOKEN");
		
		try {
			superadminID = Long.parseLong(System.getenv().get("SUPERADMIN_ID"));
		} catch (NumberFormatException e) {
			throw new RuntimeException("The given superadmin ID is not valid.");
		}
		
		
		client = createClient(botToken, false);
		client.getDispatcher().registerListener(new CommandHandler());
		client.login();
	}
}
