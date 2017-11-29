package ultimategdbot.guildsettings;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import ultimategdbot.util.AppTools;

/**
 * The channel where the bot is supposed to send important announcements from the dev
 * 
 * @author Alex1304
 *
 */
public class ChannelBotAnnouncementsSetting extends ChannelSetting {

	public ChannelBotAnnouncementsSetting(IGuild guild, IChannel value) {
		super(guild, "When the bot developer sends an important announcement, it will be sent in "
				+ "this channel. If this is not provided, the message will be sent in the first channel"
				+ " containing the word \"bot\" in this server (or the general channel if there is none).", value);
	}
	
	@Override
	public IChannel defaultValue() {
		return AppTools.findDefaultBotChannelForGuild(guild);
	}
	
	@Override
	public String valueToString() {
		return super.valueToString() + (value.equals(defaultValue()) ? " (default)" : "");
	}

}
