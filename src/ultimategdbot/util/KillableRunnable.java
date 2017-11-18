package ultimategdbot.util;

import ultimategdbot.exceptions.ThreadKilledException;

public interface KillableRunnable {
	
	void run(KillableThread thisThread) throws ThreadKilledException;
}
