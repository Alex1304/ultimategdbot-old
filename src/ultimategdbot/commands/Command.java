package ultimategdbot.commands;

import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.exceptions.CommandFailedException;

/**
 * A command is a sub-program that is triggered by another program or a person.
 * The behavior of a command can be influenced by the arguments given.
 * This is a core feature for the bot, as it is the only way for it to communicate 
 * and interact with Discord users.
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
	 * @throws CommandFailedException
	 *             if the command was unable to terminate correctly or if the
	 *             command syntax is invalid.
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
	 * 
	 * @param cmdName - The name of the sub-command to trigger
	 * @param event - the Discord event info provided by the parent core command
	 * @param args - arguments to give to the subcommand
	 * @return false if the command could not be found, true otherwise
	 * @throws CommandFailedException
	 *             if the sub-command was unable to terminate correctly or if the
	 *             command syntax is invalid. Note that in most of the cases, the
	 *             parent command won't catch this exception and will propagate it
	 *             to the command handler.
	 */
	boolean triggerSubCommand(String cmdName, MessageReceivedEvent event, List<String> args) throws CommandFailedException;

}
