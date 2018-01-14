package ultimategdbot.commands.impl;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.CoreCommand;
import ultimategdbot.commands.impl.subcommands.StatGrindEventInitSubCommand;
import ultimategdbot.commands.impl.subcommands.StatGrindEventJoinSubCommand;
import ultimategdbot.commands.impl.subcommands.StatGrindEventKickSubCommand;
import ultimategdbot.commands.impl.subcommands.StatGrindEventLeaveSubCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.net.database.dao.impl.DAOFactory;
import ultimategdbot.net.database.entities.JoinSGE;
import ultimategdbot.net.database.entities.StatGrindEvent;
import ultimategdbot.net.database.entities.UserSettings;
import ultimategdbot.net.geometrydash.GDUser;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.BotRoles;
import ultimategdbot.util.Emoji;

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
		if (args.isEmpty()) {
			StatGrindEvent ongoingEventInGuild = DAOFactory.getStatGrindEventDAO()
					.findByGuild(event.getGuild().getLongID());
			
			if (ongoingEventInGuild == null)
				throw new CommandFailedException("There is no event running in this server.");
			
			List<JoinSGE> playersWhoJoined = DAOFactory.getJoinSGEDAO().findUsersForEvent(ongoingEventInGuild);
			Map<JoinSGE, GDUser> gdAccOfPlayersWhoJoined = gdAccountsOfPlayersWhoJoined(playersWhoJoined,
					ongoingEventInGuild);
			
			String message = Emoji.INFO + " __**Stat grind event info:**__\n\n";
			message += "**Total players who joined:** " + playersWhoJoined.size() + "\n";
			if (System.currentTimeMillis() < ongoingEventInGuild.getDateBegin() + 86400000)
				message += "**Starts in:** " + AppTools.formatMillis(ongoingEventInGuild.getDateBegin() + 86400000
						- System.currentTimeMillis()) + "\n";
			else {
				message += "**Ends in:** " + AppTools.formatMillis(ongoingEventInGuild.getDateEnd()
					- System.currentTimeMillis()) + "\n\n";
				
				message += "**Current top 10:**\n";
				
				int rank = 1;
				for (Entry<JoinSGE, GDUser> entry : sortedEntryList(gdAccOfPlayersWhoJoined, ongoingEventInGuild)) {
					GDUser user = entry.getValue();
					JoinSGE jsge = entry.getKey();
					String row = "`#" + AppTools.normalizeNumber(rank, 3, ' ') + "`\t|\t"
							+ ongoingEventInGuild.getStatType().getEmoji() + " "
							+ Math.max(0, ongoingEventInGuild.getStatType().forUser(user) - jsge.getCurrStat())
							+ "\t-\t" + user.getName() + " (" + AppTools.formatDiscordUsername(jsge.getUser())
							+ ")\n";
			
					message += row;
					rank++;
				}
			}
			
			AppTools.sendMessage(event.getChannel(), message);
		} else if (!triggerSubCommand(args.get(0), event, args.subList(1, args.size())))
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
		map.put("kick", new StatGrindEventKickSubCommand(this));
		
		return map;
	}
	
	/**
	 * Builds a Map that quickly associates Discord users who joined stat rind
	 * events with their GD profile
	 * 
	 * @param playersWhoJoined
	 *            - Discord users who joined the stat grind event
	 * @param sge
	 *            - the said stat grind event
	 * @return a Map with JoinSGE as key and GDUser as value.
	 */
	private Map<JoinSGE, GDUser> gdAccountsOfPlayersWhoJoined(List<JoinSGE> playersWhoJoined, StatGrindEvent sge) {
		Map<JoinSGE, GDUser> map = new HashMap<>();
		
		List<UserSettings> playersUserSettings = DAOFactory.getUserSettingsDAO()
				.findForLinkedUsers(playersWhoJoined.stream().map(JoinSGE::getUser).collect(Collectors.toList()));
		playersUserSettings = playersUserSettings.stream()
				.filter(us -> us.getGDUserInstance() != null)
				.collect(Collectors.toList());
		
		for (UserSettings us : playersUserSettings) {
			Optional<JoinSGE> optJoined = playersWhoJoined.stream()
					.filter(jsge -> jsge.getUser().getLongID() == us.getUserID()).findAny();
			if (optJoined.isPresent() /* should always return true */ )
				map.put(optJoined.get(), us.getGDUserInstance());
		}
		
		return map;
	}
	
	private List<Entry<JoinSGE, GDUser>> sortedEntryList(Map<JoinSGE, GDUser> map, StatGrindEvent sge) {
		Set<Entry<JoinSGE, GDUser>> entrySet = map.entrySet();
		
		List<Entry<JoinSGE, GDUser>> entryList = entrySet.stream().sorted((e1, e2) -> {
			long score1 = sge.getStatType().forUser(e1.getValue()) - e1.getKey().getCurrStat();
			long score2 = sge.getStatType().forUser(e2.getValue()) - e2.getKey().getCurrStat();
			System.out.println(score1 + " // " + score2);
			return (int) (score2 - score1);
		}).limit(10).collect(Collectors.toList());
		
		return entryList;
	}
}
