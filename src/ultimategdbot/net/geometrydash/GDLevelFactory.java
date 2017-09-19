package ultimategdbot.net.geometrydash;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ultimategdbot.exceptions.RawDataMalformedException;

/**
 * Utility class to convert raw data into GDLevel instances
 * 
 * @author Alex1304
 *
 */
public class GDLevelFactory {
	
	/**
	 * Reads the rawdata and return an instance of GDLevel corresponding to the requested level.
	 * When searching for a level using the levelID, only one result can show up.
	 * 
	 * @param rawData - urlencoded String of the level info
	 * @return new instance of GDLevel
	 */
	public static GDLevel buildGDLevelSearchedByID(String rawData) throws RawDataMalformedException {
		return buildGDLevelSearchedByFilter(rawData, 0);
	}
	
	/**
	 * Reads the rawdata and return an instance of GDLevel corresponding to the requested level.
	 * When searching for a level using filters, several search results can show up (up to 10 per page).
	 * So it's necessary to provide which result item is the requested level.
	 * 
	 * @param rawData - urlencoded String of the level search results
	 * @param index - result item corresponding to the requested level
	 * @return new instance of GDLevel
	 * @throws RawDataMalformedException if the raw data syntax is invalid
	 * @throws IndexOutOfBoundsException if the index given doesn't point to a search item.
	 */
	public static GDLevel buildGDLevelSearchedByFilter(String rawData, int index) throws RawDataMalformedException, IndexOutOfBoundsException {
		
		Map<Integer, String> structuredLvlInfo = structureLevelInfo(cutOneLevel(cutLevelInfoPart(rawData), index));
		Map<Long, String> structuredCreatorsInfo = structureCreatorsInfo(cutCreatorInfoPart(rawData));
		
		try {
			return new GDLevel(
				Long.parseLong(structuredLvlInfo.get(1)),
				structuredLvlInfo.get(2),
				structuredCreatorsInfo.get(Long.parseLong(structuredLvlInfo.get(6))),
				new String(Base64.getUrlDecoder().decode(structuredLvlInfo.get(3))),
				Integer.parseInt(structuredLvlInfo.get(39)),
				!structuredLvlInfo.get(19).equals("0"),
				structuredLvlInfo.get(42).equals("1"),
				Integer.parseInt(structuredLvlInfo.get(10)),
				Integer.parseInt(structuredLvlInfo.get(14))
			);
		} catch (NullPointerException|IllegalArgumentException e) {
			throw new RawDataMalformedException();
		}
	}
	
	/**
	 * Rather than selecting one level out of the search results using an index, this method
	 * will return a list of instances of GDLevels corresponding to all the search results.
	 * 
	 * @param rawData - urlencoded String of the level search results
	 * @return a list of GDLevels
	 * @throws RawDataMalformedException if the raw data syntax is invalid
	 */
	public static List<GDLevel> buildAllGDLevelsSearchResults(String rawData) throws RawDataMalformedException {
		List<GDLevel> levelList = new ArrayList<>();
		
		for (int i = 0 ; i < cutLevelInfoPart(rawData).split("\\|").length ; i++) {
			levelList.add(buildGDLevelSearchedByFilter(rawData, i));
		}
		
		return levelList;
	}
	
	// The private methods below are used to make the code of the public methods clearer
	
	private static String cutLevelInfoPart(String rawData) {
		return rawData.split("#")[0];
	}
	
	private static String cutCreatorInfoPart(String rawData) {
		return rawData.split("#")[1];
	}
	
	private static String cutOneLevel(String levelInfoPartRD, int index) {
		return levelInfoPartRD.split("\\|")[index];
	}
	
	private static Map<Integer, String> structureLevelInfo(String oneLevelRD) {
		String[] arrayLvlInfo = oneLevelRD.split(":");
		Map<Integer, String> structuredLvlInfo = new HashMap<>();
		
		for (int i = 0 ; i < arrayLvlInfo.length ; i += 2) {
			structuredLvlInfo.put(Integer.parseInt(arrayLvlInfo[i]), arrayLvlInfo[i+1]);
		}
		
		return structuredLvlInfo;
	}
	
	private static Map<Long, String> structureCreatorsInfo(String creatorsInfoRD) {
		String[] arrayCreatorsRD = creatorsInfoRD.split("\\|");
		Map<Long, String> structuredCreatorslInfo = new HashMap<>();
		
		for (String creatorRD : arrayCreatorsRD) {
			structuredCreatorslInfo.put(Long.parseLong(creatorRD.split(":")[0]), creatorRD.split(":")[1]);
		}
		
		return structuredCreatorslInfo;
	}
}
