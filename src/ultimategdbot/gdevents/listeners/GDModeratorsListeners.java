package ultimategdbot.gdevents.listeners;

import java.util.ArrayList;
import java.util.List;

import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import ultimategdbot.gdevents.GDEvent;
import ultimategdbot.gdevents.handler.GDEventHandler;
import ultimategdbot.gdevents.users.UserModdedGDEvent;
import ultimategdbot.gdevents.users.UserUnmoddedGDEvent;
import ultimategdbot.guildsettings.ChannelGdModeratorsSetting;
import ultimategdbot.guildsettings.RoleGdModeratorsSetting;
import ultimategdbot.net.database.dao.GuildSettingsDAO;
import ultimategdbot.net.database.entities.GuildSettings;
import ultimategdbot.net.geometrydash.GDUser;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.GDUtils;

/**
 * Utility class to manage event listeners related to GD Moderators.
 * 
 * @author Alex1304
 *
 */
public abstract class GDModeratorsListeners {

	/**
	 * Builds a list of listeners and returns it
	 * @return a list of GD event listeners.
	 */
	public static List<GDEventHandler<? extends GDEvent>> getListeners() {
		List<GDEventHandler<? extends GDEvent>> listeners = new ArrayList<>();
		
		listeners.add(new GDEventHandler<>((UserModdedGDEvent event) -> {
			notifySubscribers("**" + event.getUser().getName() + "** just got Geometry Dash moderator !!!",
					event.getUser(), userModdedEmbed(event.getUser()));
		}));
		
		listeners.add(new GDEventHandler<>((UserUnmoddedGDEvent event) -> {
			notifySubscribers("**" + event.getUser().getName() + "** just lost Geometry Dash moderator...",
					event.getUser(), userUnmoddedEmbed(event.getUser()));
		}));
		
		return listeners;
	}
	
	/**
	 * Sends a message to all guilds in the channel specified in the "channel
	 * gd moderators" guild setting.
	 * 
	 * @param message
	 *            - the message to send
	 * @param level
	 *            - the newly awarded level it's supposed to announce
	 * @param levelEmbed
	 *            - the embed containing level info
	 */
	private static void notifySubscribers(String message, GDUser user, EmbedObject embed) {
		List<GuildSettings> guilds = new GuildSettingsDAO().findAll();
		
		guilds.forEach(gs -> {
			IGuild guild = gs.getGuild();
			
			if (guild != null) {
				IChannel channelGDModerators = gs.getSetting(ChannelGdModeratorsSetting.class).getValue();
				IRole roleGDModerators = gs.getSetting(RoleGdModeratorsSetting.class).getValue();
				
				if (channelGDModerators != null) {
					AppTools.sendMessage(channelGDModerators,
							(roleGDModerators != null ? roleGDModerators.mention() + " " : "") + message,
							embed);
				}
				
			} else {
				System.err.println("[INFO] Guild deleted");
				new GuildSettingsDAO().delete(gs);
			}
		});
	}
	
	/**
	 * Constructs the announcement embed for the new moderator
	 * 
	 * @param user - the modded user
	 * @return the embed that will be sent to guilds
	 */
	private static EmbedObject userModdedEmbed(GDUser user) {
		return GDUtils.buildEmbedForGDUser("User modded!", "https://i.imgur.com/zY61GDD.png", user);
	}
	
	/**
	 * Constructs the announcement embed for the ex-moderator
	 * 
	 * @param user - the unmodded user
	 * @return the embed that will be sent to guilds
	 */
	private static EmbedObject userUnmoddedEmbed(GDUser user) {
		return GDUtils.buildEmbedForGDUser("User unmodded...", "https://i.imgur.com/X53HV7d.png", user);
	}
}
