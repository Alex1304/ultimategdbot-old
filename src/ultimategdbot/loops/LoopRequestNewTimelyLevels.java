package ultimategdbot.loops;

import java.io.IOException;

import ultimategdbot.app.Main;
import ultimategdbot.exceptions.RawDataMalformedException;
import ultimategdbot.exceptions.ThreadKilledException;
import ultimategdbot.gdevents.levels.NewDailyLevelGDEvent;
import ultimategdbot.gdevents.levels.NewWeeklyDemonGDEvent;
import ultimategdbot.net.database.dao.impl.DAOFactory;
import ultimategdbot.net.database.dao.impl.TimelyLevelDAO;
import ultimategdbot.net.geometrydash.GDLevel;
import ultimategdbot.net.geometrydash.GDLevelFactory;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.GDUtils;
import ultimategdbot.util.KillableRunnable;
import ultimategdbot.util.KillableThread;

/**
 * Checks regularly if there are new Daily and Weekly levels
 * @author alexandre
 *
 */
public class LoopRequestNewTimelyLevels implements KillableRunnable {
	
	private static final int REQUEST_COOLDOWN_SECONDS = 60;
	private int lastSeenDailyID = Integer.MAX_VALUE;
	private int lastSeenWeeklyID = Integer.MAX_VALUE;

	@Override
	public void run(KillableThread thisThread) throws ThreadKilledException {
		while (!thisThread.isKilled()) {
			if (Main.DISCORD_ENV.getClient().isReady()) {
				try {
					TimelyLevelDAO tldao = DAOFactory.getTimelyLevelDAO();
					if (lastSeenDailyID == Integer.MAX_VALUE || lastSeenWeeklyID == Integer.MAX_VALUE) {
						lastSeenDailyID = tldao.findLastTimely(true).intValue();
						lastSeenWeeklyID = tldao.findLastTimely(false).intValue();
						
						if (lastSeenDailyID == -1)
							lastSeenDailyID = Integer.MAX_VALUE;
						if (lastSeenWeeklyID == -1)
							lastSeenWeeklyID = Integer.MAX_VALUE;
					}
					
					int dailyID = GDUtils.fetchCurrentTimelyID(true);
					int weeklyID = GDUtils.fetchCurrentTimelyID(false) + 100000;
					
					if (dailyID > lastSeenDailyID) {
						GDLevel daily = GDLevelFactory.buildTimelyLevel(true);
						Main.GD_EVENT_DISPATCHER.dispatch(new NewDailyLevelGDEvent(daily));
						tldao.insert((long) dailyID);
					}
					
					if (weeklyID > lastSeenWeeklyID) {
						GDLevel weekly = GDLevelFactory.buildTimelyLevel(false);
						Main.GD_EVENT_DISPATCHER.dispatch(new NewWeeklyDemonGDEvent(weekly));
						tldao.insert((long) weeklyID);
					}
					
					lastSeenWeeklyID = weeklyID;
					lastSeenDailyID = dailyID;
				} catch (IOException e) {
					AppTools.sendDebugPMToSuperadmin("Unable to communicate with Geometry Dash servers: `"
							+ e.getLocalizedMessage() + "`");
				} catch (ArrayIndexOutOfBoundsException | RawDataMalformedException e) {
					e.printStackTrace();
					lastSeenDailyID = Integer.MAX_VALUE;
					lastSeenWeeklyID = Integer.MAX_VALUE;
				} catch (Exception e) {
					AppTools.sendDebugPMToSuperadmin(
							"An internal error occured when trying to fetch Timely levels from GD Servers: `"
									+ e.getLocalizedMessage() + "`");
					e.printStackTrace();
					thisThread.kill();
				}
			} else
				System.out.println(thisThread.getName() + ": Client not ready, trying again in " + REQUEST_COOLDOWN_SECONDS + " seconds...");
			try {
				Thread.sleep(REQUEST_COOLDOWN_SECONDS * 1000);
			} catch (InterruptedException e) {
			}
		}
	}

}
