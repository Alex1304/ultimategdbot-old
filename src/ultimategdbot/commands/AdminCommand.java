package ultimategdbot.commands;

import java.util.EnumSet;
import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
import ultimategdbot.exceptions.CommandUsageDeniedException;

/**
 * Commands that are supposed to be used only by guid administrators will extend this class.
 * 
 * @author Alex1304
 *
 */
public abstract class AdminCommand implements Command {
	
	/**
	 * {@inheritDoc}
	 * Checks for Admininstrator permission in the guild
	 */
	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandUsageDeniedException {
		EnumSet<Permissions> authorPerms = event.getAuthor().getPermissionsForGuild(event.getGuild());
		
		if (!authorPerms.contains(Permissions.ADMINISTRATOR))
			throw new CommandUsageDeniedException("You need the Administrator permission to execute this command.");
	}

}
