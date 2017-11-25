package ultimategdbot.exceptions;

import ultimategdbot.app.Main;
import ultimategdbot.guildsettings.GuildSetting;

public class SettingsNotProvidedException extends CommandFailedException {
	
	private static final long serialVersionUID = 8691754542478563040L;

	public SettingsNotProvidedException(GuildSetting<?>... settings) {
		super(generateFailureMessage(settings));
	}
	
	private static String generateFailureMessage(GuildSetting<?>... settings) {
		String message = "I am not configured to execute this command yet! The following settings"
				+ " need to be provided by a server administrator using the `" + Main.CMD_PREFIX + "setup` command"
				+ ": ";
		
		for (int i = 0 ; i < settings.length ; i++)
			message += (i != 0 ? ", " : "") + "`" + settings[i].toString() + "`";
		
		return message;
	}
}
