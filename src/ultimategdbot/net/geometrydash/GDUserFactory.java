package ultimategdbot.net.geometrydash;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
		String userSearchResultsRD = GDServer.fetchUsersByNameOrID(gdNameOrPlayerID);
		List<String> searchResultList = new ArrayList<>(Arrays.asList(userSearchResultsRD.split("#")[0].split("\\|")));
		List<Map<Integer, String>> structuredSearchResultList = new ArrayList<>();
		searchResultList.forEach(e -> {
			System.out.println(e);
			structuredSearchResultList.add(GDUtils.structureRawData(e));
		});
		
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

	public static GDUser buildGDUserFromNameOrDiscordTag(String name) throws RawDataMalformedException, IOException {
		long accountID = -1;
		if (name.trim().matches("<@!?[0-9]+>")) {
			long userID = Long.parseLong(name.trim().replaceAll("<@!?", "").replace(">", ""));
			UserSettings us = new UserSettingsDAO().find(userID);
			if (us == null || !us.isLinkActivated())
				return null;
			
			accountID = us.getGdUserID();
		} else
			accountID = GDUserFactory.findAccountIDForGDUser(name.replace('_', ' '));
		
		return GDUserFactory.buildGDUserFromProfileRawData(GDServer.fetchUserProfile(accountID));
	}
}
