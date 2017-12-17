package ultimategdbot.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;
import ultimategdbot.exceptions.RawDataMalformedException;
import ultimategdbot.net.geometrydash.Difficulty;
import ultimategdbot.net.geometrydash.GDLevel;
import ultimategdbot.net.geometrydash.GDRole;
import ultimategdbot.net.geometrydash.GDServer;
import ultimategdbot.net.geometrydash.GDUser;

/**
 * Utilitary class for Geometry Dash related stuff in general
 * @author alexandre
 *
 */
public abstract class GDUtils {
	
	public static Map<String, String> difficultyIconByName = new HashMap<>();
	
	static {
		difficultyIconByName.put("6-harder-featured", "https://i.imgur.com/b7J4AXi.png");
		difficultyIconByName.put("0-insane-epic", "https://i.imgur.com/GdS2f8f.png");
		difficultyIconByName.put("0-harder", "https://i.imgur.com/5lT74Xj.png");
		difficultyIconByName.put("4-hard-epic", "https://i.imgur.com/toyo1Cd.png");
		difficultyIconByName.put("4-hard", "https://i.imgur.com/XnUynAa.png");
		difficultyIconByName.put("6-harder", "https://i.imgur.com/e499HCB.png");
		difficultyIconByName.put("5-hard-epic", "https://i.imgur.com/W11eyJ9.png");
		difficultyIconByName.put("6-harder-epic", "https://i.imgur.com/9x1ddvD.png");
		difficultyIconByName.put("5-hard", "https://i.imgur.com/Odx0nAT.png");
		difficultyIconByName.put("1-auto-featured", "https://i.imgur.com/DplWGja.png");
		difficultyIconByName.put("5-hard-featured", "https://i.imgur.com/HiyX5DD.png");
		difficultyIconByName.put("8-insane-featured", "https://i.imgur.com/PYJ5T0x.png");
		difficultyIconByName.put("0-auto-featured", "https://i.imgur.com/eMwuWmx.png");
		difficultyIconByName.put("8-insane", "https://i.imgur.com/RDVJDaO.png");
		difficultyIconByName.put("7-harder-epic", "https://i.imgur.com/X3N5sm1.png");
		difficultyIconByName.put("0-normal-epic", "https://i.imgur.com/VyV8II6.png");
		difficultyIconByName.put("0-demon-hard-featured", "https://i.imgur.com/lVdup3A.png");
		difficultyIconByName.put("8-insane-epic", "https://i.imgur.com/N2pjW2W.png");
		difficultyIconByName.put("3-normal-epic", "https://i.imgur.com/S3PhlDs.png");
		difficultyIconByName.put("0-normal-featured", "https://i.imgur.com/Q1MYgu4.png");
		difficultyIconByName.put("2-easy", "https://i.imgur.com/yG1U6RP.png");
		difficultyIconByName.put("0-hard-featured", "https://i.imgur.com/8DeaxfL.png");
		difficultyIconByName.put("0-demon-hard-epic", "https://i.imgur.com/xLFubIn.png");
		difficultyIconByName.put("1-auto", "https://i.imgur.com/Fws2s3b.png");
		difficultyIconByName.put("0-demon-hard", "https://i.imgur.com/WhrTo7w.png");
		difficultyIconByName.put("0-easy", "https://i.imgur.com/kWHZa5d.png");
		difficultyIconByName.put("2-easy-featured", "https://i.imgur.com/Kyjevk1.png");
		difficultyIconByName.put("0-insane-featured", "https://i.imgur.com/t8JmuIw.png");
		difficultyIconByName.put("0-hard", "https://i.imgur.com/YV4Afz2.png");
		difficultyIconByName.put("0-na", "https://i.imgur.com/T3YfK5d.png");
		difficultyIconByName.put("7-harder", "https://i.imgur.com/dJoUDUk.png");
		difficultyIconByName.put("0-na-featured", "https://i.imgur.com/C4oMYGU.png");
		difficultyIconByName.put("3-normal", "https://i.imgur.com/cx8tv98.png");
		difficultyIconByName.put("0-harder-featured", "https://i.imgur.com/n5kA2Tv.png");
		difficultyIconByName.put("0-harder-epic", "https://i.imgur.com/Y7bgUu9.png");
		difficultyIconByName.put("0-na-epic", "https://i.imgur.com/hDBDGzX.png");
		difficultyIconByName.put("1-auto-epic", "https://i.imgur.com/uzYx91v.png");
		difficultyIconByName.put("0-easy-featured", "https://i.imgur.com/5p9eTaR.png");
		difficultyIconByName.put("0-easy-epic", "https://i.imgur.com/k2lJftM.png");
		difficultyIconByName.put("0-hard-epic", "https://i.imgur.com/SqnA9kJ.png");
		difficultyIconByName.put("3-normal-featured", "https://i.imgur.com/1v3p1A8.png");
		difficultyIconByName.put("0-normal", "https://i.imgur.com/zURUazz.png");
		difficultyIconByName.put("6-harder-featured", "https://i.imgur.com/b7J4AXi.png");
		difficultyIconByName.put("2-easy-epic", "https://i.imgur.com/wl575nH.png");
		difficultyIconByName.put("7-harder-featured", "https://i.imgur.com/v50cZBZ.png");
		difficultyIconByName.put("0-auto", "https://i.imgur.com/7xI8EOp.png");
		difficultyIconByName.put("0-insane", "https://i.imgur.com/PeOvWuq.png");
		difficultyIconByName.put("4-hard-featured", "https://i.imgur.com/VW4yufj.png");
		difficultyIconByName.put("0-auto-epic", "https://i.imgur.com/QuRBnpB.png");
		difficultyIconByName.put("10-demon-hard", "https://i.imgur.com/jLBD7cO.png");
		difficultyIconByName.put("9-insane-featured", "https://i.imgur.com/byhPbgR.png");
		difficultyIconByName.put("10-demon-hard-featured", "https://i.imgur.com/7deDmTQ.png");
		difficultyIconByName.put("10-demon-hard-epic", "https://i.imgur.com/xtrTl4r.png");
		difficultyIconByName.put("9-insane", "https://i.imgur.com/5VA2qDb.png");
		difficultyIconByName.put("9-insane-epic", "https://i.imgur.com/qmfey5L.png");
		
		// Demon difficulties
		difficultyIconByName.put("0-demon-medium-epic", "https://i.imgur.com/eEEzM6I.png");
		difficultyIconByName.put("10-demon-medium-epic", "https://i.imgur.com/ghco42q.png");
		difficultyIconByName.put("10-demon-insane", "https://i.imgur.com/nLZqoyQ.png");
		difficultyIconByName.put("0-demon-extreme-epic", "https://i.imgur.com/p250YUh.png");
		difficultyIconByName.put("0-demon-easy-featured", "https://i.imgur.com/r2WNVw0.png");
		difficultyIconByName.put("10-demon-easy", "https://i.imgur.com/0zM0VuT.png");
		difficultyIconByName.put("10-demon-medium", "https://i.imgur.com/lvpPepA.png");
		difficultyIconByName.put("10-demon-insane-epic", "https://i.imgur.com/2BWY8pO.png");
		difficultyIconByName.put("10-demon-medium-featured", "https://i.imgur.com/kkAZv5O.png");
		difficultyIconByName.put("0-demon-extreme-featured", "https://i.imgur.com/4MMF8uE.png");
		difficultyIconByName.put("0-demon-extreme", "https://i.imgur.com/v74cX5I.png");
		difficultyIconByName.put("0-demon-medium", "https://i.imgur.com/H3Swqhy.png");
		difficultyIconByName.put("0-demon-medium-featured", "https://i.imgur.com/IaeyGY4.png");
		difficultyIconByName.put("0-demon-insane", "https://i.imgur.com/fNC1iFH.png");
		difficultyIconByName.put("0-demon-easy-epic", "https://i.imgur.com/idesUcS.png");
		difficultyIconByName.put("10-demon-easy-epic", "https://i.imgur.com/wUGOGJ7.png");
		difficultyIconByName.put("10-demon-insane-featured", "https://i.imgur.com/RWqIpYL.png");
		difficultyIconByName.put("10-demon-easy-featured", "https://i.imgur.com/fFq5lbN.png");
		difficultyIconByName.put("0-demon-insane-featured", "https://i.imgur.com/1MpbSRR.png");
		difficultyIconByName.put("0-demon-insane-epic", "https://i.imgur.com/ArGfdeh.png");
		difficultyIconByName.put("10-demon-extreme", "https://i.imgur.com/DEr1HoM.png");
		difficultyIconByName.put("0-demon-easy", "https://i.imgur.com/45GaxRN.png");
		difficultyIconByName.put("10-demon-extreme-epic", "https://i.imgur.com/gFndlkZ.png");
		difficultyIconByName.put("10-demon-extreme-featured", "https://i.imgur.com/xat5en2.png");
	}
	
