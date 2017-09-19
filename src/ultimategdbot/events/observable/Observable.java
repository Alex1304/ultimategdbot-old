package ultimategdbot.events.observable;

import ultimategdbot.events.observable.impl.LoopRequestNewAwardedLevels;
import ultimategdbot.events.observer.Observer;

public interface Observable<T> {
	public void addObserver(Observer<LoopRequestNewAwardedLevels> o);
	public void removeObserver(Observer<T> o);
	public void clearObservers();
	public void updateObservers(Object... args);
}
