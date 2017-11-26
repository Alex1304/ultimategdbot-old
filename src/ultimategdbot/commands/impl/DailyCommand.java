package ultimategdbot.commands.impl;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.CoreCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.exceptions.RawDataMalformedException;
import ultimategdbot.net.geometrydash.GDLevel;
import ultimategdbot.net.geometrydash.GDLevelFactory;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.BotRoles;
import ultimategdbot.util.GDUtils;

public class DailyCommand extends CoreCommand {

	public DailyCommand(EnumSet<BotRoles> rolesRequired) {
		super("daily", rolesRequired);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		try {
			GDLevel level = GDLevelFactory.buildTimelyLevel(true);
			
			AppTools.sendMessage(event.getChannel(), event.getAuthor().mention()
					+ ", here is the Daily level of today:",
					GDUtils.buildEmbedForGDLevel("Daily level", "https://i.imgur.com/a9B6LyS.png", level));
			
		} catch (RawDataMalformedException e) {
			throw new CommandFailedException("Daily level is unavailable at the moment. Please try again later.");
		} catch (IOException e) {
			throw new CommandFailedException("Unable to communicate with Geometry Dash servers at the moment."
					+ " Please try again later.");
		}
	}

	@Override
	public String getHelp() {
		return "Displays the Daily level of today in Geometry Dash.";
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
