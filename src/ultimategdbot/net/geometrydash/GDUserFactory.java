package ultimategdbot.net.geometrydash;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import ultimategdbot.app.AppParams;
import ultimategdbot.exceptions.RawDataMalformedException;
import ultimategdbot.net.database.dao.UserSettingsDAO;
import ultimategdbot.net.database.entities.UserSettings;
import ultimategdbot.util.GDUtils;

/**
 * Utility class to build {@link GDUser} instances.
 * 
 * @author Alex1304
 *
 */
public abstract class GDUserFactory {
	
	/**
	 * The GDUser instance representing the bot in-game.
	 */
	private static GDUser botUser = initBotUser();
	
	/**
	 * Initializes the bot user instance by searching for its profile in-game using the accountID
	 * provided in {@link AppParams}. If nothing is found or if it failed to connect to GD servers,
	 * null is returned.
	 * 
	 * @return GDUser
	 */
	private static GDUser initBotUser() {
		try {
			return buildGDUserFromProfileRawData(GDServer.fetchUserProfile(AppParams.GD_ACCOUNT_ID));
		} catch (RawDataMalformedException | IOException e) {
			return null;
		}
	}
	
	/**
	 * Gets the GDUser instance representing the bot in-game
	 * 
	 * @return GDUser
	 */
	public static GDUser getBotUserInstance() {
		if (botUser == null)
			botUser = initBotUser();
		
		return botUser;
	}
	
	/**
	 * Builds a GDUser instance by parsing the profile raw data.
	 * 
	 * @param rawData
	 *            - user profile data returned by Geometry Dash servers.
	 * @return the built GDUser instance
	 * @throws RawDataMalformedException
	 *             - if the raw data given couldn't be parsed.
	 */
	public static GDUser buildGDUserFromProfileRawData(String rawData) throws RawDataMalformedException {
		GDUser user = null;
		
		try {
			Map<Integer, String> structuredRD = GDUtils.structureRawData(rawData);
			user = new GDUser(structuredRD.get(1), 
					Long.parseLong(structuredRD.get(2)),
					Integer.parseInt(structuredRD.get(13)),
					Integer.parseInt(structuredRD.get(17)),
					Integer.parseInt(structuredRD.get(3)),
					Integer.parseInt(structuredRD.get(46)),
					Integer.parseInt(structuredRD.get(4)),
					Integer.parseInt(structuredRD.get(8)),
					structuredRD.get(20),
					structuredRD.get(30).equals("") ? 0 : Integer.parseInt(structuredRD.get(30)),
					Long.parseLong(structuredRD.get(16)),
					GDRole.values()[Integer.parseInt(structuredRD.get(49))],
					structuredRD.get(44),
					structuredRD.get(45)
			);
		} catch (NullPointerException|IllegalArgumentException e) {
			throw new RawDataMalformedException();
		}
		
		return user;
	}
	
	/**
	 * Searches for the desired user by name or playerID, and returns the
	 * corresponding accountID if found.
	 * 
	 * @param gdNameOrPlayerID
	 *            - user name or playerID
	 * @return the accountID of the searched user, or -1 if not found.
	 * @throws IOException
	 *             if a problem occurs while connecting to GD servers
	 */
	public static long findAccountIDForGDUser(String gdNameOrPlayerID) throws IOException {
		String userSearchResultsRD = GDServer.fetchUsersByNameOrID(gdNameOrPlayerID);
		List<String> searchResultList = new ArrayList<>(Arrays.asList(userSearchResultsRD.split("#")[0].split("\\|")));
		List<Map<Integer, String>> structuredSearchResultList = new ArrayList<>();
		for (String e : searchResultList)
			try {
				structuredSearchResultList.add(GDUtils.structureRawData(e));
			} catch (RawDataMalformedException e1) {
				return -1;
			}
		
		long result = -1;
		int i = 0;
		
		while (result == -1 && i < structuredSearchResultList.size()) {
			try {
				if (gdNameOrPlayerID.equalsIgnoreCase(structuredSearchResultList.get(i).get(1)) ||
						gdNameOrPlayerID.equals(structuredSearchResultList.get(i).get(2)))
					result = Long.parseLong(structuredSearchResultList.get(i).get(16));
			} catch (ArrayIndexOutOfBoundsException|NumberFormatException e) {
				result = -1;
			}
			
			i++;
		}
		
		return result;
	}
	
	/**
	 * If the name provided refers to a Discord user with a Geometry Dash account
	 * linked, it will build the user of linked account. If the name provided is a
	 * GD username, it will simply search for it and build the corresponding user
	 * instance.
	 * 
	 * @param name
	 *            - The name or the Discord tag of user to build
	 * @return the desired user instance
	 * @throws RawDataMalformedException
	 *             if the server returns corrupted data when fetching user profile,
	 *             usually happens when the user couldn't be found
	 * @throws IOException
	 *             if a problem occurs while connecting to GD servers
	 */
	public static GDUser buildGDUserFromNameOrDiscordTag(String name) throws RawDataMalformedException, IOException {
		long accountID = -1;
		if (name.trim().matches("<@!?[0-9]+>")) {
			long userID = Long.parseLong(name.trim().replaceAll("<@!?", "").replace(">", ""));
			UserSettings us = new UserSettingsDAO().find(userID);
			if (us == null || !us.isLinkActivated())
				accountID = -1;
			else
				accountID = us.getGdUserID();
		} else
			accountID = findAccountIDForGDUser(name.replace('_', ' '));
		
		return buildGDUserFromProfileRawData(GDServer.fetchUserProfile(accountID));
	}
}
