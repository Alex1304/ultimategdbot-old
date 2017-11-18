package ultimategdbot.util;

import ultimategdbot.exceptions.ThreadKilledException;

public class KillableThread extends Thread implements KillableRunnable {

	private boolean killed = false;
	private KillableRunnable target = null;
	
	public KillableThread() {
		super();
	}

	public KillableThread(KillableRunnable target, String name) {
		super(name);
		this.target = target;
	}

	public KillableThread(KillableRunnable target) {
		this.target = target;
	}

	public KillableThread(String name) {
		super(name);
	}

	public KillableThread(ThreadGroup group, KillableRunnable target, String name, long stackSize) {
		super(group, null, name, stackSize);
		this.target = target;
	}

	public KillableThread(ThreadGroup group, KillableRunnable target, String name) {
		super(group, name);
		this.target = target;
	}

	public KillableThread(ThreadGroup group, KillableRunnable target) {
		super(group, (Runnable) null);
		this.target = target;
	}

	public KillableThread(ThreadGroup group, String name) {
		super(group, name);
	}

	public boolean isKilled() {
		return killed;
	}
	
	public void checkKilledState() throws ThreadKilledException {
		if (killed)
			throw new ThreadKilledException();
	}
	
	@Override
	public void run() {
		try {
			run(this);
			checkKilledState();
		} catch (ThreadKilledException e) {
			System.err.println("Thread " + getName() + " has been killed.");
		}
	}
	
	@Override
	public void run(KillableThread thisThread) throws ThreadKilledException {
		if (target == null)
			return;
		
		target.run(thisThread);
	}
	
	public void kill() {
		killed = true;
		this.interrupt();
	}

}