	/**
	 * Builds an embed for the specified Geometry Dash level
	 * @param authorName - authorName field of the embed
	 * @param authorIcon - authorIcon field of the embed
	 * @param lvl - the level to convert to embed
	 * @return an EmbedObject representing the embedded level
	 */
	public static EmbedObject buildEmbedForGDLevel(String authorName, String authorIcon, GDLevel lvl) {
		EmbedBuilder eb = new EmbedBuilder();
		
		eb.withAuthorName(authorName);
		eb.withAuthorIcon(authorIcon);
		eb.withThumbnail(getDifficultyImageForLevel(lvl));
	
		eb.appendField(Emoji.PLAY + "  __" + lvl.getName() + "__ by " + lvl.getCreator() + "", "**Description:** "
				+ escapeMarkdown(lvl.getDescription()), true);
		eb.appendField(Emoji.INFO + "  Stats and info", Emoji.DOWNLOADS + " " + lvl.getDownloads() + "\t\t"
				+ (lvl.getLikes() < 0 ? Emoji.DISLIKE + " " : Emoji.LIKE + " ") + lvl.getLikes() + "\t\t"
						+ Emoji.LENGTH + " " + lvl.getLength().toString().toUpperCase(), false);
		
		if (lvl.isFeatured())
			eb.appendField("Featured", "Score: " + lvl.getFeaturedScore(), false);
		
		eb.withFooterText("Level ID: " + lvl.getId());
		
		return eb.build();
	}
	
