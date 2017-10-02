package ultimategdbot.exceptions;

import ultimategdbot.util.Settings;

public class SettingsNotProvidedException extends CommandFailedException {
	
	private static final long serialVersionUID = 8691754542478563040L;

	public SettingsNotProvidedException(Settings... settings) {
		super(generateFailureMessage(settings));
	}
	
	private static String generateFailureMessage(Settings... settings) {
		String message = "I am not set up to execute this command yet! The following settings"
				+ " need to be provided by a server administrator using the `g!setup` command"
				+ ": ";
		
		for (int i = 0 ; i < settings.length ; i++)
			message += (i != 0 ? ", " : "") + "`" + settings[i].toString() + "`";
		
		return message;
	}
}
