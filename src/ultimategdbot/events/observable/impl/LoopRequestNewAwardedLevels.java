package ultimategdbot.events.observable.impl;

import java.util.ArrayList;
import java.util.List;

import ultimategdbot.app.Main;
import ultimategdbot.events.observable.Observable;
import ultimategdbot.events.observer.Observer;
import ultimategdbot.events.observer.impl.NewAwardedLevelsObserver;
import ultimategdbot.exceptions.RawDataMalformedException;
import ultimategdbot.net.geometrydash.GDLevel;
import ultimategdbot.net.geometrydash.GDLevelFactory;
import ultimategdbot.net.geometrydash.GDServer;

public class LoopRequestNewAwardedLevels implements Runnable, Observable<LoopRequestNewAwardedLevels> {

	private static final int REQUEST_COOLDOWN_SECONDS = 10;
	private GDLevel lastLevelRecorded;
	private List<Observer<LoopRequestNewAwardedLevels>> obsList = new ArrayList<>();
	
	public LoopRequestNewAwardedLevels() {
		this.addObserver(new NewAwardedLevelsObserver());
	}

	@Override
	public void run() {
		while (true) {
			if (Main.client != null && Main.client.isLoggedIn()) {
				try {
					// First, fetch awarded levels from GD servers
					String awardedLevelsRD = GDServer.fetchNewAwardedLevels();
					//String awardedLevelsRD = GDServer.fetchMostRecentLevels();

					try {
						// Convert the raw data given by the server into GDLevel objects
						List<GDLevel> awardedLevels = GDLevelFactory.buildAllGDLevelsSearchResults(awardedLevelsRD);
						List<GDLevel> newAwardedLevels = new ArrayList<>();
						int i = 0;

						// If it's the first loop, the last record is set to the last awarded level
						if (lastLevelRecorded == null)
							lastLevelRecorded = awardedLevels.get(0);

						// Add new awarded levels to a list
						while (i < awardedLevels.size() && !awardedLevels.get(i).hasSameIDThan(lastLevelRecorded)) {
							newAwardedLevels.add(awardedLevels.get(i));
							i++;
						}

						// Send the list of new awarded levels to the observer so it can notify the subscribers
						if (!newAwardedLevels.isEmpty() && newAwardedLevels.size() < 10)
							updateObservers(newAwardedLevels.toArray());

					} catch (RawDataMalformedException e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else
				System.out.println("Client not logged in, trying again in " + REQUEST_COOLDOWN_SECONDS + " seconds...");
			try {
				Thread.sleep(REQUEST_COOLDOWN_SECONDS * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void addObserver(Observer<LoopRequestNewAwardedLevels> o) {
		this.obsList.add(o);
	}

	@Override
	public void removeObserver(Observer<LoopRequestNewAwardedLevels> o) {
		this.obsList.remove(o);
	}

	@Override
	public void clearObservers() {
		this.obsList.clear();
	}

	@Override
	public void updateObservers(Object... args) {
		for (Observer<LoopRequestNewAwardedLevels> o : obsList)
			o.update(args);
	}

}
