package ultimategdbot.commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.exceptions.CommandFailedException;

/**
 * This overlay ensures that only one instance of the command is running at once.
 * If someone attempts to execute this command while someone else is already running
 * it, a CommandFailedException will be thrown and same will happen for everyone else
 * until the command is not running anymore.
 * 
 * @author Alex1304
 *
 */
public class SingleRunningCommand extends EmbeddedCoreCommand {
	
	private boolean running = false;

	public SingleRunningCommand(CoreCommand cmd) {
		super(cmd);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		if (running)
			throw new CommandFailedException("This command is already running. This command cannot be run several times"
					+ " in parallel by different users.");
		running = true;
		try {
			cmd.runCommand(event, args);
		} catch (Exception e) {
			running = false;
			throw e;
		} finally {
			running = false;
		}
		
	}
}
