package ultimategdbot.events;

import java.util.List;

import ultimategdbot.net.geometrydash.GDLevel;

/**
 * Event dispatched when there is one or several new awarded levels
 * on Geometry Dash.
 * 
 * @author Alex1304
 *
 */
public class NewAwardedGDEvent implements GDEvent {
	
	private List<GDLevel> newAwardedLevels;

	public NewAwardedGDEvent(List<GDLevel> newAwardedLevels) {
		this.newAwardedLevels = newAwardedLevels;
	}

	public List<GDLevel> getNewAwardedLevels() {
		return newAwardedLevels;
	}
}
