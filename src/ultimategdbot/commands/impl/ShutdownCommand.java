package ultimategdbot.commands.impl;

import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.SuperadminCoreCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.util.AppTools;

public class ShutdownCommand extends SuperadminCoreCommand {

	public ShutdownCommand() {
		super("shutdown");
	}

	@Override
	public void runSuperadminCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		AppTools.sendMessage(event.getChannel(), "Shuting down...");
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
