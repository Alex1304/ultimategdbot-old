package ultimategdbot.gdevents.levels;

import ultimategdbot.net.geometrydash.GDLevel;

/**
 * Event dispatched when there is a new Weekly demon available on Geometry Dash
 * 
 * @author Alex1304
 *
 */
public class NewWeeklyDemonGDEvent extends LevelGDEvent {

	public NewWeeklyDemonGDEvent(GDLevel level) {
		super(level);
	}

}
