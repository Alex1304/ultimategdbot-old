package ultimategdbot.commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.util.BotRoles;

/**
 * Sub-commands restricted to server admins will extend this class
 * 
 * @author Alex1304
 */
public abstract class ServerAdminOnlySubCommand<T extends Command> extends SubCommand<T> {

	public ServerAdminOnlySubCommand(T parentCommand) {
		super(parentCommand);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		if (!BotRoles.isGranted(event.getAuthor(), event.getChannel(), BotRoles.SERVER_ADMIN))
			throw new CommandFailedException("This sub-command may only be used by server administrators.");
		
		runServAdminSubCommand(event, args);
	}
	
	/**
	 * Code for the restricted sub-command
	 * @param event
	 * @param args
	 * @throws CommandFailedException
	 */
	public abstract void runServAdminSubCommand(MessageReceivedEvent event, List<String> args)
			throws CommandFailedException;
}
