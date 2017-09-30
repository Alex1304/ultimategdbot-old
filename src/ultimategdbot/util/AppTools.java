package ultimategdbot.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;
import ultimategdbot.app.Main;

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
			System.err.println("ERROR: Unable to find the file \"" + filepath + "\"");
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return str;
	}

	/**
	 * Sends a message in Discord to the specified channel using a RequestBuffer
	 * 
	 * @param channel
	 * @param message
	 */
	public static void sendMessage(IChannel channel, String message) {
		RequestBuffer.request(() -> {
			channel.sendMessage(message);
		});
	}

	/**
	 * Finds the most appropriate channel in the given guild to send messages
	 * that aren't triggered by a command. To determine which channel will be
	 * retained, it will do the following:
	 * <ul>
	 * <li>First, it will look for a channel named "bot-commands" or
	 * "bot_commands"</li>
	 * <li>if not found, it will try to send it in a channel called
	 * "general"</li>
	 * <li>if not found, it will send the message in the first channel the bot
	 * can write messages in</li>
	 * </ul>
	 * 
	 * @param guild
	 *            - The guild to perform the research in
	 * @return the most appropriate channel to send messages. If it can't find
	 *         any channel where the bot has the SEND_MESSAGES permission, null
	 *         will be returned.
	 */
	public static IChannel findDefaultBotChannelForGuild(IGuild guild) {
		// First channel which the bot can send messages in
		IChannel firstChannel = null;
		// Channel named "bot_commands" or "bot-commands"
		IChannel botCommandsChannel = null;
		// Channel named "general"
		IChannel generalChannel = null;
		// The bot's highest role in the guild
		IRole botHighestRole = null;
		try {
			botHighestRole = guild.getRolesForUser(Main.client.getOurUser()).get(0);
		} catch (IndexOutOfBoundsException e) {
			botHighestRole = guild.getEveryoneRole();
		}

		for (IChannel channel : guild.getChannels()) {
			if (channel.getModifiedPermissions(botHighestRole).contains(Permissions.SEND_MESSAGES)) {
				if (botCommandsChannel == null && channel.getName().matches("bot[-_]commands"))
					botCommandsChannel = channel;
				else if (generalChannel == null && channel.getName().equals("general"))
					generalChannel = channel;
				else if (firstChannel == null)
					firstChannel = channel;
			}
		}
		return botCommandsChannel != null ? botCommandsChannel : generalChannel != null ? generalChannel : firstChannel;
	}
	
	/**
	 * Gets the channel of a guild by the given String.
	 * @param str - The desired channel to look for. It can either be the name, the ID or the mention of it.
	 * @param guild - The guild in which the desired channel is supposed to be
	 * @return The desired channel, or null if the channel could not be found
	 */
	public static IChannel stringToChannel(String str, IGuild guild) {
		long channelID;
		
		try {
			channelID = Long.parseLong(str);
		} catch (NumberFormatException e) {
			try {
				channelID = Long.parseLong(str.substring(2, str.length() -1));
			} catch (NumberFormatException|IndexOutOfBoundsException e2) {
				try {
					channelID = guild.getChannelsByName(str).get(0).getLongID();
				} catch (IndexOutOfBoundsException e3) {
					return null;
				}
			}
		}
		return guild.getChannelByID(channelID);
	}
	
	/**
	 * Gets the role of a guild by the given String.
	 * @param str - The desired role to look for. It can either be the name, the ID or the mention of it.
	 * @param guild - The guild in which the desired role is supposed to be
	 * @return The desired role, or null if the role could not be found
	 */
	public static IRole stringToRole(String str, IGuild guild) {
		long roleID;
		
		try {
			roleID = Long.parseLong(str);
		} catch (NumberFormatException e) {
			try {
				roleID = Long.parseLong(str.substring(2, str.length() -1));
			} catch (NumberFormatException e2) {
				try {
					roleID = guild.getRolesByName(str).get(0).getLongID();
				} catch (IndexOutOfBoundsException e3) {
					return null;
				}
			}
		}
		return guild.getRoleByID(roleID);
	}
	
	public static void sendDebugPMToSuperadmin(String msg) {
		sendMessage(Main.superadmin.getOrCreatePMChannel(), msg);
	}
}
