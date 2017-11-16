package ultimategdbot.commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.exceptions.CommandFailedException;

public class SingleRunningCommand extends EmbeddedCoreCommand {
	
	private static boolean running = false;

	public SingleRunningCommand(CoreCommand cmd) {
		super(cmd);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		if (running)
			throw new CommandFailedException("This command is already running. This command cannot be run several times"
					+ " in parallel by different users.");
		running = true;
		cmd.runCommand(event, args);
		running = false;
	}
}
