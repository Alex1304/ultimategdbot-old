package ultimategdbot.events.observable.impl;

import java.util.ArrayList;
import java.util.List;

import ultimategdbot.app.Main;
import ultimategdbot.events.GDEvent;
import ultimategdbot.events.LastAwardedStateChangedGDEvent;
import ultimategdbot.events.NewAwardedGDEvent;
import ultimategdbot.events.observable.Observable;
import ultimategdbot.events.observer.Observer;
import ultimategdbot.events.observer.impl.NewAwardedLevelsObserver;
import ultimategdbot.exceptions.RawDataMalformedException;
import ultimategdbot.net.geometrydash.GDLevel;
import ultimategdbot.net.geometrydash.GDLevelFactory;
import ultimategdbot.net.geometrydash.GDServer;
import ultimategdbot.util.AppTools;

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
			if (Main.client.isReady()) {
				try {
					// First, fetch awarded levels from GD servers
					String awardedLevelsRD = !Main.isTestEnvironment() ?
							GDServer.fetchNewAwardedLevels() : 
							GDServer.fetchMostRecentLevels();

					try {
						// Convert the raw data given by the server into GDLevel
						// objects
						List<GDLevel> awardedLevels = GDLevelFactory.buildAllGDLevelsSearchResults(awardedLevelsRD);
						List<GDLevel> newAwardedLevels = new ArrayList<>();
						int i = 0;

						// If it's the first loop, the last record is set to the
						// last awarded level
						if (lastLevelRecorded == null)
							lastLevelRecorded = awardedLevels.get(0);

						// Add new awarded levels to a list
						while (i < awardedLevels.size() && !awardedLevels.get(i).equals(lastLevelRecorded)) {
							newAwardedLevels.add(awardedLevels.get(i));
							i++;
						}
						
						if (checkForStateChange(awardedLevels.get(0)))
							updateObservers(new LastAwardedStateChangedGDEvent(awardedLevels.get(0)));

						lastLevelRecorded = GDLevelFactory.buildGDLevelFirstSearchResult(awardedLevelsRD);

						// Send the list of new awarded levels to the observer
						// so it can notify the subscribers
						if (!newAwardedLevels.isEmpty() && newAwardedLevels.size() < 10)
							updateObservers(new NewAwardedGDEvent(newAwardedLevels));

					} catch (RawDataMalformedException e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else
				System.out.println("Client not ready, trying again in " + REQUEST_COOLDOWN_SECONDS + " seconds...");
			try {
				Thread.sleep(REQUEST_COOLDOWN_SECONDS * 1000);
			} catch (InterruptedException e) {
				AppTools.sendDebugPMToSuperadmin(
						"An error occured when trying to fetch Awarded levels from GD Servers: "
								+ e.getLocalizedMessage());
				System.err.println(e.getLocalizedMessage());
			}
		}
	}

	/**
	 * Check if the state of the specified level is different than the state of
	 * the last recorded level. It immediately returns false if the level ID of
	 * these two levels are different, because it shouldn't compare state of two
	 * actually different levels.
	 * 
	 * @param level
	 *            - The level to check
	 * @return true if state changed between the two levels, false otherwise.
	 *         Also returns false if the two levels don't have the same ID.
	 */
	private boolean checkForStateChange(GDLevel level) {
		if (!lastLevelRecorded.equals(level))
			return false;
		return !lastLevelRecorded.stateEquals(level);
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
	public void updateObservers(GDEvent event) {
		for (Observer<LoopRequestNewAwardedLevels> o : obsList)
			o.update(event);
	}

}
