package ultimategdbot.events.observable.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ultimategdbot.app.Main;
import ultimategdbot.events.GDEvent;
import ultimategdbot.events.LastAwardedDeletedGDEvent;
import ultimategdbot.events.LastAwardedStateChangedGDEvent;
import ultimategdbot.events.NewAwardedGDEvent;
import ultimategdbot.events.observable.Observable;
import ultimategdbot.events.observer.Observer;
import ultimategdbot.events.observer.impl.AwardedLevelsObserver;
import ultimategdbot.exceptions.RawDataMalformedException;
import ultimategdbot.net.database.dao.GDLevelDAO;
import ultimategdbot.net.geometrydash.GDLevel;
import ultimategdbot.net.geometrydash.GDLevelFactory;
import ultimategdbot.net.geometrydash.GDServer;
import ultimategdbot.util.AppTools;

public class LoopRequestNewAwardedLevels implements Runnable, Observable<LoopRequestNewAwardedLevels> {

	private static final int REQUEST_COOLDOWN_SECONDS = 10;
	private GDLevel lastLevelRecorded;
	private List<Observer<LoopRequestNewAwardedLevels>> obsList = new ArrayList<>();

	public LoopRequestNewAwardedLevels() {
		this.addObserver(new AwardedLevelsObserver());
	}

	@Override
	public void run() {
		while (true) {
			if (Main.DISCORD_ENV.getClient().isReady()) {
				try {
					// First, fetch awarded levels from GD servers
					String awardedLevelsRD = !Main.isTestEnvironment() ?
							GDServer.fetchNewAwardedLevels() : 
							GDServer.fetchMostRecentLevels();
							
					GDLevelDAO gdldao = new GDLevelDAO();

					try {
						// Convert the raw data given by the server into GDLevel
						// objects
						List<GDLevel> awardedLevels = GDLevelFactory.buildAllGDLevelsSearchResults(awardedLevelsRD);
						List<GDLevel> newAwardedLevels = new ArrayList<>();
						int i = 0;
						
						if (lastLevelRecorded == null) {
							lastLevelRecorded = gdldao.findLastAwarded();
							// Checks if this level still exists on GD. If not, the level is deleted from the DB.
							try {
								quickLevelSearch(lastLevelRecorded.getId() + "");
							} catch (RawDataMalformedException e) {
								if (!lastLevelRecorded.equals(awardedLevels.get(0))) {
									gdldao.delete(lastLevelRecorded);
									lastLevelRecorded = null;
								}
							}
							
							if (lastLevelRecorded == null) {
								gdldao.insert(awardedLevels.get(0));
								lastLevelRecorded = awardedLevels.get(0);
							}
						}

						// Add new awarded levels to a list
						while (i < awardedLevels.size() && !awardedLevels.get(i).equals(lastLevelRecorded)) {
							newAwardedLevels.add(awardedLevels.get(i));
							i++;
						}
						
						List<GDLevel> levelsAlreadyInDB = gdldao.findAll();
						newAwardedLevels.removeIf(level -> levelsAlreadyInDB.contains(level));
						
						if (checkForStateChange(awardedLevels.get(0)))
							updateObservers(new LastAwardedStateChangedGDEvent(awardedLevels.get(0)));

						if (!awardedLevels.contains(lastLevelRecorded)) {
							if (!quickLevelSearch(lastLevelRecorded.getId() + "").isAwarded())
								updateObservers(new LastAwardedDeletedGDEvent(lastLevelRecorded));
							else {
								gdldao.delete(lastLevelRecorded);
								lastLevelRecorded = awardedLevels.get(0);
							}
						}
						else if (!newAwardedLevels.isEmpty())
							updateObservers(new NewAwardedGDEvent(newAwardedLevels));
						
						lastLevelRecorded = awardedLevels.get(0);

					} catch (RawDataMalformedException e) {
						e.printStackTrace();
						System.err.println(awardedLevelsRD);
						lastLevelRecorded = null;
					}
				} catch (IOException e) {
					AppTools.sendDebugPMToSuperadmin("Unable to communicate with Geometry Dash servers: `"
							+ e.getLocalizedMessage() + "`");
				} catch (Exception e) {
					AppTools.sendDebugPMToSuperadmin(
							"An internal error occured when trying to fetch Awarded levels from GD Servers: `"
									+ e.getLocalizedMessage() + "`");
					e.printStackTrace();
				}
			} else
				System.out.println("Client not ready, trying again in " + REQUEST_COOLDOWN_SECONDS + " seconds...");
			try {
				Thread.sleep(REQUEST_COOLDOWN_SECONDS * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
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
	
	private GDLevel quickLevelSearch(String levelNameOrID) throws IOException, RawDataMalformedException {
		return GDLevelFactory.buildGDLevelFirstSearchResult(GDServer.fetchLevelByNameOrID(levelNameOrID));
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
