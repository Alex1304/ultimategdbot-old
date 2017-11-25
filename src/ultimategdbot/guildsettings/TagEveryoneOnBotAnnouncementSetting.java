package ultimategdbot.guildsettings;

import sx.blah.discord.handle.obj.IGuild;

public class TagEveryoneOnBotAnnouncementSetting extends BooleanSetting {

	public TagEveryoneOnBotAnnouncementSetting(IGuild guild, Boolean value) {
		super(guild, "When the bot developer sends an important announcement, you "
				+ "can toggle this on to allow the bot to tag everyone in the server. Note that I need "
				+ "the proper permissions in this server to perform this! Disabled by default.", value);
	}
	
}
