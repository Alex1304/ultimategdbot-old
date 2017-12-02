package ultimategdbot.util;

import ultimategdbot.exceptions.ThreadKilledException;

/**
 * A custom Thread implementation for this app. It does the same as regular Thread instances,
 * but this class allows simulating kill signals. Basically, a boolean value used as a flag is
 * first initialized to false, and as long as this value stays at false, the thread will continue
 * to run normally. If the value changes to true by the use of the {@link KillableThread#kill()}
 * method, the thread will be able to detect it and do an action consequently, generally it will
 * consist on stopping the thread execution.
 * 
 * @see {@link KillableRunnable}
 * 
 * @author Alex1304
 *
 */
public class KillableThread extends Thread implements KillableRunnable {
	
	/**
	 * Boolean flag that tells whether the thread is marked as killed
	 */
	private boolean killed = false;
	
	/**
	 * A customized version of {@link Runnable}
	 */
	private KillableRunnable target = null;
	
	/**
	 * @see {@link Thread#Thread()}
	 */
	public KillableThread() {
		super();
	}

	/**
	 * @see {@link Thread#Thread(Runnable, String)}
	 */
	public KillableThread(KillableRunnable target, String name) {
		super(name);
		this.target = target;
	}

	/**
	 * @see {@link Thread#Thread(Runnable)}
	 */
	public KillableThread(KillableRunnable target) {
		this.target = target;
	}
	
	/**
	 * @see {@link Thread#Thread(String)}
	 */
	public KillableThread(String name) {
		super(name);
	}

	/**
	 * @see {@link Thread#Thread(ThreadGroup, Runnable, String, long)}
	 */
	public KillableThread(ThreadGroup group, KillableRunnable target, String name, long stackSize) {
		super(group, null, name, stackSize);
		this.target = target;
	}

	/**
	 * @see {@link Thread#Thread(ThreadGroup, Runnable, String)}
	 */
	public KillableThread(ThreadGroup group, KillableRunnable target, String name) {
		super(group, name);
		this.target = target;
	}

	/**
	 * @see {@link Thread#Thread(ThreadGroup, Runnable)}
	 */
	public KillableThread(ThreadGroup group, KillableRunnable target) {
		super(group, (Runnable) null);
		this.target = target;
	}

	/**
	 * @see {@link Thread#Thread(ThreadGroup, String)}
	 */
	public KillableThread(ThreadGroup group, String name) {
		super(group, name);
	}
	
	/**
	 * Whether the thread is marked as killed.
	 * 
	 * @return boolean
	 */
	public boolean isKilled() {
		return killed;
	}
	
	/**
	 * Does nothing if the killed flag is at false.
	 * 
	 * @throws ThreadKilledException if {@link KillableThread#isKilled()} returns true
	 */
	public void checkKilledState() throws ThreadKilledException {
		if (isKilled())
			throw new ThreadKilledException();
	}
	
	/**
	 * This method is final, please override {@link KillableThread#run(KillableThread)} instead.
	 * 
	 * @see {@link Thread#run()}
	 */
	@Override
	public final void run() {
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
	
	/**
	 * Changes the killed flag to true.
	 */
	public void kill() {
		killed = true;
		this.interrupt();
	}

}
