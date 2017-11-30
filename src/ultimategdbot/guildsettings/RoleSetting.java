package ultimategdbot.guildsettings;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import ultimategdbot.exceptions.InvalidValueException;
import ultimategdbot.util.AppTools;

/**
 * Guild setting which value is a Discord role
 * 
 * @author Alex1304
 *
 */
public abstract class RoleSetting extends GuildSetting<IRole> {

	public RoleSetting(IGuild guild, String info, IRole value) {
		super(guild, info, value);
	}
	
	@Override
	public String valueToString() {
		if (value == null)
			return super.valueToString();
		
		return "@" + value.getName();
	}

	@Override
	public IRole parseValue(String valueStr) throws InvalidValueException {
		IRole role = AppTools.stringToRole(valueStr, guild);
		if (role == null)
			throw new InvalidValueException("This role couldn't be found.");
		
		return role;
	}

}
