package ultimategdbot.gdevents.levels;

import java.util.List;

import ultimategdbot.gdevents.GDEvent;
import ultimategdbot.net.geometrydash.GDLevel;

/**
 * GD events that involve a list of instances of GDLevel will inherit this class
 * 
 * @author Alex1304
 *
 */
public abstract class LevelListGDEvent implements GDEvent {

	
	private List<GDLevel> levelList;

	public LevelListGDEvent(List<GDLevel> levelList) {
		this.levelList = levelList;
	}

	public List<GDLevel> getLevelList() {
		return levelList;
	}
}
