package ultimategdbot.gdevents;

import java.util.ArrayList;
import java.util.List;

import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import ultimategdbot.app.Main;
import ultimategdbot.gdevents.handler.GDEventHandler;
import ultimategdbot.gdevents.levels.LastAwardedDeletedGDEvent;
import ultimategdbot.gdevents.levels.LastAwardedStateChangedGDEvent;
import ultimategdbot.gdevents.levels.NewAwardedGDEvent;
import ultimategdbot.net.database.dao.GDLevelDAO;
import ultimategdbot.net.database.dao.GuildSettingsDAO;
import ultimategdbot.net.database.entities.GuildSettings;
import ultimategdbot.net.geometrydash.GDLevel;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.GDUtils;

/**
 * Utility class to manage event listeners related to levels inthe Awarded section
 * of Geometry Dash.
 * 
 * @author Alex1304
 *
 */
public class AwardedLevelListeners {
	
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
				notifySubscribers("A new level has just been rated on Geometry Dash!!!", level,
						newAwardedLevelEmbed(level));
				new GDLevelDAO().insert(level);
			});
		}));
		
		// Code executed when the last known level gets unrated from Geometry Dash
		listeners.add(new GDEventHandler<>((LastAwardedDeletedGDEvent event) -> {
			notifySubscribers("A level just got un-rated from Geometry Dash...", event.getLevel(),
					unratedLevelEmbed(event.getLevel()));
			new GDLevelDAO().delete(event.getLevel());
		}));

		// Code executed when the last known level experiences a state change on Geometry Dash
		listeners.add(new GDEventHandler<>((LastAwardedStateChangedGDEvent event) -> {
			for (IMessage message : notifMessageOfLastAwardedForEachGuild)
				if (message != null)
					AppTools.editMessage(message, message.getContent(), newAwardedLevelEmbed(event.getLevel()));
		}));
		
		return listeners;
	}
	
	private static void notifySubscribers(String message, GDLevel level, EmbedObject levelEmbed) {
		List<GuildSettings> gsList = new GuildSettingsDAO().findAll();
		notifMessageOfLastAwardedForEachGuild.clear();

		for (GuildSettings gs : gsList) {
			IGuild guild = Main.DISCORD_ENV.getClient().getGuildByID(gs.getGuildId());

			if (guild != null) {
				IChannel channelGDEventSub = guild.getChannelByID(gs.getGdeventSubscriberChannelId());
				IRole roleGDEventSub = guild.getRoleByID(gs.getGdeventSubscriberRoleId());
				IMessage lastMessage = null;
				if (channelGDEventSub != null) {
					try {
						// This 1 millisecond break will guarantee that messages won't be sent at the
						// same time, which can cause issues in the AppTools#sendMessage() method.
						Thread.sleep(1);
					} catch (InterruptedException e) {}
					lastMessage = AppTools.sendMessage(channelGDEventSub,
							(roleGDEventSub != null ? roleGDEventSub.mention() + " " : "")
									+ message,
							levelEmbed);
				}
				notifMessageOfLastAwardedForEachGuild.add(lastMessage);
			} else {
				System.err.println("[INFO] Guild deleted");
				new GuildSettingsDAO().delete(gs);
			}
		}
	}
	
	private static EmbedObject newAwardedLevelEmbed(GDLevel level) {
		return GDUtils.buildEmbedForGDLevel("New rated level!", "https://i.imgur.com/asoMj1W.png", level);
	}
	
	private static EmbedObject unratedLevelEmbed(GDLevel level) {
		return GDUtils.buildEmbedForGDLevel("Level unrated...", "https://i.imgur.com/fPECXUz.png", level);
	}
}
