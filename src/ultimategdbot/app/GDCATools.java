package ultimategdbot.app;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

/**
 * Utility class useful for the GDCA implementation of the Discord API
 * 
 * @author Alex1304
 *
 */
public class GDCATools {

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
}
