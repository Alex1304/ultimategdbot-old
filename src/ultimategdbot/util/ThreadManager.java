package ultimategdbot.util;

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * Facilitates threads management
 * 
 * @author Alex1304
 *
 */
public class ThreadManager {
	
	private Map<String, Thread> threadMap = new ConcurrentHashMap<>();

	/**
	 * Adds an implementation of Runnable into a map which key is the given name
	 * it will be affected to a thread ready to start.
	 * 
	 * @param name
	 *            - the name of the thread that will be associated to it
	 * @param r
	 *            - the runnable to add
	 */
	public void addThread(String name, Runnable r) {
		if (!threadMap.containsKey(name))
			threadMap.put(name, new Thread(r));
	}
	
	/**
	 * Adds an existing thread to the map and associates it with the given name.
	 *
	 * @param name
	 *            - the name of the thread that will be associated to it
	 * @param t
	 *            - the thread to add
	 */
	public void addExistingThread(String name, Thread t) {
		if (!threadMap.containsKey(name))
			threadMap.put(name, t);
	}

	public Thread getThread(String name) {
		return threadMap.get(name);
	}

	/**
	 * Starts all new threads.
	 * 
	 * @return the number of threads started
	 */
	public int startAllNew() {
		return startAllNew(name -> true);
	}

	/**
	 * Starts all new threads which name verifies the given predicate.
	 * 
	 * @return the number of threads started
	 */
	public int startAllNew(Predicate<String> filter) {
		int count = 0;
		for (Entry<String, Thread> t : threadMap.entrySet())
			if (filter.test(t.getKey()) && t.getValue().getState() == State.NEW) {
				t.getValue().start();
				count++;
			}

		return count;
	}
	
	/**
	 * Starts the thread with specified name if and only if its state is NEW
	 * 
	 * @return the number of threads started
	 */
	public void startIfNew(String name) {
		if (threadMap.containsKey(name) && threadMap.get(name).getState() == State.NEW)
			threadMap.get(name).start();
	}

	/**
	 * Kills/interrupts the thread associated with the given name. If no such
	 * thread exists for this name, no operation will be performed
	 * 
	 * @param name
	 *            - name of target thread
	 */
	public void killThread(String name) {
		if (threadMap.containsKey(name))
			threadMap.remove(name).interrupt();
		else
			System.out.println("bruh");
	}
	
	/**
	 * Restarts a thread by killing it first, then instantiate a new Thread using
	 * the same Runnable instance.
	 * 
	 * @param name
	 *            - name of target thread
	 */
	public void restartThread(String name) {
		killThread(name);
		Runnable r = threadMap.remove(name);
		addThread(name, r);
	}
	
	/**
	 * All terminated threads will be removed from the map.
	 */
	public void removeAllTerminated() {
		removeAllTerminated(name -> true);
	}
	
	/**
	 * All terminated threads which name verifies the given predicate will be
	 * removed from the map.
	 */
	public void removeAllTerminated(Predicate<String> filter) {
		for (Entry<String, Thread> t : threadMap.entrySet())
			if (filter.test(t.getKey()) && t.getValue().getState() == State.TERMINATED)
				threadMap.remove(t.getKey());
	}
	
	/**
	 * The thread with specified name will be removed from the map if and only if
	 * it's terminated.
	 * 
	 * @param name
	 * 			- name of target thread
	 */
	public void removeIfTerminated(String name) {
		if (threadMap.containsKey(name) && threadMap.get(name).getState() == State.TERMINATED)
			threadMap.remove(name);
	}
	
	/**
	 * Returns a collection of threads which name verifies the given predicate.
	 * 
	 * @param filter
	 *            - the predicate to test
	 * @return collection of threads
	 */
	public Collection<Thread> getThreadsFilteredByName(Predicate<String> filter) {
		List<Thread> result = new ArrayList<>();
		for (Entry<String, Thread> t : threadMap.entrySet())
			if (filter.test(t.getKey()))
				result.add(t.getValue());
		
		return result;
	}

	public Set<String> nameSet() {
		return threadMap.keySet();
	}
}