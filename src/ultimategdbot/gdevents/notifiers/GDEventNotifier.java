package ultimategdbot.gdevents.notifiers;

import java.util.List;

import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import ultimategdbot.app.Main;
import ultimategdbot.guildsettings.ChannelSetting;
import ultimategdbot.guildsettings.RoleSetting;
import ultimategdbot.net.database.dao.impl.DAOFactory;
import ultimategdbot.net.database.entities.GuildSettings;
import ultimategdbot.net.database.entities.UserSettings;
import ultimategdbot.net.geometrydash.GDUser;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.GuildMessageBroadcaster;

/**
 * Class that sends notification to all subscribed guilds about a Geometry Dash
 * event.
 * 
 * @author Alex1304
 */
public abstract class GDEventNotifier {
	
	private Class<? extends ChannelSetting> channelSettingType;
	private Class<? extends RoleSetting> roleSettingType;
	private List<GuildSettings> gsList;
	protected String message;
	protected EmbedObject embed;
	
	/**
	 * Constructor initializing all fields.
	 * 
	 * @param channelSettingType - channel setting class necessary to get the desired channel
	 * @param roleSettingType - role setting class necessary to get the desired role
	 * @param gsList - the list of guilds that should be notified
	 */
	public GDEventNotifier(Class<? extends ChannelSetting> channelSettingType,
			Class<? extends RoleSetting> roleSettingType, List<GuildSettings> gsList) {
		this.channelSettingType = channelSettingType;
		this.roleSettingType = roleSettingType;
		this.gsList = gsList;
		this.message = null;
		this.embed = null;
	}
	
	/**
	 * Sends the notification message to all guilds
	 * 
	 * @return the List of successfully sent messages
	 */
	public List<IMessage> notifySubscribers() {
		initMessageAndEmbedIfNull();
		GuildMessageBroadcaster gmb = new GuildMessageBroadcaster(gsList, 
				gs -> gs.getSetting(channelSettingType).getValue(),
				gs -> {
					IRole role = gs.getSetting(roleSettingType).getValue();
					return (role != null ? role.mention() + " " : "")
							+ message;
				},
				gs -> embed
		);
		
		gmb.broadcast();
		
		onNotifyDone();
		
		return gmb.getResult();
	}
	
	private void initMessageAndEmbedIfNull() {
		if (message == null)
			this.message = buildMessage();
		if (embed == null)
			this.embed = buildEmbed();
	}
	
	/**
	 * Builds the notification message content
	 * 
	 * @return a String
	 */
	public abstract String buildMessage();
	
	/**
	 * Builds the embed to send with the notification message
	 * 
	 * @return an EmbedObject
	 */
	public abstract EmbedObject buildEmbed();
	
	/**
	 * Sends a PM to concerned user about the event
	 */
	protected void notifyConcernedUserIfLinked(GDUser user, String message) {
		initMessageAndEmbedIfNull();
		if (user != null) {
			UserSettings us = DAOFactory.getUserSettingsDAO().findByGDUserID(user.getAccountID());
			if (us != null) {
				IUser discordUser = Main.DISCORD_ENV.getClient().fetchUser(us.getUserID());
				if (discordUser != null) {
					AppTools.sendMessage(Main.DISCORD_ENV.getClient().getOrCreatePMChannel(discordUser),
							message, embed);
				}
			}
		}
	}
	
	/**
	 * Called after notifySubscribers() is done.
	 * Implementing this is optional, does nothing by default.
	 */
	public void onNotifyDone() {
		return;
	}
}
