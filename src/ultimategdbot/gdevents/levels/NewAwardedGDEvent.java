package ultimategdbot.gdevents.levels;

import java.util.List;

import ultimategdbot.net.geometrydash.GDLevel;

/**
 * Event dispatched when there is one or several new awarded levels
 * on Geometry Dash.
 * 
 * @author Alex1304
 *
 */
public class NewAwardedGDEvent extends LevelListGDEvent {

	public NewAwardedGDEvent(List<GDLevel> levelList) {
		super(levelList);
	}
}
