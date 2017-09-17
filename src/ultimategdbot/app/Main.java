package ultimategdbot.app;

import static ultimategdbot.app.GDCATools.createClient;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
	public static String CMD_PREFIX = "g!";

	/**
	 * Discord client (represents the bot itself)
	 */
	public static IDiscordClient client;

	/**
	 * Starts the program Creates the client, registers events and then logs in
	 * the client.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String botToken = readBotTokenFile();
		if (botToken == null)
			throw new RuntimeException();
		
		client = createClient(botToken, false);
		client.getDispatcher().registerListener(new CommandHandler());
		client.login();
	}

	/**
	 * Reads the bot token from the bot-token.txt file
	 * 
	 * @return a String containing the bot token
	 */
	public static String readBotTokenFile() {
		FileReader fr = null;
		String str = "";

		try {
			fr = new FileReader("bot-token.txt");
			int i = 0;
			while ((i = fr.read()) != -1)
				str += (char) i;
			fr.close();
		} catch (FileNotFoundException e) {
			System.err.println(
					"ERROR: Unable to find the file \"bot-token.txt\" which contains the token required for the bot authentication.");
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return str;
	}
}
