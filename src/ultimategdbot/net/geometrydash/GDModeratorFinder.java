package ultimategdbot.net.geometrydash;

import java.io.IOException;
import java.util.function.Consumer;

import ultimategdbot.commands.impl.UpdateModListCommand;
import ultimategdbot.exceptions.RawDataMalformedException;
import ultimategdbot.util.KillableThread;

/**
 * This class does the 
 * 
 * @author alexandre
 *
 */
public class GDModeratorFinder extends KillableThread {
	
	private long beginAccountID;
	private long endAccountID;
	private Consumer<GDUser> actionOnModeratorFound;
	private static final int NB_MAX_ATTEMPTS = 3;
	
	public GDModeratorFinder(long beginAccountID, long endAccountID, Consumer<GDUser> actionOnModeratorFound) {
		this.beginAccountID = beginAccountID;
		this.endAccountID = endAccountID;
		this.actionOnModeratorFound = actionOnModeratorFound;
	}

	@Override
	public void run(KillableThread thisThread) {
		for (long i = beginAccountID ; !thisThread.isKilled() && i <= endAccountID ; i++) {
			GDUser user = fetchOneUser(i);
			if (user != null && user.getRole() != GDRole.USER)
				actionOnModeratorFound.accept(user);
			UpdateModListCommand.incrementAndShowProcessedUsers();
		}
	}
	
	private GDUser fetchOneUser(long accountID) {
		GDUser result = null;
		
		int attempt = 0;
		while (result == null && attempt < NB_MAX_ATTEMPTS) {
			try {
				result = GDUserFactory.buildGDUserFromProfileRawData(GDServer.fetchUserProfile(accountID));
			} catch (RawDataMalformedException e) {
				return null;
			} catch (IOException e) {
				attempt++;
				if (attempt < NB_MAX_ATTEMPTS)
					try {
						Thread.sleep(200);
					} catch (InterruptedException e1) {
					}
			}
		}
		
		return result;
	}
}
