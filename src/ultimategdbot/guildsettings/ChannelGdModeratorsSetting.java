package ultimategdbot.guildsettings;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

public class ChannelGdModeratorsSetting extends ChannelSetting {

	public ChannelGdModeratorsSetting(IGuild guild, IChannel value) {
		super(guild, "When someone gets granted Moderator in Geometry Dash, I will "
				+ "send a notification to this channel.", value);
	}
	
}
