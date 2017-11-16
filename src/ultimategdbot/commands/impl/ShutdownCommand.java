package ultimategdbot.commands.impl;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.CoreCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.BotRoles;

public class ShutdownCommand extends CoreCommand {

	public ShutdownCommand(EnumSet<BotRoles> rolesRequired) {
		super("shutdown", rolesRequired);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		AppTools.sendMessage(event.getChannel(), "Shutting down...");
		System.exit(0);
	}

	@Override
	public String getHelp() {
		return "Shuts the bot down.";
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
