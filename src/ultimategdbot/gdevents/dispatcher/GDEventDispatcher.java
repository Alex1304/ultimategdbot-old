package ultimategdbot.gdevents.dispatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ultimategdbot.gdevents.GDEvent;
import ultimategdbot.gdevents.handler.GDEventHandler;

/**
 * Dispatches Geometry Dash events. It provides methods to add/remove event
 * listeners
 * 
 * @author Alex1304
 *
 */
public class GDEventDispatcher {

	/**
	 * The list of event listeners
	 */
	private List<GDEventHandler<? extends GDEvent>> listeners = new ArrayList<>();

	/**
	 * Adds a listener to the listener list.
	 * 
	 * @param listener
	 *            - the listener to add
	 */
	public void addListener(GDEventHandler<? extends GDEvent> listener) {
		this.listeners.add(listener);
	}
	
	/**
	 * Adds an entire collection of listeners to the listener list.
	 * 
	 * @param listeners
	 *            - the listeners to add
	 */
	public void addAllListeners(Collection<GDEventHandler<? extends GDEvent>> listeners) {
		this.listeners.addAll(listeners);
	}

	/**
	 * Removes a listener from the listener list.
	 * 
	 * @param listener
	 *            - the listener to remove
	 */
	public void removeListener(GDEventHandler<? extends GDEvent> listener) {
		this.listeners.remove(listener);
	}

	/**
	 * Removes all listener from the listener list.
	 */
	public void clearListeners() {
		this.listeners.clear();
	}

	/**
	 * Dispatches a Geometry Dash event. It will call the handle method of all
	 * listeners present in the list.
	 * 
	 * @param event
	 *            - the event to dispatch
	 */
	public void dispatch(GDEvent event) {
		for (GDEventHandler<? extends GDEvent> listener : this.listeners)
			listener.handleIfCorrectEventType(event);
	}
}
