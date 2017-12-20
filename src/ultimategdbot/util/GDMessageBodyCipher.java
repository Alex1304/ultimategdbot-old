package ultimategdbot.util;

import java.util.Base64;

import ultimategdbot.exceptions.InvalidCharacterException;
import ultimategdbot.util.robtopsweakcrypto.RobTopsWeakCrypto;

/**
 * Utility class to encode/decode private messages in Geometry Dash.
 * 
 * @author Alex1304
 *
 */
public abstract class GDMessageBodyCipher {
	
	public static String encode(String s) throws InvalidCharacterException {
		String cipher = RobTopsWeakCrypto.getGDMessageBodyXORCipher().cipher(s);
		return new String(Base64.getUrlEncoder().encode(cipher.getBytes()));
	}
	
	public static String decode(String s) {
		String base64decoded = new String(Base64.getUrlDecoder().decode(s));
		return RobTopsWeakCrypto.getGDMessageBodyXORCipher().cipher(base64decoded);
	}
}
