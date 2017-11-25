package ultimategdbot.guildsettings;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

public class ChannelAwardedLevelsSetting extends ChannelSetting {

	public ChannelAwardedLevelsSetting(IGuild guild, IChannel value) {
		super(guild, "When a new level is available in the Awarded section of Geometry Dash, I will "
				+ "send a notification to this channel.", value);
	}

}
