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

public class RestartCommand extends CoreCommand {

	public RestartCommand(EnumSet<BotRoles> rolesRequired) {
		super("restart", rolesRequired);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		AppTools.sendMessage(event.getChannel(), "Restarting...");
		AppTools.restart();
		throw new CommandFailedException("Unable to restart the bot because an internal error occured. Please contact"
					+ " the developer or report that in the development server.");
	}

	@Override
	public String getHelp() {
		return "Restarts the bot completely.";
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
