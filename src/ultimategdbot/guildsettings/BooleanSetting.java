package ultimategdbot.guildsettings;

import sx.blah.discord.handle.obj.IGuild;
import ultimategdbot.exceptions.InvalidValueException;

public abstract class BooleanSetting extends GuildSetting<Boolean> {

	public BooleanSetting(IGuild guild, String info, Boolean value) {
		super(guild, info, value);
	}
	
	@Override
	public String valueToString() {
		return value ? "ON" : "OFF";
	}

	@Override
	public Boolean parseValue(String valueStr) throws InvalidValueException {
		if (valueStr.equalsIgnoreCase("on"))
			return true;
		else if (valueStr.equalsIgnoreCase("off"))
			return false;
		
		throw new InvalidValueException("You must specify either `ON` or `OFF` as value for this setting"
				+ " (case insensitive)");
	}
	
	@Override
	public Boolean defaultValue() {
		return false;
	}

}
