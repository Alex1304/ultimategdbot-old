package ultimategdbot.guildsettings;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;

public class RoleTimelyLevelsSetting extends RoleSetting {

	public RoleTimelyLevelsSetting(IGuild guild, IRole value) {
		super(guild, "When I send a notification telling that a new Daily level or Weekly demon is available "
				+ "on Geometry Dash, I will mention this role along with the message.", value);
	}

}
