package ultimategdbot.events;

import ultimategdbot.net.geometrydash.GDLevel;

/**
 * Event dispatched when a level gets unrated on Geometry Dash
 * 
 * @author Alex1304
 */
public class LastAwardedDeletedGDEvent extends LevelGDEvent {

	public LastAwardedDeletedGDEvent(GDLevel level) {
		super(level);
	}
}
