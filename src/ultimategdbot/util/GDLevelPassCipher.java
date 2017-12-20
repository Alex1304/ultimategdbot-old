package ultimategdbot.util;

import java.util.Base64;

import ultimategdbot.util.robtopsweakcrypto.RobTopsWeakCrypto;

/**
 * Utility class to encode/decode level passwords in Geometry Dash.
 * 
 * @author Alex1304
 *
 */
public class GDLevelPassCipher {

	private static final int LEVEL_PASSWORD_MAX_LENGTH = 6;

	/**
	 * Decodes the input and returns the decoded result.
	 * 
	 * @param encodedInput
	 *            - The password to decode
	 * @return decoded result as an integer value.
	 * @throws NumberFormatException
	 *             if the input isn't formatted as an encoded level
	 *             passcode.
	 */
	public static int decode(String encodedInput) {
		String base64decoded = new String(Base64.getUrlDecoder().decode(encodedInput));
		return Integer.parseInt(RobTopsWeakCrypto.getLevelPassXORCipher().cipher(base64decoded).substring(1));
	}
	
	/**
	 * Encodes the input and returns the encoded result.
	 * 
	 * @param decodedInput
	 *            - The password to encode
	 * @return encoded result as a String.
	 * @throws NumberFormatException
	 *             if the input isn't a valid level passcode.
	 */
	public static String encode(int decodedInput) {
		String inputAsString = "1" + format(decodedInput);
		return Base64.getUrlEncoder().encodeToString(
				RobTopsWeakCrypto.getLevelPassXORCipher().cipher(inputAsString).getBytes());
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

}
