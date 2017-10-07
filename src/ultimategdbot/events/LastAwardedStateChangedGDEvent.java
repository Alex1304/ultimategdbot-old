package ultimategdbot.events;

import ultimategdbot.net.geometrydash.GDLevel;

/**
 * Event dispatched when the last awarded level gets a state change
 * 
 * @author Alex1304
 *
 */
public class LastAwardedStateChangedGDEvent implements GDEvent {
	private GDLevel level;

	public LastAwardedStateChangedGDEvent(GDLevel level) {
		this.level = level;
	}

	public GDLevel getLevel() {
		return level;
	}
}
