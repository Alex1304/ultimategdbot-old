package ultimategdbot.commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.exceptions.CommandFailedException;

/**
 * Will run the command only if the user has launched it in a Discord server.
 * Will throw a CommandFailedException if it has been launched in a private channel.
 * 
 * @author Alex1304
 *
 */
public class ServerOnlyCommand extends EmbeddedCoreCommand {

	public ServerOnlyCommand(CoreCommand cmd) {
		super(cmd);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		if (event.getChannel().isPrivate())
			throw new CommandFailedException("This command cannot be used in DMs.");
		cmd.runCommand(event, args);
	}
}

