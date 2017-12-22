package ultimategdbot.commands.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;
import ultimategdbot.app.Main;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.CoreCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.exceptions.RawDataMalformedException;
import ultimategdbot.net.database.dao.impl.DAOFactory;
import ultimategdbot.net.database.dao.impl.UserSettingsDAO;
import ultimategdbot.net.database.entities.UserSettings;
import ultimategdbot.net.geometrydash.GDUser;
import ultimategdbot.net.geometrydash.GDUserFactory;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.BotRoles;
import ultimategdbot.util.Emoji;

/**
 * Displays a server-wide leaderboard according to users Geometry Dash stats.
 * It can be sorted by stars, diamonds, user/secret coins, or creator points.
 * 
 * @author Alex1304
 *
 */
public class LeaderboardCommand extends CoreCommand {
	
	private static Map<String, Comparator<GDUser>> userComparators = initComparators();
	private static Map<String, Emoji> userStatEmojis = initEmojis();
	private static Map<String, Function<GDUser, Integer>> userStatFunctions = initStatFunctions();
	private static final int MAX_ENTRY_COUNT = 20;

	public LeaderboardCommand(EnumSet<BotRoles> rolesRequired) {
		super("leaderboard", rolesRequired);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		if (args.isEmpty() || !userComparators.containsKey(args.get(0)))
			throw new CommandFailedException(this);
		
		UserSettingsDAO usdao = DAOFactory.getUserSettingsDAO();
		List<IUser> linkedUsers = event.getGuild().getUsers();
		GDUser mentionnedUser = null;
		UserSettings authorUserSettings = usdao.find(event.getAuthor().getLongID());
		GDUser authorUser = authorUserSettings != null ? authorUserSettings.getGDUserInstance() : null;
		
		if (args.size() >= 2)
			try {
				mentionnedUser = GDUserFactory.buildGDUserFromNameOrDiscordTag(args.get(1));
			} catch (RawDataMalformedException e) {
				throw new CommandFailedException("User not found in GD, or hasn't linked his GD account yet.");
			} catch (IOException e) {
				throw new CommandFailedException("Unable to communicate with Geometry Dash servers at the "
						+ "moment. Please try again later.");
			}
		
		List<UserSettings> usList = new ArrayList<>(linkedUsers.stream()
				.map(u -> usdao.find(u.getLongID()))
				.filter(us -> us != null && us.isLinkActivated() && us.getGDUserInstance() != null)
				.sorted((us1, us2) -> userComparators.get(args.get(0))
						.compare(us1.getGDUserInstance(), us2.getGDUserInstance()))
				.collect(Collectors.toList()));
		Collections.reverse(usList);
		
		// To be used in lambda expressions to avoid the "variable must be final" error.
		final GDUser finalMentionnedUser = mentionnedUser;
		if (mentionnedUser != null && usList.stream().map(us -> us.getGDUserInstance())
				.noneMatch(u -> u.equals(finalMentionnedUser)))
			throw new CommandFailedException("This user is not present in this Discord server.");

		Map<Integer, UserSettings> leaderboard = buildLeaderboard(usList, mentionnedUser);
		
		String message = "__Geometry Dash leaderboard for server **" + event.getGuild().getName() + ":**__\n";
		message += "Total players: " + usList.size() + "\n";
		message += "Showing " + (mentionnedUser == null ? "Top " + MAX_ENTRY_COUNT
				: "view for user " + mentionnedUser.getName())
				+ ", " + userStatEmojis.get(args.get(0)) + " leaderboard\n\n";
		
		for (Entry<Integer, UserSettings> entry : leaderboard.entrySet()) {
			GDUser user = entry.getValue().getGDUserInstance();
			String row = "`#" + AppTools.normalizeNumber(entry.getKey(), 3, ' ') + "`\t|\t" + userStatEmojis.get(args.get(0))
					+ " " + userStatFunctions.get(args.get(0)).apply(user) + "\t-\t" + user.getName() + " ("
					+ AppTools.formatDiscordUsername(event.getGuild().getUserByID(entry.getValue().getUserID())) + ")\n";
			
			if (user.equals(mentionnedUser) || user.equals(authorUser))
				row = "**" + row + "**";
			
			message += row;
		}
		
		message += "\nNote that members of this server must have linked their Geometry Dash account to be displayed "
				+ "on this leaderboard. Use `" + Main.CMD_PREFIX + "help account` for details.";
		
		AppTools.sendMessage(event.getChannel(), message);
	}
	
