package ultimategdbot.guildsettings;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;

/**
 * The role that the bot is supposed to mention where there are new Geometry Dash moderators
 * 
 * @author Alex1304
 *
 */
public class RoleGdModeratorsSetting extends RoleSetting {

	public RoleGdModeratorsSetting(IGuild guild, IRole value) {
		super(guild, "When I send a notification telling that someone have been granted Moderator"
				+ " access on Geometry Dash, I will mention this role along with the message.", value);
	}

}
