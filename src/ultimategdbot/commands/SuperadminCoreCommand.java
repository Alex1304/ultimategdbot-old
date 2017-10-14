package ultimategdbot.commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.app.AppParams;
import ultimategdbot.exceptions.CommandFailedException;

/**
 * Commands that are supposed to be used only by the bot owner (Superadmin) will extend this class.
 * 
 * @author Alex1304
 *
 */
public abstract class SuperadminCoreCommand extends CoreCommand {
	
	

	public SuperadminCoreCommand(String name) {
		super(name);
	}

	/**
	 * {@inheritDoc}
	 * Checks for superadmin privilege
	 */
	@Override
	public final void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		if (event.getAuthor().getLongID() != AppParams.SUPERADMIN_ID)
			throw new CommandFailedException("This command can be used only by the bot developer.");
		
		runSuperadminCommand(event, args);
	}
	
	/**
	 * This is called in the regular runCommand() method, so no need to check for superadmin privilege
	 * @param event
	 * @param args
	 */
	public abstract void runSuperadminCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException;
}
