package ultimategdbot.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.PermissionUtils;
import sx.blah.discord.util.RequestBuffer;
import ultimategdbot.app.Main;

/**
 * Utility class useful for the GDCA implementation of the Discord API
 * 
 * @author Alex1304
 *
 */
public abstract class AppTools {
	
	private static Map<Long, IMessage> messageQueue = new HashMap<>();

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
		ClientBuilder clientBuilder = new ClientBuilder();
		clientBuilder.withToken(token);
		clientBuilder.withRecommendedShardCount();
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
	public static IMessage sendMessage(IChannel channel, String message) {
		return sendMessage(channel, message, null);
	}
	
	/**
	 * Edits a message in Discord using a RequestBuffer
	 * 
	 * @param messageToEdit
	 * @param message
	 */
	public static IMessage editMessage(IMessage messageToEdit, String message) {
		return editMessage(messageToEdit, message, null);
	}

	/**
	 * Sends a message in Discord to the specified channel using a RequestBuffer.
	 * This method supports embeds.
	 * 
	 * @param channel
	 * @param message
	 * @return the IMessage instance of the message sent, or null if it failed to send the message
	 * in less than 10 seconds.
	 */
	public static IMessage sendMessage(IChannel channel, String message, EmbedObject embed) {
		try {
			// This 1 millisecond break will guarantee that every call of this method within the same
			// thread will generate a different timestamp for each message.
			Thread.sleep(1);
		} catch (InterruptedException e) {}
		long currTime = System.currentTimeMillis();
		
		RequestBuffer.request(() -> {
			try {
				messageQueue.put(currTime, (embed != null) ? channel.sendMessage(message, embed) : channel.sendMessage(message));
			} catch (MissingPermissionsException | DiscordException e) {
				System.err.println(e.getLocalizedMessage());
				messageQueue.put(currTime, null);
			}
		});
		
		while (!messageQueue.containsKey(currTime) && System.currentTimeMillis() - currTime < 30000) {}
		
		if (!messageQueue.containsKey(currTime))
			System.err.println("Unable to send message: Timeout");
		
		return messageQueue.remove(currTime);
	}
	
	/**
	 * Applies the sendMessage method to a collection of channels. The same message will be sent
	 * in all of the channels in this collection. This method supports embeds.
	 * 
	 * @param channels - Collection of channels to send the message in
	 * @param message - text content of message
	 * @param embed - embed to attach to the message.
	 * @return a List containing all of the messages successfully sent.
	 */
	public static List<IMessage> sendMessageToAll(Collection<IChannel> channels, String message, EmbedObject embed) {
		List<IMessage> sentMessages = new ArrayList<>();
		
		for (IChannel channel : channels) {
			IMessage m = sendMessage(channel, message, embed);
			if (m != null)
				sentMessages.add(m);
		}
		
		return sentMessages;
	}
	
	/**
	 * Applies the sendMessage method to a collection of channels. The same message will be sent
	 * in all of the channels in this collection.
	 * 
	 * @param channels - Collection of channels to send the message in
	 * @param message - text content of message
	 * @return a List containing all of the messages successfully sent.
	 */
	public static List<IMessage> sendMessageToAll(Collection<IChannel> channels, String message) {
		return sendMessageToAll(channels, message, null);
	}
	
