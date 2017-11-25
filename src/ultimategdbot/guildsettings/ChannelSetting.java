package ultimategdbot.guildsettings;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import ultimategdbot.exceptions.InvalidValueException;
import ultimategdbot.util.AppTools;

public abstract class ChannelSetting extends GuildSetting<IChannel> {

	public ChannelSetting(IGuild guild, String info, IChannel value) {
		super(guild, info, value);
	}
	
	@Override
	public String valueToString() {
		if (value == null)
			return super.valueToString();
		
		return "#" + value.getName();
	}

	@Override
	public IChannel parseValue(String valueStr) throws InvalidValueException {
		IChannel channel = AppTools.stringToChannel(valueStr, guild);
		if (channel == null)
			throw new InvalidValueException("This channel couldn't be found.");
		
		return channel;
	}

}
