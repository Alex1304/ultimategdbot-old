package ultimategdbot.commands.impl;

import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.util.MissingPermissionsException;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.CoreCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.net.database.dao.GuildSettingsDAO;
import ultimategdbot.net.database.entities.GuildSettings;
import ultimategdbot.util.AppTools;

public class GDEventsCommand extends CoreCommand {

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		if (args.size() != 1)
			throw new CommandFailedException("Correct usage:\n`g!gdevents [subscribe|unsubscribe]`");
		
		GuildSettings gs = new GuildSettingsDAO().find(event.getGuild().getLongID());
		if (gs == null)
			throw new CommandFailedException("Database Error");
		
		if (gs.getGdeventSubscriberChannelId() == 0 || gs.getGdeventSubscriberRoleId() == 0)
			throw new CommandFailedException("I am not set up to execute this command yet! The following settings"
					+ " need to be provided by a server administrator using the `g!setup` command"
					+ ": `gdevents_announcements_channel`, `gdevents_subscriber_role`");

		try {
			IRole roleAwardedSub = event.getGuild().getRoleByID(gs.getGdeventSubscriberRoleId());
			switch (args.get(0)) {
				case "subscribe":
					if (!event.getAuthor().getRolesForGuild(event.getGuild()).contains(roleAwardedSub)) {
						event.getAuthor().addRole(roleAwardedSub);
						AppTools.sendMessage(event.getChannel(),
							"You will now get notified when something great happens on Geometry Dash!");
					} else
						throw new CommandFailedException("You are already subscribed to GD events notifications!");
					break;
				case "unsubscribe":
					if (event.getAuthor().getRolesForGuild(event.getGuild()).contains(roleAwardedSub)) {
						event.getAuthor().removeRole(roleAwardedSub);
						AppTools.sendMessage(event.getChannel(), "You will no longer receive notifications!");
					} else
						throw new CommandFailedException("You are already unsubscribed to GD events notifications!");
					break;
				default:
					throw new CommandFailedException("Correct usage:\n`g!gdevents [subscribe|unsubscribe]`");
			}

		} catch (MissingPermissionsException e) {
			AppTools.sendMessage(event.getChannel(),
					"Oops, I don't seem to have the permission to assign roles to members...");
		}
	}

	@Override
	public String getHelp() {
		return "`g!gdevents [subscribe|unsubscribe]` - When a certain event happens on Geometry Dash"
				+ " (RobTop rating new levels, a member of this server gets his level rated, etc), I"
				+ " will send a notification in the server by pinging a role. Use this command to"
				+ " assign/remove this role by yourself!";
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		// TODO Auto-generated method stub
		return null;
	}

}
