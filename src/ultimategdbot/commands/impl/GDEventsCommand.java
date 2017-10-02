package ultimategdbot.commands.impl;

import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.util.MissingPermissionsException;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.CoreCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.exceptions.SettingsNotProvidedException;
import ultimategdbot.net.database.dao.GuildSettingsDAO;
import ultimategdbot.net.database.entities.GuildSettings;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.Settings;

public class GDEventsCommand extends CoreCommand {

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		if (args.size() != 1)
			throw new CommandFailedException(this);
		
		GuildSettings gs = new GuildSettingsDAO().findOrCreate(event.getGuild().getLongID());
		
		if (gs.getGdeventSubscriberRoleId() == 0)
			throw new SettingsNotProvidedException(Settings.GDEVENTS_SUBSCRIBER_ROLE);

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
					throw new CommandFailedException(this);
			}

		} catch (MissingPermissionsException e) {
			AppTools.sendMessage(event.getChannel(),
					"Oops, it seems I don't have the permission to assign this role to members...");
		}
	}

	@Override
	public String getHelp() {
		return "`g!gdevents [subscribe|unsubscribe]` - When a certain event happens on Geometry Dash"
				+ " (RobTop rating new levels, etc), I"
				+ " will send a notification in the server by pinging a role. Use this command to"
				+ " assign/remove this role by yourself!\n";
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
