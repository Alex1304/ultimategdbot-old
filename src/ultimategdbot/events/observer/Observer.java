package ultimategdbot.events.observer;

import ultimategdbot.events.GDEvent;

public interface Observer<T> {
	public void update(GDEvent event);
}
