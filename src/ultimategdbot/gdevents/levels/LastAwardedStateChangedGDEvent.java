package ultimategdbot.gdevents.levels;

import ultimategdbot.net.geometrydash.GDLevel;

/**
 * Event dispatched when the last awarded level gets a state change
 * 
 * @author Alex1304
 *
 */
public class LastAwardedStateChangedGDEvent extends LevelGDEvent {

	public LastAwardedStateChangedGDEvent(GDLevel level) {
		super(level);
	}
}
