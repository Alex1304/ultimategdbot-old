package ultimategdbot.util.robtopsweakcrypto;

/**
 * Provides instances of XORCipher used to encrypt some data such as private
 * messages, level passwords and account passwords.
 * 
 * @author Alex1304
 */
public abstract class RobTopsWeakCrypto {

	private static final XORCipher GD_MESSAGE_BODY_XOR_CIPHER = new XORCipher("14251");
	private static final XORCipher LEVEL_PASS_XOR_CIPHER = new XORCipher("26364");
	private static final XORCipher ACCOUNT_GJP_XOR_CIPHER = new XORCipher("37526");
	
	public static XORCipher getGDMessageBodyXORCipher() {
		return GD_MESSAGE_BODY_XOR_CIPHER;
	}
	
	public static XORCipher getLevelPassXORCipher() {
		return LEVEL_PASS_XOR_CIPHER;
	}
	
	public static XORCipher getAccountGJPXORCipher() {
		return ACCOUNT_GJP_XOR_CIPHER;
	}

}
