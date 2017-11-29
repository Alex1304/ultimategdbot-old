package ultimategdbot.commands.impl;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.app.Main;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.CoreCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.guildsettings.ChannelBotAnnouncementsSetting;
import ultimategdbot.guildsettings.TagEveryoneOnBotAnnouncementSetting;
import ultimategdbot.net.database.dao.GuildSettingsDAO;
import ultimategdbot.net.database.entities.GuildSettings;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.BotRoles;

/**
 * Allows the user to send an announcement message to all guilds, in the channel specified in
 * the guild settings. Should be administrator only.
 * 
 * @author Alex1304
 *
 */
public class AnnouncementCommand extends CoreCommand {

	public AnnouncementCommand(EnumSet<BotRoles> rolesRequired) {
		super("announcement", rolesRequired);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		final GuildSettingsDAO gsdao = new GuildSettingsDAO();
		
		Main.DISCORD_ENV.getClient().getGuilds().forEach(g -> {
			GuildSettings settings = gsdao.findOrCreate(g.getLongID());
			boolean tagEveryone = settings.getSetting(TagEveryoneOnBotAnnouncementSetting.class).getValue();
			
			AppTools.sendMessage(settings.getSetting(ChannelBotAnnouncementsSetting.class).getValue(),
					"[Announcement by the bot developer] " + AppTools.concatCommandArgs(args)
					+ (tagEveryone ? "\n\n@everyone" : ""));
		});
		
		AppTools.sendMessage(event.getChannel(), ":white_check_mark: Announcement sent to all guilds!");
	}

	@Override
	public String getHelp() {
		return "Sends an announcement message to all guilds.";
	}

	@Override
	public String[] getSyntax() {
		String[] syn = { "<message>" };
		return syn;
	}

	@Override
	public String[] getExamples() {
		String[] ex = { "The bot will be down for maintenance." };
		return ex;
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
