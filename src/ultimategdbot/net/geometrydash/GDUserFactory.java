package ultimategdbot.net.geometrydash;

import java.io.IOException;
import java.util.Map;

import ultimategdbot.exceptions.RawDataMalformedException;
import ultimategdbot.net.database.dao.UserSettingsDAO;
import ultimategdbot.net.database.entities.UserSettings;
import ultimategdbot.util.GDUtils;

public abstract class GDUserFactory {
	
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
					false,
					structuredRD.get(44),
					structuredRD.get(45)
			);
		} catch (NullPointerException|IllegalArgumentException e) {
			throw new RawDataMalformedException();
		}
		
		return user;
	}
	
	public static long findAccountIDForGDUser(String gdNameOrPlayerID) throws IOException {
		String userSearchResultsRD = GDServer.fetchUserByNameOrID(gdNameOrPlayerID);
		long result;
		
		try {
			if (!gdNameOrPlayerID.equalsIgnoreCase(userSearchResultsRD.split(":")[1]) && 
					!gdNameOrPlayerID.equals(userSearchResultsRD.split(":")[3]))
				return -1;
			result = Long.parseLong(userSearchResultsRD.split(":")[21]);
		} catch (ArrayIndexOutOfBoundsException|NumberFormatException e) {
			return -1;
		}
		
		return result;
	}

	public static GDUser buildGDUserFromNameOrDiscordTag(String name) throws RawDataMalformedException, IOException {
		long accountID = -1;
		if (name.matches("<@!?[0-9]>")) {
			long userID = Long.parseLong(name.replaceAll("<@!?", "").replace(">", ""));
			UserSettings us = new UserSettingsDAO().find(userID);
			if (us == null || !us.isLinkActivated())
				return null;
			
			accountID = us.getGdUserID();
		} else
			accountID = GDUserFactory.findAccountIDForGDUser(name);
		
		return GDUserFactory.buildGDUserFromProfileRawData(GDServer.fetchUserProfile(accountID));
	}
}
