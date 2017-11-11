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
import ultimategdbot.net.geometrydash.GDServer;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.BotRoles;
import ultimategdbot.util.GDUtils;

public class LevelCommand extends CoreCommand {

	public LevelCommand(EnumSet<BotRoles> rolesRequired) {
		super("level", rolesRequired);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		try {
			String rawData = GDServer.fetchLevelByNameOrID(AppTools.concatCommandArgs(args));
			
			GDLevel level = GDLevelFactory.buildGDLevelFirstSearchResult(rawData);
			
			AppTools.sendMessage(event.getChannel(), event.getAuthor().mention()
					+ ", here is the level you searched for:",
					GDUtils.buildEmbedForGDLevel("Search Result", "https://i.imgur.com/a9B6LyS.png", level));
			
		} catch (RawDataMalformedException e) {
			throw new CommandFailedException("No results found.");
		} catch (IOException e) {
			throw new CommandFailedException("Unable to communicate with Geometry Dash servers at the moment."
					+ " Please try again later.");
		}
	}

	@Override
	public String getHelp() {
		return "Searches for any online level available in Geometry Dash";
	}

	@Override
	public String[] getSyntax() {
		String[] res = { "<name_or_id>" };
		return res;
	}

	@Override
	public String[] getExamples() {
		String[] res = { "36227266", "BloodBath" };
		return res;
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
