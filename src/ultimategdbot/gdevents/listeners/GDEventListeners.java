package ultimategdbot.gdevents.listeners;

import java.util.ArrayList;
import java.util.List;

import sx.blah.discord.handle.obj.IMessage;
import ultimategdbot.gdevents.GDEvent;
import ultimategdbot.gdevents.handler.GDEventHandler;
import ultimategdbot.gdevents.levels.LastAwardedDeletedGDEvent;
import ultimategdbot.gdevents.levels.LastAwardedStateChangedGDEvent;
import ultimategdbot.gdevents.levels.NewAwardedGDEvent;
import ultimategdbot.gdevents.levels.NewDailyLevelGDEvent;
import ultimategdbot.gdevents.levels.NewWeeklyDemonGDEvent;
import ultimategdbot.gdevents.notifiers.AwardedLevelNotifier;
import ultimategdbot.gdevents.notifiers.GDModeratorNotifier;
import ultimategdbot.gdevents.notifiers.TimelyLevelNotifier;
import ultimategdbot.gdevents.users.UserModdedGDEvent;
import ultimategdbot.gdevents.users.UserUnmoddedGDEvent;
import ultimategdbot.net.database.dao.impl.DAOFactory;
import ultimategdbot.util.AppTools;

/**
 * Utility class to manage event listeners related to levels in the Awarded section
 * of Geometry Dash.
 * 
 * @author Alex1304
 *
 */
public abstract class GDEventListeners {
	
	/**
	 * Contains the notification messages sent for the latest awarded level.
	 */
	private static List<IMessage> notifMessageOfLastAwardedForEachGuild = new ArrayList<>();
	
	
	/**
	 * Builds a list of listeners and returns it
	 * @return a list of GD event listeners.
	 */
	public static List<GDEventHandler<? extends GDEvent>> getListeners() {
		List<GDEventHandler<? extends GDEvent>> listeners = new ArrayList<>();
		
		// Code executed when a new level gets rated on Geometry Dash.
		listeners.add(new GDEventHandler<>((NewAwardedGDEvent event) -> {
			event.getLevelList().forEach(level -> {
				notifMessageOfLastAwardedForEachGuild.clear();
				notifMessageOfLastAwardedForEachGuild.addAll(new AwardedLevelNotifier(level, true).notifySubscribers());
				DAOFactory.getAwardedLevelDAO().insert(level);
			});
		}));
		
		// Code executed when the last known level gets unrated from Geometry Dash
		listeners.add(new GDEventHandler<>((LastAwardedDeletedGDEvent event) -> {
			new AwardedLevelNotifier(event.getLevel(), false).notifySubscribers();
			DAOFactory.getAwardedLevelDAO().delete(event.getLevel());
		}));

		// Code executed when the last known level experiences a state change on Geometry Dash
		listeners.add(new GDEventHandler<>((LastAwardedStateChangedGDEvent event) -> {
			for (IMessage message : notifMessageOfLastAwardedForEachGuild)
				if (message != null)
					AppTools.editMessage(message, message.getContent(),
							new AwardedLevelNotifier(event.getLevel(), true).buildEmbed());
		}));
		
		listeners.add(new GDEventHandler<>((UserModdedGDEvent event) -> {
			new GDModeratorNotifier(event.getUser(), true).notifySubscribers();
		}));
		
		listeners.add(new GDEventHandler<>((UserUnmoddedGDEvent event) -> {
			new GDModeratorNotifier(event.getUser(), false).notifySubscribers();
		}));

		listeners.add(new GDEventHandler<>((NewDailyLevelGDEvent event) -> {
			new TimelyLevelNotifier(event.getLevel(), true).notifySubscribers();
		}));
		
		listeners.add(new GDEventHandler<>((NewWeeklyDemonGDEvent event) -> {
			new TimelyLevelNotifier(event.getLevel(), false).notifySubscribers();
		}));
		
		return listeners;
	}
}
