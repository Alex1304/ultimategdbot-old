package ultimategdbot.commands.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IRole;
import ultimategdbot.commands.AdminCoreCommand;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.impl.subcommands.SetupEditSubCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.net.database.dao.GuildSettingsDAO;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.GuildSettingsAsObject;

public class SetupCommand extends AdminCoreCommand {
	
	private Map<String, String> settings = new HashMap<>();
	private GuildSettingsDAO gsdao = new GuildSettingsDAO();
	private GuildSettingsAsObject gso;

	@Override
	public void runAdminCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		this.gso = new GuildSettingsAsObject(gsdao.find(event.getGuild().getLongID()));
		
		if (gso.isNull())
			throw new CommandFailedException("This guild couldn't be found in the database.");
		
		// Whether the user typed the command with or without args
		if (args.size() == 0) {
			refreshSettingsMap(gso);
			AppTools.sendMessage(event.getChannel(), settingsMapAsString());
		} else {
			if (args.size() < 3)
				throw new CommandFailedException(this);
			
			triggerSubCommand(args.get(0), event, args.subList(1, args.size()));
		}
	}
	
	/**
	 * Fills the map with the up-to-date guild settings information
	 * 
	 * @param gso - Object providing guild settings info
	 */
	private void refreshSettingsMap(GuildSettingsAsObject gso) {
		// Settings as objects
		IRole gdeventsSubRole = gso.getGdEventsSubscriberRole();
		IChannel gdeventsChannel = gso.getGdEventsAnnouncementChannel();
		
		// Adding them to the map after converting them as String
		settings.put("gdevents_subscriber_role", gdeventsSubRole == null ? "Undefined" : gdeventsSubRole.getName());
		settings.put("gdevents_announcements_channel", gdeventsChannel == null ? "Undefined" : gdeventsChannel.getName());
	}
	
	/**
	 * Gives a String representation of the settings map.
	 * @return String containing the formatted map info
	 */
	private String settingsMapAsString() {
		String message = "**Bot settings for this server:**\n";
		message += "```\n";
		for (Entry<String, String> entry : settings.entrySet())
			message += entry.getKey() + " : " + entry.getValue() + "\n";
		message += "```\n";
		message += "To edit a setting, use `g!setup edit <setting_name> <new_value>`\n";
		message += "To reset a setting to its default value, use `g!setup reset <setting_name>`\n";
		
		return message;
	}
	
	@Override
	public String getHelp() {
		return "`g!setup [edit <setting_name> <new_value>]` - View and edit the bot settings for this server";
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		Map<String, Command> subCommandMap = new HashMap<>();
		subCommandMap.put("edit", new SetupEditSubCommand(this));
		return subCommandMap;
	}

	public Map<String, String> getSettings() {
		return settings;
	}

	public GuildSettingsDAO getGsdao() {
		return gsdao;
	}

	public GuildSettingsAsObject getGso() {
		return gso;
	}
}
