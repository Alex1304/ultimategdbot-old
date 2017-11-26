package ultimategdbot.gdevents.users;

import ultimategdbot.net.geometrydash.GDUser;

/**
 * Event dispatched when a user loses access to Moderator Geometry Dash
 * 
 * @author Alex1304
 */
public class UserUnmoddedGDEvent extends UserGDEvent {

	public UserUnmoddedGDEvent(GDUser user) {
		super(user);
	}

}
