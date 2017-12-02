package ultimategdbot.commands.impl;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

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

/**
 * Allows the user to compare the score of two featured levels in order to determine
 * which one is higher placed in the Featured levels section of Geometry Dash.
 * 
 * @author Alex1304
 *
 */
public class CompareCommand extends CoreCommand {

	public CompareCommand(EnumSet<BotRoles> rolesRequired) {
		super("compare", rolesRequired);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		if (args.size() < 3 || !args.contains("&") || args.indexOf("&") != args.lastIndexOf("&")
				|| args.indexOf("&") == 0 || args.indexOf("&") == args.size() - 1)
			throw new CommandFailedException(this);

		String level1Str = AppTools.concatCommandArgs(args.subList(0, args.indexOf("&")));
		String level2Str = AppTools.concatCommandArgs(args.subList(args.indexOf("&") + 1, args.size()));

		try {
			String level1RD = GDServer.fetchLevelByNameOrID(level1Str);
			GDLevel level1 = GDLevelFactory.buildGDLevelFirstSearchResult(level1RD);
			String level2RD = GDServer.fetchLevelByNameOrID(level2Str);
			GDLevel level2 = GDLevelFactory.buildGDLevelFirstSearchResult(level2RD);

			if (!level1.isFeatured() || !level2.isFeatured())
				throw new CommandFailedException("Both levels must either be featured or epic!");

			SortedSet<GDLevel> sl = new TreeSet<>((o1, o2) -> o1.getFeaturedScore() != o2.getFeaturedScore()
					? o2.getFeaturedScore() - o1.getFeaturedScore() : (int) o2.getId() - (int) o1.getId());
			sl.add(level1);
			if (!sl.add(level2))
				throw new CommandFailedException("Cannot compare a level to itself!");

			String message = "";
			message += levelSummary(level1) + "\n";
			message += levelSummary(level2) + "\n";
			message += "\n";
			if (level1.getFeaturedScore() == level2.getFeaturedScore())
				message += "As you can see, the score of both levels are identical. In that case, "
						+ "levels are sorted by their ID. ";
			message += "Therefore, " + sl.first().toString() + " is higher placed than "
					+ sl.last().toString() + " in the Featured section.";
			
			AppTools.sendMessage(event.getChannel(), message);
		} catch (RawDataMalformedException | IOException e) {
			throw new CommandFailedException(
					"One of the two specified level names/IDs are invalid or couldn't be found on "
					+ "Geometry Dash servers.");
		}
	}

	private String levelSummary(GDLevel level) {
		return "<:Play:364096635019722764> " + level.toString() + " has a featured score of **" + level.getFeaturedScore() + "**.";
	}

	@Override
	public String getHelp() {
		return "Compares the score of two featured levels and tells you which one is higher placed in "
				+ "the Featured section of the game.";
	}

	@Override
	public String[] getSyntax() {
		String[] res = { "<level1_name_or_id> & <level2_name_or_id>" };
		return res;
	}

	@Override
	public String[] getExamples() {
		String[] res = { "Bloodbath & Supersonic", "36227266 & 32320513" };
		return res;
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
