package ultimategdbot.net.geometrydash;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ultimategdbot.exceptions.RawDataMalformedException;
import ultimategdbot.util.GDUtils;

/**
 * Utility class to convert raw data into GDLevel instances
 * 
 * @author Alex1304
 *
 */
public abstract class GDLevelFactory {
	
	/**
	 * Associates the integer value in the raw data with the corresponding difficulty
	 */
	private static Map<Integer, Difficulty> difficultyByValue = new HashMap<>();
	
	/**
	 * Associates the integer value in the raw data with the corresponding Demon difficulty
	 */
	private static Map<Integer, DemonDifficulty> demonDifficultyByValue = new HashMap<>();
	
	static {
		difficultyByValue.put(0, Difficulty.NA);
		difficultyByValue.put(10, Difficulty.EASY);
		difficultyByValue.put(20, Difficulty.NORMAL);
		difficultyByValue.put(30, Difficulty.HARD);
		difficultyByValue.put(40, Difficulty.HARDER);
		difficultyByValue.put(50, Difficulty.INSANE);
		
		demonDifficultyByValue.put(0, DemonDifficulty.HARD);
		demonDifficultyByValue.put(3, DemonDifficulty.EASY);
		demonDifficultyByValue.put(4, DemonDifficulty.MEDIUM);
		demonDifficultyByValue.put(5, DemonDifficulty.INSANE);
		demonDifficultyByValue.put(6, DemonDifficulty.EXTREME);
	}
	
	/**
	 * Reads the rawdata and return an instance of GDLevel corresponding to the requested level.
	 * Taking the first search result
	 * 
	 * @param rawData - urlencoded String of the level info
	 * @return new instance of GDLevel
	 */
	public static GDLevel buildGDLevelFirstSearchResult(String rawData) throws RawDataMalformedException {
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
		try {
			Map<Integer, String> structuredLvlInfo = GDUtils.structureRawData(cutOneLevel(cutLevelInfoPart(rawData), index));
			Map<Long, String> structuredCreatorsInfo = structureCreatorsInfo(cutCreatorInfoPart(rawData));

			// Determines the difficulty of the level
			Difficulty lvlDiff = difficultyByValue.get(Integer.parseInt(structuredLvlInfo.get(9)));
			if (structuredLvlInfo.get(25).equals("1"))
				lvlDiff = Difficulty.AUTO;
			if (structuredLvlInfo.get(17).equals("1"))
				lvlDiff = Difficulty.DEMON;
		
			return new GDLevel(
				Long.parseLong(structuredLvlInfo.get(1)),
				structuredLvlInfo.get(2),
				structuredCreatorsInfo.get(Long.parseLong(structuredLvlInfo.get(6))),
				new String(Base64.getUrlDecoder().decode(structuredLvlInfo.get(3))),
				lvlDiff,
				demonDifficultyByValue.get(Integer.parseInt(structuredLvlInfo.get(43))),
				Short.parseShort(structuredLvlInfo.get(18)),
				Integer.parseInt(structuredLvlInfo.get(19)),
				structuredLvlInfo.get(42).equals("1"),
				Long.parseLong(structuredLvlInfo.get(10)),
				Long.parseLong(structuredLvlInfo.get(14)),
				Length.values()[Integer.parseInt(structuredLvlInfo.get(15))]
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
	
	private static Map<Long, String> structureCreatorsInfo(String creatorsInfoRD) {
		String[] arrayCreatorsRD = creatorsInfoRD.split("\\|");
		Map<Long, String> structuredCreatorslInfo = new HashMap<>();
		
		for (String creatorRD : arrayCreatorsRD) {
			structuredCreatorslInfo.put(Long.parseLong(creatorRD.split(":")[0]), creatorRD.split(":")[1]);
		}
		
		return structuredCreatorslInfo;
	}
}
