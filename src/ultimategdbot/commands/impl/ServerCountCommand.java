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

public class ServerCountCommand extends CoreCommand {

	public ServerCountCommand(EnumSet<BotRoles> rolesRequired) {
		super("servercount", rolesRequired);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		String message = "**UltimateGDBot is member of " + Main.DISCORD_ENV.getClient().getGuilds().size()
				+ " servers!**\n";
		AppTools.sendMessage(event.getChannel(), message);
	}

	@Override
	public String getHelp() {
		return "Displays the number of servers where the bot is operating.";
	}

	@Override
	public String[] getSyntax() {
		return null;
	}

	@Override
	public String[] getExamples() {
		return null;
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
