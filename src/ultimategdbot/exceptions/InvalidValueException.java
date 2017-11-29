package ultimategdbot.exceptions;

import ultimategdbot.guildsettings.GuildSetting;

/**
 * Thrown when it fails to parse a String into a valid value in {@link GuildSetting}
 * 
 * @author alexandre
 *
 */
public class InvalidValueException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public InvalidValueException() {
		super();
	}
	
	public InvalidValueException(String message) {
		super(message);
	}

}
