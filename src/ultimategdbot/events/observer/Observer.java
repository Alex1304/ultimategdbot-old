package ultimategdbot.events.observer;

public interface Observer<T> {
	public void update(Object... args);
}
