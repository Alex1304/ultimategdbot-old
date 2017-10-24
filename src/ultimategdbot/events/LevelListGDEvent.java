package ultimategdbot.events;

import java.util.List;

import ultimategdbot.net.geometrydash.GDLevel;

public abstract class LevelListGDEvent implements GDEvent {

	
	private List<GDLevel> levelList;

	public LevelListGDEvent(List<GDLevel> levelList) {
		this.levelList = levelList;
	}

	public List<GDLevel> getLevelList() {
		return levelList;
	}
}
