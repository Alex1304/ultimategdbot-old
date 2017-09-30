package ultimategdbot.commands;

import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.exceptions.CommandFailedException;

/**
 * A command is a sub-program that is triggered by another program or a person.
 * The behavior of a command can be influenced by the arguments given.
 * 
 * @author Alex1304
 *
 */
public interface Command {

	/**
	 * Executes the command
	 * 
	 * @param event
	 *            - contains all info about the user who launched the command,
	 *            the channels, the guild, etc
	 * @param args
	 *            - arguments the user provided with the command
	 */
	void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException;

	/**
	 * Gets a map containing all subcommands, mapped by their name
	 * 
	 * @return {@link Map}
	 */
	Map<String, Command> getSubCommandMap();
	
	/**
	 * Triggers a subcommand by giving its name
	 * @param cmdName - The name of the command to trigger
	 * @param event - the Discord event info provided by the parent core command
	 * @param args - arguments to give to the subcommand
	 */
	void triggerSubCommand(String cmdName, MessageReceivedEvent event, List<String> args) throws CommandFailedException;

}
