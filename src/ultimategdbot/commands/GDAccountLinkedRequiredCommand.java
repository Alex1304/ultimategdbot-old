package ultimategdbot.commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.net.database.dao.UserSettingsDAO;
import ultimategdbot.net.database.entities.UserSettings;

public class GDAccountLinkedRequiredCommand extends EmbeddedCoreCommand {

	public GDAccountLinkedRequiredCommand(CoreCommand cmd) {
		super(cmd);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		UserSettings us = new UserSettingsDAO().find(event.getAuthor().getLongID());
		if (us == null || !us.isLinkActivated())
			throw new CommandFailedException("You must be linked to a Geometry Dash account to use this command.");
		
		cmd.runCommand(event, args);
	}

}
