package ultimategdbot.gdevents.users;

import ultimategdbot.net.geometrydash.GDUser;

/**
 * Event dispatched when a user gets granted access to Moderator on Geometry Dash
 * 
 * @author Alex1304
 */
public class UserModdedGDEvent extends UserGDEvent {

	public UserModdedGDEvent(GDUser user) {
		super(user);
	}

}
