package ultimategdbot.gdevents.handler;

import java.util.function.Consumer;

import ultimategdbot.gdevents.GDEvent;

/**
 * Handles a Geometry Dash event. You can define the action to perform either by
 * providing a {@link Consumer} instance or by overriding the
 * <code>handle</code> method in a nested class.
 * 
 * @author Alex1304
 *
 * @param <T>
 *            - The type of GD Event this listener should handle.
 */
public class GDEventHandler<T extends GDEvent> {

	/**
	 * The action to perform when the event associated with this listener is
	 * dispatched. Can be null if the handle method is overriden in a nested
	 * class.
	 */
	private Consumer<T> handleAction;

	/**
	 * Constructs a new {@link GDEventHandler} and initializes the consumer
	 * action to null.
	 */
	public GDEventHandler() {
		this.handleAction = null;
	}

	/**
	 * Constructs a new {@link GDEventHandler} and initializes the consumer
	 * action to whatever is provided as parameter.
	 * 
	 * @param action
	 *            - the action to perform when the event associated with this
	 *            listener is dispatched.
	 */
	public GDEventHandler(Consumer<T> action) {
		this.handleAction = action;
	}

	/**
	 * This method can be given any instance of {@link GDEvent}. If the argument
	 * type is incompatible with the type of event this object is supposed to
	 * handle, this method will return and do nothing. Otherwise it will invoke
	 * the proper method to perform the event handling action. If you didn't
	 * provide a {@link Consumer} when constructing this object, then the
	 * <code>handle</code> method of this object will be called instead.
	 * 
	 * @param event
	 *            - the event to handle.
	 */
	@SuppressWarnings("unchecked")
	public final void handleIfCorrectEventType(GDEvent event) {
		try {
			if (handleAction != null)
				handleAction.accept((T) event);
			else
				handle((T) event);
		} catch (ClassCastException e) {
			return;
		}
	}

	/**
	 * This method is invoked only if this object was created with a null
	 * {@link Consumer} instance. Performs the action to handle the event given
	 * as parameter.
	 * 
	 * @param event
	 *            - the event to handle.
	 */
	public void handle(T event) {
		return;
	}
}