	/**
	 * Initializes the static map of comparators for GD users.
	 * 
	 * @return the initialized {@link Map}
	 */
	private static Map<String, Comparator<GDUser>> initComparators() {
		Map<String, Comparator<GDUser>> map = new HashMap<>();
		
		map.put("stars", Comparator.comparing(GDUser::getStars));
		map.put("diamonds", Comparator.comparing(GDUser::getDiamonds));
		map.put("ucoins", Comparator.comparing(GDUser::getUserCoins));
		map.put("scoins", Comparator.comparing(GDUser::getSecretCoins));
		map.put("demons", Comparator.comparing(GDUser::getDemons));
		map.put("cp", Comparator.comparing(GDUser::getCreatorPoints));
		
		return map;
	}
	
	/**
	 * Initializes the static map of emojis for GD user stats.
	 * 
	 * @return the initialized {@link Map}
	 */
	private static Map<String, Emoji> initEmojis() {
		Map<String, Emoji> map = new HashMap<>();
		
		map.put("stars", Emoji.STAR);
		map.put("diamonds", Emoji.DIAMOND);
		map.put("ucoins", Emoji.USERCOIN);
		map.put("scoins", Emoji.SECRETCOIN);
		map.put("demons", Emoji.DEMON);
		map.put("cp", Emoji.CREATOR_POINTS);
		
		return map;
	}
	
	/**
	 * Initializes the static map of stat functions for GD users.
	 * 
	 * @return the initialized {@link Map}
	 */
	private static Map<String, Function<GDUser, Integer>> initStatFunctions() {
		Map<String, Function<GDUser, Integer>> map = new HashMap<>();
		map.put("stars", u -> u.getStars());
		map.put("diamonds", u -> u.getDiamonds());
		map.put("ucoins", u -> u.getUserCoins());
		map.put("scoins", u -> u.getSecretCoins());
		map.put("demons", u -> u.getDemons());
		map.put("cp", u -> u.getCreatorPoints());
		
		return map;
	}
	
	/**
	 * Returns a Map of user settings mapped by their rank representing the
	 * leaderboard, with at most {@link LeaderboardCommand#MAX_ENTRY_COUNT}
	 * entries.
	 * 
	 * @param fullList
	 *            - the list to extract the entries from
	 * @param forUser
	 *            - if not null, this method will return the leaderboard
	 *            centered for this user, or the top leaderboard otherwise.
	 * @return the leaderboard map
	 * @throws IllegalArgumentException
	 *             if userRank is negative or equal to zero
	 */
	private Map<Integer, UserSettings> buildLeaderboard(List<UserSettings> fullList, GDUser forUser) {
		Map<Integer, UserSettings> result = new TreeMap<>();
		int rank = 0;
		for (UserSettings us : fullList)
			result.put(++rank, us);
		
		int min = 0, max = fullList.size();
		
		if (fullList.size() > MAX_ENTRY_COUNT) {
			int userRank = fullList.stream().map(us -> us.getGDUserInstance()).collect(Collectors.toList()).indexOf(forUser);
			if (forUser != null && userRank != -1) {
				min = userRank - MAX_ENTRY_COUNT / 2;
				max = userRank + MAX_ENTRY_COUNT / 2;

				if (min < 0) {
					max += Math.abs(min);
					min = 0;
				} else if (max > fullList.size()) {
					min -= (max - fullList.size());
					max = fullList.size();
				}

			} else
				max = MAX_ENTRY_COUNT;
		}
		
		final int finalMin = min, finalMax = max;
		result.entrySet().removeIf(entry -> entry.getKey() <= finalMin || entry.getKey() > finalMax);
		
		return result;
	}

	@Override
	public String getHelp() {
		return "Displays a server-wide leaderboard according to users Geometry Dash stats."
				+ "It can be sorted by stars, diamonds, user/secret coins, or creator points.";
	}

	@Override
	public String[] getSyntax() {
		String[] syn = { "stars|diamonds|ucoins|scoins|demons|cp [<gd_username_or_discord_@_mention>]" };
		return syn;
	}

	@Override
	public String[] getExamples() {
		String[] syn = { "stars", "stars MyGDName", "ucoins", "cp", "demons @MyDiscordName#0000" };
		return syn;
	}
	
	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
