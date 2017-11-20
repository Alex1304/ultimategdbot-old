package ultimategdbot.loops;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ultimategdbot.app.Main;
import ultimategdbot.exceptions.RawDataMalformedException;
import ultimategdbot.gdevents.levels.LastAwardedDeletedGDEvent;
import ultimategdbot.gdevents.levels.LastAwardedStateChangedGDEvent;
import ultimategdbot.gdevents.levels.NewAwardedGDEvent;
import ultimategdbot.net.database.dao.GDLevelDAO;
import ultimategdbot.net.geometrydash.GDLevel;
import ultimategdbot.net.geometrydash.GDLevelFactory;
import ultimategdbot.net.geometrydash.GDServer;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.KillableRunnable;
import ultimategdbot.util.KillableThread;

/**
 * Checks regularly if new levels get rated/unrated/updated on Geometry Dash
 * by looking at the Awarded section of the game.
 * 
 * @author Alex1304
 *
 */
public class LoopRequestNewAwardedLevels implements KillableRunnable {

	private static final int REQUEST_COOLDOWN_SECONDS = 10;
	private GDLevel lastLevelRecorded;
	
	@Override
	public void run(KillableThread thisThread) {
		while (!thisThread.isKilled()) {
			if (Main.DISCORD_ENV.getClient().isReady()) {
				try {
					// First, fetch awarded levels from GD servers
					String awardedLevelsRD = Main.isTestEnvironment() ?
							GDServer.fetchMostRecentLevels() :
							GDServer.fetchNewAwardedLevels();
							
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
								lastLevelRecorded = awardedLevels.get(0);
								gdldao.insert(awardedLevels.get(0));
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
							Main.GD_EVENT_DISPATCHER.dispatch(new LastAwardedStateChangedGDEvent(awardedLevels.get(0)));

						if (!awardedLevels.contains(lastLevelRecorded)) {
							if (!quickLevelSearch(lastLevelRecorded.getId() + "").isAwarded())
								Main.GD_EVENT_DISPATCHER.dispatch(new LastAwardedDeletedGDEvent(lastLevelRecorded));
							else {
								gdldao.delete(lastLevelRecorded);
								lastLevelRecorded = awardedLevels.get(0);
							}
						}
						else if (!newAwardedLevels.isEmpty())
							Main.GD_EVENT_DISPATCHER.dispatch(new NewAwardedGDEvent(newAwardedLevels));
						
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
					thisThread.kill();
				}
			} else
				System.out.println("Client not ready, trying again in " + REQUEST_COOLDOWN_SECONDS + " seconds...");
			try {
				Thread.sleep(REQUEST_COOLDOWN_SECONDS * 1000);
			} catch (InterruptedException e) {
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
}
