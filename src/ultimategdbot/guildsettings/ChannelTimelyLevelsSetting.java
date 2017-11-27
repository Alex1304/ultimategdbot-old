package ultimategdbot.guildsettings;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

public class ChannelTimelyLevelsSetting extends ChannelSetting {

	public ChannelTimelyLevelsSetting(IGuild guild, IChannel value) {
		super(guild, "I will send a notification in the specified channel each time there is a new "
				+ "Daily level or a new Weekly demon.", value);
	}

}
