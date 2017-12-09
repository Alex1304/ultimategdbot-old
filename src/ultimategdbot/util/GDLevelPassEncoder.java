package ultimategdbot.util;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Utility class to encode/decode level passwords in Geometry Dash.
 * 
 * @author Alex1304
 *
 */
public class GDLevelPassEncoder {
	
	private static Map<Integer, byte[]> digitEncodingMap = initMap();
	private static final int BYTE_ARRAY_SIZE = 7;
	private static final byte FIRST_BYTE_VALUE = 3;
	private static final int LEVEL_PASSWORD_MAX_LENGTH = 6;
	
	private static Map<Integer, byte[]> initMap() {
		Map<Integer, byte[]> map = new HashMap<>();
		
		map.put(0, Base64.getUrlDecoder().decode("AwYDBgQCBg=="));
		map.put(1, Base64.getUrlDecoder().decode("AwcCBwUDBw=="));
		map.put(2, Base64.getUrlDecoder().decode("AwQBBAYABA=="));
		map.put(3, Base64.getUrlDecoder().decode("AwUABQcBBQ=="));
		map.put(4, Base64.getUrlDecoder().decode("AwIHAgAGAg=="));
		map.put(5, Base64.getUrlDecoder().decode("AwMGAwEHAw=="));
		map.put(6, Base64.getUrlDecoder().decode("AwAFAAIEAA=="));
		map.put(7, Base64.getUrlDecoder().decode("AwEEAQMFAQ=="));
		map.put(8, Base64.getUrlDecoder().decode("Aw4LDgwKDg=="));
		map.put(9, Base64.getUrlDecoder().decode("Aw8KDw0LDw=="));
		
		return map;
	}

	/**
	 * Decodes the input and returns the decoded result.
	 * 
	 * @param encodedInput
	 *            - The password to decode
	 * @return decoded result as an integer value.
	 * @throws IllegalArgumentException
	 *             if the input isn't formatted as an encoded level
	 *             passcode.
	 */
	public static int decode(String encodedInput) {
		byte[] base64decoded = Base64.getUrlDecoder().decode(encodedInput);
		if (base64decoded.length > BYTE_ARRAY_SIZE)
			throw new IllegalArgumentException();
		
		String decodedResultString = "";
		
		for (int i = 1 ; i < base64decoded.length ; i++) {
			for (Entry<Integer, byte[]> entry : digitEncodingMap.entrySet()) {
				if (entry.getValue()[i] == base64decoded[i]) {
					decodedResultString += entry.getKey();
					break;
				}
			}
		}
		
		return Integer.parseInt(decodedResultString);
	}
	
	/**
	 * Encodes the input and returns the encoded result.
	 * 
	 * @param decodedInput
	 *            - The password to encode
	 * @return encoded result as a String.
	 * @throws IllegalArgumentException
	 *             if the input isn't a valid level passcode.
	 */
	public static String encode(int decodedInput) {
		byte[] result = new byte[BYTE_ARRAY_SIZE];
		result[0] = FIRST_BYTE_VALUE;
		String inputAsString = format(decodedInput);
		
		for (int i = 1 ; i < result.length ; i++) {
			result[i] = digitEncodingMap.get(Integer.parseInt("" + inputAsString.charAt(i - 1)))[i];
		}
		
		return Base64.getUrlEncoder().encodeToString(result);
	}
	
	/**
	 * If the integer value representing a GD level password has less than 6
	 * digits, it will return the value as a String with exactly 6 digits,
	 * missing digits filled with zeros. For example, if the code
	 * <code>12</code> is input, this method will return <code>"000012"</code>
	 * (as a String).
	 * 
	 * @param pass
	 *            - the passcode to format.
	 * @return the formatted code as String.
	 * @throws NumberFormatException
	 *             if the input isn't a valid level passcode.
	 */
	public static String format(int pass) {
		if ((pass + "").length() > LEVEL_PASSWORD_MAX_LENGTH)
			throw new NumberFormatException();
		
		return AppTools.normalizeNumber(pass, LEVEL_PASSWORD_MAX_LENGTH, '0');
	}
	
//	Some testing
//	
//	public static void main(String[] args) {
//		for (Entry<Integer, byte[]> entry : digitEncodingMap.entrySet()) {
//			System.out.print(entry.getKey() + " => ");
//			for (byte b : entry.getValue())
//				System.out.print(Integer.toHexString(b) + " ");
//			System.out.println();
//		}
//		
//		System.out.println("\n" + decode("AwMHDwQDAQ=="));
//		System.out.println("\n" + encode(0));
//	}

}
