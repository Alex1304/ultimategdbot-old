package ultimategdbot.commands.impl;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.CoreCommand;
import ultimategdbot.commands.impl.subcommands.StatGrindEventInitSubCommand;
import ultimategdbot.commands.impl.subcommands.StatGrindEventJoinSubCommand;
import ultimategdbot.commands.impl.subcommands.StatGrindEventLeaveSubCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.util.BotRoles;

/**
 * Provides sub-commands related to stat grind events
 * 
 * @author Alex1304
 *
 */
public class StatGrindEventCommand extends CoreCommand {

	public StatGrindEventCommand(EnumSet<BotRoles> rolesRequired) {
		super("statgrindevent", rolesRequired);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		if (args.isEmpty() || !triggerSubCommand(args.get(0), event, args.subList(1, args.size())))
			throw new CommandFailedException(this);
	}

	@Override
	public String getHelp() {
		return "Allows users to create, manage, join and leave stat grind events. In those events, "
				+ "participants will have to grind a specific stat (stars, demons, diamonds, etc) in "
				+ " a limited time. Server admininstrators can start an event at anytime, as long as there is "
				+ "no other one already running in the said server. The duration can be set as 1 day (minimum)"
				+ " up to 90 days (maximum). Events will always have a preparation time of 1 day,"
				+ " to let users some time to join them before start. People can still join after"
				+ " the event has started of course, but they will be disadvantaged as they will have "
				+ "less time to grind their stats.\n"
				+ "When someone joins, the bot will keep in memory his current stats, and will compare them with "
				+ "his new stats once the event ends. A leaderboard will be finally displayed announcing the "
				+ "top 10 winners. Every participant will also receive a DM with their final individual rank, "
				+ "and can view their current rank at anytime while the event is running.\n"
				+ "Server admins have the ability to kick other people from the event (for example if someone "
				+ "is using hacks to gain stats faster). They can still re-join but they will start from scratch.\n"
				+ "**Note: people must have linked their GD account to participate to stat grind events!**";
	}

	@Override
	public String[] getSyntax() {
		String[] syn = { "init stars|diamonds|ucoins|scoins|demons|cp <duration_in_days>", "status", "rank", "cancel", "join", "leave", 
				"kick <user>"};
		return syn;
	}

	@Override
	public String[] getExamples() {
		String[] ex = { "init stars 7", "init demons 30", "status", "rank", "cancel", "join", "leave", "kick Hacker123" };
		return ex;
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		Map<String, Command> map = new HashMap<>();
		
		map.put("init", new StatGrindEventInitSubCommand(this));
		map.put("join", new StatGrindEventJoinSubCommand(this));
		map.put("leave", new StatGrindEventLeaveSubCommand(this));
		
		return map;
	}
}
