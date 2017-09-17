package ultimategdbot.app;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

/**
 * Utility class useful for the GDCA implementation of the Discord API
 * 
 * @author Alex1304
 *
 */
public class AppTools {

	/**
	 * Builds the client using the ClientBuilder object
	 * 
	 * @param token
	 *            - the authentication token of the bot
	 * @param login
	 *            - if true, automatically logs the bot in after creation
	 * @return null if the client creation failed, or returns an instance of
	 *         IDiscordClient if created successfully
	 */
	public static IDiscordClient createClient(String token, boolean login) {
		ClientBuilder clientBuilder = new ClientBuilder(); // Creates the
															// ClientBuilder
															// instance
		clientBuilder.withToken(token); // Adds the login info to the builder
		try {
			if (login) {
				return clientBuilder.login();
			} else {
				return clientBuilder.build();
			}
		} catch (DiscordException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Reads the text contained in a file and puts it into a String 
	 * 
	 * @return the String containing the file text
	 */
	public static String readFile(String filepath) {
		FileReader fr = null;
		String str = "";

		try {
			fr = new FileReader(filepath);
			int i = 0;
			while ((i = fr.read()) != -1)
				str += (char) i;
			fr.close();
		} catch (FileNotFoundException e) {
			System.err.println(
					"ERROR: Unable to find the file \"" + filepath + "\"");
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return str;
	}
}
