package ultimategdbot.guildsettings;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

/**
 * The channel where the bot is supposed to announce new Daily levels and Weekly
 * demons
 * 
 * @author Alex1304
 *
 */
public class ChannelTimelyLevelsSetting extends ChannelSetting {

	public ChannelTimelyLevelsSetting(IGuild guild, IChannel value) {
		super(guild, "I will send a notification in the specified channel each time there is a new "
				+ "Daily level or a new Weekly demon.", value);
	}

}
