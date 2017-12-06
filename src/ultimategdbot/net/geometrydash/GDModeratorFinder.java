package ultimategdbot.net.geometrydash;

import java.io.IOException;
import java.util.function.Consumer;

import ultimategdbot.commands.impl.UpdateModListCommand;
import ultimategdbot.exceptions.RawDataMalformedException;
import ultimategdbot.util.KillableThread;

/**
 * Scans user profiles in the specified range of accountIDs and performs an
 * action when a moderator is found.
 * 
 * @author Alex1304
 *
 */
public class GDModeratorFinder extends KillableThread {
	
	/**
	 * Range beginning bound of accountIDs
	 */
	private long beginAccountID;
	
	/**
	 * Range end bound of accountIDs
	 */
	private long endAccountID;
	
	/**
	 * Action to perform when a moderator is found
	 */
	private Consumer<GDUser> actionOnModeratorFound;
	
	/**
	 * Max attempt count to fetch a user profile again if it fails.
	 */
	private static final int NB_MAX_ATTEMPTS = 3;
	
	/**
	 * Constructs a new instance with all fields provided?
	 * 
	 * @param beginAccountID
	 *            - range beginning bound of accountIDs
	 * @param endAccountID
	 *            - Range end bound of accountIDs
	 * @param actionOnModeratorFound
	 *            - action to perform when a moderator is found
	 */
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
	
	/**
	 * Fetches one user profile. Attempts to fetch it again NB_AX_ATTEMPTS times if failed.
	 * 
	 * @param accountID - The accountID of the profile to fetch
	 * @return the GDUser instance corresponding to the fetched profile.
	 */
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
