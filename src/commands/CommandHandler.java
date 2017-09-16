package commands;

import static app.Main.CMD_PREFIX;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exceptions.CommandUsageDeniedException;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

/**
 * Bot commands are handled here, using the Discord API based on events
 * 
 * @author alexandre
 *
 */
public class CommandHandler {

	/**
	 * Map that associates text commands to their actions.
	 */
	private Map<String, Command> commandMap = new HashMap<>();

	/**
	 * Constructor
	 */
	public CommandHandler() {
		loadCommandMap();
	}

	/**
	 * Loads the command map so they are recognized by the handler
	 */
	private void loadCommandMap() {
		// Admin commands
		commandMap.put("changename", new ChangeBotUsernameCommand());
		
		// Public commands
		commandMap.put("test", new TestCommand());
	}

	/**
	 * Handles messages sent in the guild and execute commands
	 * 
	 * @param event
	 */
	@EventSubscriber
	public void onMessageReceived(MessageReceivedEvent event) {
		// Note for error handling, you'll probably want to log failed commands
		// with a logger or sout
		// In most cases it's not advised to annoy the user with a reply incase
		// they didn't intend to trigger a
		// command anyway, such as a user typing ?notacommand, the bot should
		// not say "notacommand" doesn't exist in
		// most situations. It's partially good practise and partially developer
		// preference

		// Given a message "/test arg1 arg2", argArray will contain ["/test",
		// "arg1", "arg2"]
		String[] argArray = event.getMessage().getContent().split(" ");

		// First ensure at least the command and prefix is present, the arg
		// length can be handled by your command func
		if (argArray.length == 0)
			return;

		// Check if the first arg (the command) starts with the prefix defined
		// in the utils class
		if (!argArray[0].startsWith(CMD_PREFIX))
			return;

		// Extract the "command" part of the first arg out by just ditching the
		// first character
		String commandStr = argArray[0].substring(CMD_PREFIX.length());

		// Load the rest of the args in the array into a List for safer access
		List<String> argsList = new ArrayList<>(Arrays.asList(argArray));
		argsList.remove(0); // Remove the command

		if (commandMap.containsKey(commandStr)) {
			try {
				commandMap.get(commandStr).runCommand(event, argsList);
			} catch (CommandUsageDeniedException e) {
				System.err.println("[" + new Date().toString() + "]" + "The user " + event.getAuthor().getName() + "#"
						+ event.getAuthor().getDiscriminator() + " has been denied from using the command " + commandStr);
			} catch (DiscordException e) {
				RequestBuffer.request(() -> {
		    		event.getChannel().sendMessage(":negative_squared_cross_mark: Sorry, an error occured while running the command.\n```\n" + e.getErrorMessage() + "\n```");
		        });
				e.printStackTrace();
			} 
		}
	}
}
