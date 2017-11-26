package ultimategdbot.gdevents.users;

import ultimategdbot.gdevents.GDEvent;
import ultimategdbot.net.geometrydash.GDUser;

/**
 * GD events that involve one instance of GDUser will inherit this class
 * 
 * @author Alex1304
 *
 */
public abstract class UserGDEvent implements GDEvent {

	private GDUser user;

	public UserGDEvent(GDUser user) {
		this.user = user;
	}

	public GDUser getUser() {
		return user;
	}

}