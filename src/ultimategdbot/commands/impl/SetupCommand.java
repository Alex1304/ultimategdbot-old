package ultimategdbot.commands.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IRole;
import ultimategdbot.app.AppTools;
import ultimategdbot.commands.AdminCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.net.database.dao.GuildSettingsDAO;
import ultimategdbot.net.database.entities.GuildSettings;

public class SetupCommand extends AdminCommand {

	@Override
	public void runAdminCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		// Defining settings entries
		Map<String, String> settings = new HashMap<>();
		settings.put("gdevents_subscriber_role", "Undefined");
		settings.put("gdevents_announcements_channel", "Undefined");
		
		GuildSettingsDAO gsdao = new GuildSettingsDAO();
		GuildSettings gs = gsdao.find(event.getGuild().getLongID());
		if (gs == null)
			gsdao.insert(new GuildSettings(event.getGuild().getLongID(), 0, 0));
		
		if (args.size() == 0) {
			// Fetching existing settings info from database
			IRole gdeventsSubRole = event.getGuild().getRoleByID(gs.getGdeventSubscriberRoleId());
			if (gdeventsSubRole != null) settings.put("gdevents_subscriber_role", gdeventsSubRole.getName());
			
			IChannel gdeventsChannel = event.getGuild().getChannelByID(gs.getGdeventSubscriberChannelId());
			if (gdeventsChannel != null) settings.put("gdevents_announcements_channel", gdeventsChannel.getName());
			
			// Building and sending message
			String message = "**Bot settings for this server:**\n";
			message += "```\n";
			for (Entry<String, String> entry : settings.entrySet())
				message += entry.getKey() + " : " + entry.getValue() + "\n";
			message += "```\n";
			message += "To modify a setting, use `g!setup update <setting_name> <new_value>`";
			AppTools.sendMessage(event.getChannel(), message);
		} else {
			if (args.size() != 3 || !args.get(0).equals("update"))
				throw new CommandFailedException("Incorrect usage\n " + getHelp());
			
			String newValue = "";
			switch (args.get(1)) {
				case "gdevents_subscriber_role":
					// The user can set a role either by giving the ID, the name, or by mentionning the role directly.
					try {
						Long.parseLong(args.get(2));
						newValue = args.get(2);
					} catch (NumberFormatException e) {
						try {
							newValue = event.getMessage().getRoleMentions().get(0).getLongID() + "";
						} catch (IndexOutOfBoundsException e2) {
							try {
								newValue = event.getGuild().getRolesByName(args.get(2)).get(0).getLongID() + "";
							} catch (IndexOutOfBoundsException e3) {
								throw new CommandFailedException("Argument 3 is not a valid value!\n ");
							}
						}
					}
					gs.setGdeventSubscriberRoleId(Long.parseLong(newValue));
					break;
				case "gdevents_announcements_channel":
					// The user can set a channel either by giving the ID, the name, or by mentionning the channel directly.
					try {
						Long.parseLong(args.get(2));
						newValue = args.get(2);
					} catch (NumberFormatException e) {
						try {
							newValue = event.getMessage().getChannelMentions().get(0).getLongID() + "";
						} catch (IndexOutOfBoundsException e2) {
							try {
								newValue = event.getGuild().getChannelsByName(args.get(2)).get(0).getLongID() + "";
							} catch (IndexOutOfBoundsException e3) {
								throw new CommandFailedException("Argument 3 is not a valid value!\n ");
							}
						}
					}
					gs.setGdeventSubscriberChannelId(Long.parseLong(newValue));
					break;
				default:
					throw new CommandFailedException("Argument 2 is not a setting name!\n ");
			}
			gsdao.update(gs);
			AppTools.sendMessage(event.getChannel(), ":white_check_mark: Settings updated!");
		}
	}
	
	@Override
	public String getHelp() {
		return "`g!setup [update <setting_name> <new_value>]` - View and edit the bot settings for this server";
	}
}
