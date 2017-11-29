package ultimategdbot.gdevents.levels;

import ultimategdbot.net.geometrydash.GDLevel;

/**
 * Event dispatched when there is a new Daily level available on Geometry Dash
 * 
 * @author Alex1304
 *
 */
public class NewDailyLevelGDEvent extends LevelGDEvent {

	public NewDailyLevelGDEvent(GDLevel level) {
		super(level);
	}

}