	/**
	 * Edits a message in Discord using a RequestBuffer.
	 * This method supports embeds.
	 * 
	 * @param messageToEdit
	 * @param message
	 * @return the IMessage instance of the edited message, or null if it failed to edit the message
	 * in less than 10 seconds.
	 */
	public static IMessage editMessage(IMessage messageToEdit, String message, EmbedObject embed) {
		long currTime = System.currentTimeMillis();
		
		RequestBuffer.request(() -> {
			try {
				messageQueue.put(currTime, (embed != null) ? messageToEdit.edit(message, embed) : messageToEdit.edit(message));
			} catch (MissingPermissionsException | DiscordException e) {
				System.err.println(e.getLocalizedMessage());
				messageQueue.put(currTime, null);
			}
		});
		
		while (!messageQueue.containsKey(currTime) && System.currentTimeMillis() - currTime < 30000) {}
		
		if (!messageQueue.containsKey(currTime))
			System.err.println("Unable to send message: Timeout");
		
		return messageQueue.remove(currTime);
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
		if (guild == null)
			return null;
		
		// First channel which the bot can send messages in
		IChannel firstChannel = null;
		// Channel named "bot_commands" or "bot-commands"
		IChannel botCommandsChannel = null;
		// Channel named "general"
		IChannel generalChannel = null;

		for (IChannel channel : guild.getChannels()) {
			if (PermissionUtils.hasPermissions(channel, Main.DISCORD_ENV.getClient().getOurUser(), Permissions.SEND_MESSAGES)) {
				if (botCommandsChannel == null && channel.getName().matches(".*bot.*"))
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
	 * 
	 * @param str
	 *            - The desired channel to look for. It can either be the name,
	 *            the ID or the mention of it.
	 * @param guild
	 *            - The guild in which the desired channel is supposed to be
	 * @return The desired channel, or null if the channel could not be found
	 */
	public static IChannel stringToChannel(String str, IGuild guild) {
		long channelID;

		try {
			channelID = Long.parseLong(str);
		} catch (NumberFormatException e) {
			try {
				channelID = Long.parseLong(str.substring(2, str.length() - 1));
			} catch (NumberFormatException | IndexOutOfBoundsException e2) {
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
	 * 
	 * @param str
	 *            - The desired role to look for. It can either be the name, the
	 *            ID or the mention of it.
	 * @param guild
	 *            - The guild in which the desired role is supposed to be
	 * @return The desired role, or null if the role could not be found
	 */
	public static IRole stringToRole(String str, IGuild guild) {
		long roleID;

		try {
			roleID = Long.parseLong(str);
		} catch (NumberFormatException e) {
			try {
				roleID = Long.parseLong(str.substring(3, str.length() - 1));
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

	/**
	 * Returns a String which is the concatenation of all the String elements
	 * from the list, each element seperated by a space.
	 * 
	 * @param args
	 *            - The list to concatenate
	 * @return the concatenated args
	 */
	public static String concatCommandArgs(List<String> args) {
		String result = "";
		for(String s : args)
			result += s + " ";
		
		return result.isEmpty() ? result : result.substring(0, result.length() - 1);
	}

	/**
	 * Sends a PM to the superadmin for debugging
	 * 
	 * @param msg
	 */
	public static void sendDebugPMToSuperadmin(String msg) {
		sendMessage(Main.DISCORD_ENV.getSuperadmin().getOrCreatePMChannel(), msg);
	}
	
	/**
	 * Generates a random String made of alphanumeric characters.
	 * The length of the generated String is specified as an argument.
	 * @param n - the length of the generated String
	 * @return the generated random String
	 */
	public static String generateAlphanumericToken(int n) {
		if (n < 1)
			return null;
		
		final String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		char[] result = new char[n];
		
		for (int i = 0 ; i < result.length ; i++)
			result[i] = alphabet.charAt(new Random().nextInt(alphabet.length()));
		
		return new String(result);
	}
	
	/**
	 * Formats the username of the user specified as argument with the format username#discriminator
	 * @param user - The user whom username will be formatted
	 * @return The formatted username as String.
	 */
	public static String formatDiscordUsername(IUser user) {
		return user.getName() + "#" + user.getDiscriminator();
	}
	
	/**
	 * Restarts the bot. Doesn't work if the bot hasn't been launched using a JAR file.
	 */
	public static void restart() {
		try {
			final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
			File currentJar = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
	
			/* is it a jar file? */
			if(!currentJar.getName().endsWith(".jar"))
				return;
	
			/* Build command: java -jar application.jar */
			final List<String> command = new ArrayList<String>();
			command.add(javaBin);
			command.add("-jar");
			command.add(currentJar.getPath());
			
			final ProcessBuilder builder = new ProcessBuilder(command);
			builder.start();
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Builds a String representing an integer value with a specific amount of digits,
	 * mising digits are filled with the specified char. For example, {@code normalizeNumber(12, 6)}
	 * will return the String "000012", with string length equal to 6. The value is directly returned
	 * as String if the number of digits of the value is greater than n.
	 * 
	 * @param val - the value to normalize
	 * @param n - the number of digits the normalized value should have
	 * @param c - the character to fill the normalized value with
	 * @return the normalized value as String
	 */
	public static String normalizeNumber(int val, int n, char c) {
		String result = "" + val;
		
		while (result.length() < n)
			result = c + result;
		
		return result;
	}
	
}
