package ultimategdbot.commands.impl;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.util.DiscordException;
import ultimategdbot.app.AppTools;
import ultimategdbot.commands.Command;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.net.database.dao.GuildSettingsDAO;
import ultimategdbot.net.database.tables.GuildSettings;

public class GDEventsCommand implements Command {

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		GuildSettings gs = new GuildSettingsDAO().find(event.getGuild().getLongID());
		if (gs == null)
			throw new CommandFailedException("Database Error");

		try {
			IRole roleAwardedSub = event.getGuild().getRoleByID(gs.getGdeventAwardedSubscriberRoleId());
			switch (args.get(0)) {
				case "subscribe":
					event.getAuthor().addRole(roleAwardedSub);
					AppTools.sendMessage(event.getChannel(),
							"You will now get notified when new levels get rated on Geometry Dash!");
					break;
				case "unsubscribe":
					event.getAuthor().removeRole(roleAwardedSub);
					AppTools.sendMessage(event.getChannel(), "You will no longer receive notifications!");
					break;
				default:
					throw new CommandFailedException("Correct usage:\n`g!gdevents subscribe|unsubscribe awarded`");
			}

		} catch (DiscordException e) {
			AppTools.sendMessage(event.getChannel(),
					"Oops, I don't seem to have the permission to assign roles to members...");
		}
	}

}