	private static String getDifficultyImageForLevel(GDLevel lvl) {
		String difficulty = "";
		
		difficulty += lvl.getStars() + "-";
		difficulty += lvl.getDifficulty().toString().toLowerCase();
		if (lvl.getDifficulty().equals(Difficulty.DEMON))
			difficulty += "-" + lvl.getDemonDifficulty().toString().toLowerCase();
		if (lvl.isEpic())
			difficulty += "-epic";
		else if (lvl.isFeatured())
			difficulty += "-featured";
		
		return difficultyIconByName.get(difficulty);
	}
	
	public static Map<Integer, String> structureRawData(String rawData) throws RawDataMalformedException {
		String[] arrayOfData = rawData.split(":");
		Map<Integer, String> result = new HashMap<>();

		try {
			for (int i = 0 ; i < arrayOfData.length ; i += 2) {
				result.put(Integer.parseInt(arrayOfData[i]), (i+1 < arrayOfData.length) ? arrayOfData[i+1] : "");
			}
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			throw new RawDataMalformedException();
		}
		
		return result;
	}
	
	/**
	 * Builds an embed for the specified Geometry Dash user
	 * @param authorName - authorName field of the embed
	 * @param authorIcon - authorIcon field of the embed
	 * @param user - the user to convert to embed
	 * @return an EmbedObject representing the embedded user
	 */
	public static EmbedObject buildEmbedForGDUser(String authorName, String authorIcon, GDUser user) {
		EmbedBuilder eb = new EmbedBuilder();
		
		eb.withAuthorName(authorName);
		eb.withAuthorIcon(authorIcon);
		
		eb.appendField(":chart_with_upwards_trend:  " + user.getName() + "'s stats", Emoji.STAR + "  " + user.getStars()+ "\t\t"
			+ Emoji.DIAMOND + "  " + user.getDiamonds() + "\t\t"
			+ Emoji.USERCOIN + "  " + user.getUserCoins() + "\t\t"
			+ Emoji.SECRETCOIN + "  " + user.getSecretCoins() + "\t\t"
			+ Emoji.DEMON + "  " + user.getDemons() + "\t\t"
			+ Emoji.CREATOR_POINTS + "  " + user.getCreatorPoints() + "\n", false);
		
		String mod = "";
		if (user.getRole() == GDRole.MODERATOR)
			mod = Emoji.MODERATOR + "  **" + user.getRole().toString().replaceAll("_", " ") + "**\n";
		else if (user.getRole() == GDRole.ELDER_MODERATOR)
			mod = Emoji.ELDER_MODERATOR + "  **" + user.getRole().toString().replaceAll("_", " ") + "**\n";
		
		try {
			eb.appendField("───────────────────", mod
					+ Emoji.GLOBAL_RANK + "  **Global Rank:** "
					+ (user.getGlobalRank() == 0 ? "*Unranked*" : user.getGlobalRank()) + "\n"
					+ Emoji.YOUTUBE + "  **Youtube:** "
						+ (user.getYoutube().isEmpty() ? "*not provided*" : "[Open link](https://www.youtube.com/channel/"
						+ URLEncoder.encode(user.getYoutube(), "UTF-8") + ")") + "\n"
					+ Emoji.TWITCH + "  **Twitch:** "
						+ (user.getTwitch().isEmpty() ? "*not provided*" : "["  + user.getTwitch()
						+ "](http://www.twitch.tv/" + URLEncoder.encode(user.getTwitch(), "UTF-8") + ")") + "\n"
					+ Emoji.TWITTER + "  **Twitter:** "
						+ (user.getTwitter().isEmpty() ? "*not provided*" : "[@" + user.getTwitter() + "]"
						+ "(http://www.twitter.com/" + URLEncoder.encode(user.getTwitter(), "UTF-8") + ")"),
					false);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		eb.withFooterText("PlayerID: " + user.getPlayerID() + " | " + "AccountID: " + user.getAccountID());
		
		return eb.build();
	}
	
	public static String escapeMarkdown(String text) {
		List<Character> resultList = new ArrayList<>();
		Character[] charsToEscape = { '\\', '_', '*', '~', '`', ':', '@', '#' };
		List<Character> charsToEscapeList = new ArrayList<>(Arrays.asList(charsToEscape));
		
		for (char c : text.toCharArray()) {
			if (charsToEscapeList.contains(c))
				resultList.add('\\');
			resultList.add(c);
		}
		
		char[] result = new char[resultList.size()];
		for (int i = 0 ; i < result.length ; i++)
			result[i] = resultList.get(i);
		
		return new String(result);
	}
	
	/**
	 * Fetches the ID of the current Daily level (if true is given), or Weekly
	 * demon (if false is given).
	 * 
	 * @param daily
	 *            - whether the desired level is the Daily level or Weekly demon
	 * @return the ID of the current Daily/Weekly, or -1 if no level is
	 *         available at this time
	 * @throws IOException
	 *             if not able to connect to Geometry Dash servers.
	 */
	public static int fetchCurrentTimelyID(boolean daily) throws IOException {
		String dailyInfo = GDServer.fetchTimelyLevelInfo(daily);
		
		if (dailyInfo.isEmpty())
			return -1;
		
		return Integer.parseInt(dailyInfo.split("\\|")[0]) - (daily ? 0 : 100000);
	}
	
	/**
	 * Returns the time remaining before the next Daily (if true is given) or
	 * next Weekly (if false is given).
	 * 
	 * @param daily
	 *            - whether the desired level is the Daily level or Weekly demon
	 * @return the time remaining formatted as "x days x hours x minutes x
	 *         seconds", or "(indefinite time)" if the info could not be found.
	 * @throws IOException
	 *             if not able to connect to Geometry Dash servers.
	 */
	public static String fetchNextTimelyCooldown(boolean daily) throws IOException {
		String dailyInfo = GDServer.fetchTimelyLevelInfo(daily);
		if (dailyInfo.isEmpty())
			return "(indefinite time)";
		
		long result = Long.parseLong(dailyInfo.split("\\|")[1]);
		
		long days = result / (24*60*60);
		long hours = (result / (60*60)) % 24;
		long minutes = (result / 60) % 60;
		long seconds = result % 60;
		
		return (days == 0 ? "" : days + " days ")
				+ (hours == 0 ? "" : hours + " hours ")
				+ (minutes == 0 ? "" : minutes + " minutes ")
				+ (seconds == 0 ? "" : seconds + " seconds ");
	}
}
