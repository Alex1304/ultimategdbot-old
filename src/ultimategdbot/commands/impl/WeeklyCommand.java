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

public class WeeklyCommand extends CoreCommand {

	public WeeklyCommand(EnumSet<BotRoles> rolesRequired) {
		super("weekly", rolesRequired);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		try {
			GDLevel level = GDLevelFactory.buildTimelyLevel(false);
			
			AppTools.sendMessage(event.getChannel(), event.getAuthor().mention()
					+ ", here is the Weekly demon of this week. Next Weekly in " + GDUtils.fetchNextTimelyCooldown(false),
					GDUtils.buildEmbedForGDLevel("Weekly demon #" + GDUtils.fetchCurrentTimelyID(false),
							"https://i.imgur.com/kcsP5SN.png", level));
			
		} catch (RawDataMalformedException e) {
			throw new CommandFailedException("Weekly demon is unavailable at the moment. Please try again later.");
		} catch (IOException e) {
			throw new CommandFailedException("Unable to communicate with Geometry Dash servers at the moment."
					+ " Please try again later.");
		}
	}

	@Override
	public String getHelp() {
		return "Displays the Weekly demon of the current week.";
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
