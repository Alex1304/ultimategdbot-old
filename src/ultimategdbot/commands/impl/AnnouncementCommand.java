package ultimategdbot.commands.impl;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.app.Main;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.CoreCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.BotRoles;

public class AnnouncementCommand extends CoreCommand {

	public AnnouncementCommand(EnumSet<BotRoles> rolesRequired) {
		super("announcement", rolesRequired);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		Main.DISCORD_ENV.getClient().getGuilds().forEach(g -> {
			AppTools.sendMessage(AppTools.findDefaultBotChannelForGuild(g), "[Announcement by the bot developer] "
					+ AppTools.concatCommandArgs(args));
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
