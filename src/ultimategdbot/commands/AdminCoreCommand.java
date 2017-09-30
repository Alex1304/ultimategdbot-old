package ultimategdbot.commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.PermissionUtils;
import ultimategdbot.app.Main;
import ultimategdbot.exceptions.CommandFailedException;

/**
 * Commands that are supposed to be used only by guild administrators will extend this class.
 * 
 * @author Alex1304
 *
 */
public abstract class AdminCoreCommand extends CoreCommand {
	
	/**
	 * {@inheritDoc}
	 * Checks for Admininstrator permission in the guild
	 */
	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		if (event.getAuthor().getLongID() != Main.superadminID &&
				PermissionUtils.hasPermissions(event.getChannel(), event.getAuthor(), Permissions.ADMINISTRATOR))
			throw new CommandFailedException("You need the Administrator permission to execute this command.");
		
		runAdminCommand(event, args);
	}
	
	/**
	 * This is called in the regular runCommand() method, so no need to check for superadmin privilege
	 * @param event
	 * @param args
	 */
	public abstract void runAdminCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException;
}
