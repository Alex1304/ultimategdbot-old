package ultimategdbot.net.geometrydash;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ultimategdbot.commands.impl.UpdateModListCommand;
import ultimategdbot.exceptions.RawDataMalformedException;

public class GDModeratorFinder extends Thread {
	
	private long beginAccountID;
	private long endAccountID;
	private Consumer<GDUser> actionOnModeratorFound;
	private static final int NB_MAX_ATTEMPTS = 3;
	private boolean stopped = false;
	
	public GDModeratorFinder(long beginAccountID, long endAccountID, Consumer<GDUser> actionOnModeratorFound) {
		this.beginAccountID = beginAccountID;
		this.endAccountID = endAccountID;
		this.actionOnModeratorFound = actionOnModeratorFound;
	}

	@Override
	public void run() {
		for (long i = beginAccountID ; !stopped && i <= endAccountID ; i++) {
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
	
	public static List<GDUser> filterUsersByRole(List<GDUser> targetList, GDRole role) {
		List<GDUser> res = new ArrayList<>();
		
		res.forEach(user -> {
			if (user.getRole() == role)
				res.add(user);
		});
		
		return res;
	}
	
	public void stopThread() {
		stopped = true;
	}

}
