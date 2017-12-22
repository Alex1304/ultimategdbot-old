package ultimategdbot.commands.impl;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.util.MissingPermissionsException;
import ultimategdbot.app.Main;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.CoreCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.guildsettings.GuildSetting;
import ultimategdbot.guildsettings.GuildSettingNames;
import ultimategdbot.net.database.dao.impl.DAOFactory;
import ultimategdbot.net.database.dao.impl.GuildSettingsDAO;
import ultimategdbot.net.database.entities.GuildSettings;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.BotRoles;

/**
 * Allows the user to subscribe to any Geometry Dash events.
 * The subscriber roles must be configured in the server previously.
 * 
 * @author Alex1304
 *
 */
public class GDEventsCommand extends CoreCommand {

	public GDEventsCommand(EnumSet<BotRoles> rolesRequired) {
		super("gdevents", rolesRequired);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		if (!args.isEmpty() && args.size() != 2)
			throw new CommandFailedException(this);
		
		GuildSettingsDAO gsdao = DAOFactory.getGuildSettingsDAO();
		GuildSettings settings = gsdao.findOrCreate(event.getGuild().getLongID());
		
		if (args.isEmpty()) {
			String message = "__**Geometry Dash events you can subscribe to in this server:**__\n";
			message += "Use `" + Main.CMD_PREFIX + name + " <event_name> subscribe` to get the desired role !\n";
			boolean noResult = true;
			
			for (GuildSetting<?> setting : settings) {
				if (setting.toString().startsWith("role_") && setting.getValue() != null) {
					noResult = false;
					message += "`" + setting.toString().replaceFirst("role_", "") + "`\n";
				}
			}
			
			if (noResult)
				throw new CommandFailedException("There is no role set up in this server to mention upon new "
						+ "Geometry Dash events. Ask a server administrator to provide some fields starting "
						+ "with \"role_\" using `" + Main.CMD_PREFIX + "setup` !");
			
			AppTools.sendMessage(event.getChannel(), message);
			return;
		}
		
		@SuppressWarnings("unchecked")
		Class<? extends GuildSetting<IRole>> settingClass = 
				(Class<? extends GuildSetting<IRole>>) GuildSettingNames.get("role_" + args.get(0));
		
		if (settingClass == null || settings.getSetting(settingClass).getValue() == null)
			throw new CommandFailedException("This event does not exist or the role associated to it isn't "
					+ "set up in this server");
		IRole role = settings.getSetting(settingClass).getValue();
		
		try {
			switch (args.get(1)) {
				case "subscribe":
					if (event.getAuthor().hasRole(role))
						throw new CommandFailedException(event.getAuthor().mention() + ", you are already subscribed to this event.");
					event.getAuthor().addRole(role);
					AppTools.sendMessage(event.getChannel(), ":white_check_mark: " + event.getAuthor().mention()
							+ ", you got the `" + role.getName() + "` role ! You will now receive a notification "
							+ "when the event `" + args.get(0) + "` happens !");
					break;
				case "unsubscribe":
					if (!event.getAuthor().hasRole(role))
						throw new CommandFailedException(event.getAuthor().mention() + ", you are already unsubscribed from this event.");
					event.getAuthor().removeRole(role);
					AppTools.sendMessage(event.getChannel(), ":white_check_mark: " + event.getAuthor().mention()
							+ ", you no longer have the `" + role.getName() + "` role. You will no longer receive a "
							+ "notification when the event `" + args.get(0) + "` happens.");
					break;
				default:
					throw new CommandFailedException(this);
			}
		} catch (MissingPermissionsException e) {
			throw new CommandFailedException("I don't have permissions in this server to give you the role "
					+ "associated with the requested event. Please ask a server admin to give me the necessary "
					+ "permissions to manage the roles defined in setup !");
		}
		
	}

	@Override
	public String getHelp() {
		return "When a certain event happens on Geometry Dash (RobTop rating new levels, new moderators, etc), "
				+ "I will send a notification in the server along with a role tag, if specified by "
				+ "an administrator. If so, then you can use this command to assign/remove the roles in question"
				+ "by yourself!";
	}

	@Override
	public String[] getSyntax() {
		String[] syn = { "", "<event_name> subscribe|unsubscribe" };
		return syn;
	}

	@Override
	public String[] getExamples() {
		String[] syn = { "", "awarded_levels subscribe", "gd_moderators unsubscribe" };
		return syn;
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
