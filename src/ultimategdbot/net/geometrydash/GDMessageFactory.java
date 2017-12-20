package ultimategdbot.net.geometrydash;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import ultimategdbot.exceptions.RawDataMalformedException;
import ultimategdbot.util.GDMessageBodyCipher;
import ultimategdbot.util.GDUtils;

/**
 * Utility class to build {@link GDMessage} instances.
 * 
 * @author Alex1304
 *
 */
public abstract class GDMessageFactory {

	/**
	 * Builds a list of all GD messages that are present in the given raw data.
	 * Message bodies are not loaded! (empty strings).
	 * 
	 * @param rawData
	 *            - the data to decode in order to build the GDMessage instances
	 * @return a List of GDMessage
	 * @throws RawDataMalformedException if the given raw data is malformed / in invalid format
	 */
	public static List<GDMessage> buildAllGDMessagesFromFirstPage(String rawData) throws RawDataMalformedException {
		List<GDMessage> result = new ArrayList<>();
		
		try {
			String[] rdSplittedByMessage = rawData.split("#")[0].split("\\|");
			
			for (String oneMessageRD : rdSplittedByMessage)
				result.add(buildOneGDMessage(oneMessageRD));
			
		} catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
			System.err.println(rawData);
			e.printStackTrace();
			throw new RawDataMalformedException();
		}
		
		return result;
	}
	
	/**
	 * Builds a single instance of GDMessage according to the given raw data.
	 * 
	 * @param rawData - the data containing info about the message
	 * @return a GDMessage instance
	 * @throws RawDataMalformedException if the given raw data is malformed / in invalid format
	 */
	public static GDMessage buildOneGDMessage(String rawData) throws RawDataMalformedException {
		GDMessage result = null;
		
		try {
			Map<Integer, String> structuredRD = GDUtils.structureRawData(rawData);
			
			GDUser sender = new GDUser(structuredRD.get(6),
					Long.parseLong(structuredRD.get(3)),
					0, 0, 0, 0, 0, 0, "", 0,
					Long.parseLong(structuredRD.get(2)),
					GDRole.USER, "", "");
			
			String body = structuredRD.containsKey(5) ? GDMessageBodyCipher.decode(structuredRD.get(5)) : "";
			
			result = new GDMessage(Long.parseLong(structuredRD.get(1)),
					sender,
					GDUserFactory.getBotUserInstance(),
					new String(Base64.getUrlDecoder().decode(structuredRD.get(4))),
					body);
		
		} catch (NullPointerException | NumberFormatException e) {
			System.err.println(rawData);
			e.printStackTrace();
			throw new RawDataMalformedException();
		}
		
		return result;
	}
	
}
