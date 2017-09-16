package commands;

import java.util.List;

import exceptions.CommandUsageDeniedException;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

/**
 * Bot commands must implement this interface to work
 * 
 * @author alexandre
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
	void runCommand(MessageReceivedEvent event, List<String> args) throws CommandUsageDeniedException;

}
