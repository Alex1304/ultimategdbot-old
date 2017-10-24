package ultimategdbot.events;

import ultimategdbot.net.geometrydash.GDLevel;

/**
 * GD events that involve one instance of GDLevel will inherit this class
 * 
 * @author Alex1304
 *
 */
public abstract class LevelGDEvent implements GDEvent {

	private GDLevel level;

	public LevelGDEvent(GDLevel level) {
		this.level = level;
	}

	public GDLevel getLevel() {
		return level;
	}
}
