package ultimategdbot.commands.impl;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.app.Main;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.CoreCommand;
import ultimategdbot.commands.impl.subcommands.SetupSetSubCommand;
import ultimategdbot.commands.impl.subcommands.SetupInfoSubCommand;
import ultimategdbot.commands.impl.subcommands.SetupResetSubCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.guildsettings.ChannelAwardedLevelsSetting;
import ultimategdbot.guildsettings.ChannelBotAnnouncementsSetting;
import ultimategdbot.guildsettings.GuildSetting;
import ultimategdbot.guildsettings.RoleAwardedLevelsSetting;
import ultimategdbot.net.database.dao.GuildSettingsDAO;
import ultimategdbot.net.database.entities.GuildSettings;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.BotRoles;

public class SetupCommand extends CoreCommand {
	
	public SetupCommand(EnumSet<BotRoles> rolesRequired) {
		super("setup", rolesRequired);
	}

	private GuildSettingsDAO gsdao = new GuildSettingsDAO();
	private GuildSettings settings;

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		this.settings = gsdao.findOrCreate(event.getGuild().getLongID());
		
		// Whether the user typed the command with or without args
		if (args.size() == 0) {
			AppTools.sendMessage(event.getChannel(), settingsAsString());
		} else {
			if (!triggerSubCommand(args.get(0), event, args.subList(1, args.size())))
				throw new CommandFailedException(this);
		}
	}
	
	/**
	 * Gives a String representation of the current settings.
	 * @return String containing the formatted settings info
	 */
	private String settingsAsString() {
		String message = "**__Bot settings for this server:__**\n";
		
		message += "```\n";
		
		for (GuildSetting<?> setting : settings) {
			message += setting + ": " + setting.valueToString() + "\n";
		}
		
		message += "```\n\n";
		
		message += "Run `" + Main.CMD_PREFIX + "help setup` for details" + "\n";
		
		return message;
	}
	
	@Override
	public String getHelp() {
		return "Without arguments, displays the current bot settings for this server.\n"
				+ "The `set` keyword allows you to assign a new value to a field\n"
				+ "The `info` keyword displays detailed info on a specific field\n"
				+ "The `reset` keyword resets a field to its default value";
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		Map<String, Command> subCommandMap = new HashMap<>();
		subCommandMap.put("set", new SetupSetSubCommand(this));
		subCommandMap.put("reset", new SetupResetSubCommand(this));
		subCommandMap.put("info", new SetupInfoSubCommand(this));
		return subCommandMap;
	}

	public GuildSettingsDAO getGsdao() {
		return gsdao;
	}

	public GuildSettings getSettings() {
		return settings;
	}

	@Override
	public String[] getSyntax() {
		String[] res = { "", "set <setting_name> <new_value>", "info|reset <setting_name>" };
		return res;
	}

	@Override
	public String[] getExamples() {
		String[] res = { "", "set " + settings.getSetting(ChannelAwardedLevelsSetting.class) + " #bot-announcements", 
				"info " + settings.getSetting(RoleAwardedLevelsSetting.class),
				"reset " + settings.getSetting(ChannelBotAnnouncementsSetting.class) };
		return res;
	}
}
