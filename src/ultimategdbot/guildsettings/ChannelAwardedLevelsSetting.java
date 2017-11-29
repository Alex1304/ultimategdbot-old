package ultimategdbot.guildsettings;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

/**
 * The channel where the bot is supposed to announce new awarded levels
 * 
 * @author Alex1304
 *
 */
public class ChannelAwardedLevelsSetting extends ChannelSetting {

	public ChannelAwardedLevelsSetting(IGuild guild, IChannel value) {
		super(guild, "When a new level is available in the Awarded section of Geometry Dash, I will "
				+ "send a notification to this channel.", value);
	}

}
