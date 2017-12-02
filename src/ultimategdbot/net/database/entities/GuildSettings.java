package ultimategdbot.net.database.entities;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IIDLinkedObject;
import sx.blah.discord.handle.obj.IRole;
import ultimategdbot.app.Main;
import ultimategdbot.guildsettings.ChannelAwardedLevelsSetting;
import ultimategdbot.guildsettings.ChannelBotAnnouncementsSetting;
import ultimategdbot.guildsettings.ChannelGdModeratorsSetting;
import ultimategdbot.guildsettings.ChannelTimelyLevelsSetting;
import ultimategdbot.guildsettings.GuildSetting;
import ultimategdbot.guildsettings.RoleAwardedLevelsSetting;
import ultimategdbot.guildsettings.RoleGdModeratorsSetting;
import ultimategdbot.guildsettings.RoleTimelyLevelsSetting;
import ultimategdbot.guildsettings.TagEveryoneOnBotAnnouncementSetting;

/**
 * This entity represents the bot settings for one specific guild.
 * It implements Iterable so you can read through the guild settings
 * one by one using a forEach.
 * 
 * @author Alex1304
 *
 */
public class GuildSettings implements Iterable<GuildSetting<?>> {
	
	/**
	 * Map of GuildSetting instances, mapped by their Class.
	 */
	private Map<Class<? extends GuildSetting<?>>, GuildSetting<?>> settings;
	
	/**
	 * The guild attached to the settings.
	 */
	private IGuild guild;
	
	/**
	 * Constructs the entity specifying a guild, and initializes all setting values
	 * to default values (using null pointers).
	 * 
	 * @param guild - the guild which settings are attached to
	 */
	public GuildSettings(IGuild guild) {
		this(guild, null, null, null, null, null, null, null, null);
	}

	/**
	 * Constructs the entity specifying a guild ID, and initializes all setting values
	 * to default values (using null pointers). The actual guild instance will be fetched
	 * by the client.
	 * 
	 * @param guild - the guild which settings are attached to
	 */
	public GuildSettings(long guildID) {
		this(Main.DISCORD_ENV.getClient().getGuildByID(guildID));
	}
	
	/**
	 * Constructs the entity for the specified guild, but with values for every
	 * single setting. The signature of this constructor should be updated each
	 * time a setting is added / edited / removed
	 * 
	 * @param guild - the guild which settings are attached to
	 * @param channelAwardedLevels - value for {@link ChannelAwardedLevelsSetting}
	 * @param channelGDModerators - value for {@link ChannelGDModeratorsSetting}
	 * @param channelBotAnnouncements - value for {@link ChannelBotAnnouncementsSetting}
	 * @param roleAwardedLevels - value for {@link RoleAwardedLevelsSetting}
	 * @param roleGDModerators - value for {@link RoleGdModeratorsSetting}
	 * @param tagEveryoneOnBotAnnouncement - value for {@link TagEveryoneOnBotAnnouncementSetting}
	 * @param channelTimelyLevels - value for {@link ChannelTimelyLevelsSetting}
	 * @param roleTimelyLevels - value for {@link RoleTimelyLevelsSetting}
	 */
	public GuildSettings(IGuild guild, IChannel channelAwardedLevels, IChannel channelGDModerators,
			IChannel channelBotAnnouncements, IRole roleAwardedLevels, IRole roleGDModerators,
			Boolean tagEveryoneOnBotAnnouncement, IChannel channelTimelyLevels, IRole roleTimelyLevels) {
		this.settings = new TreeMap<>((o1, o2) -> o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase()));
		this.guild = guild;
		
		if (guild == null)
			throw new NullPointerException("Guild cannot be null.");
		
		settings.put(ChannelAwardedLevelsSetting.class, new ChannelAwardedLevelsSetting(guild, channelAwardedLevels));
		settings.put(ChannelGdModeratorsSetting.class, new ChannelGdModeratorsSetting(guild, channelGDModerators));
		settings.put(ChannelBotAnnouncementsSetting.class, new ChannelBotAnnouncementsSetting(guild, channelBotAnnouncements));
		settings.put(RoleAwardedLevelsSetting.class, new RoleAwardedLevelsSetting(guild, roleAwardedLevels));
		settings.put(RoleGdModeratorsSetting.class, new RoleGdModeratorsSetting(guild, roleGDModerators));
		settings.put(TagEveryoneOnBotAnnouncementSetting.class, new TagEveryoneOnBotAnnouncementSetting(guild, tagEveryoneOnBotAnnouncement));
		settings.put(ChannelTimelyLevelsSetting.class, new ChannelTimelyLevelsSetting(guild, channelTimelyLevels));
		settings.put(RoleTimelyLevelsSetting.class, new RoleTimelyLevelsSetting(guild, roleTimelyLevels));
	}
	
	/**
	 * Gets the guild attached to the settings.
	 * 
	 * @return guild
	 */
	public IGuild getGuild() {
		return guild;
	}
	
	/**
	 * Sets the guild attached to the settings.
	 * 
	 * @param guild - the guild to set
	 */
	public void setGuild(IGuild guild) {
		this.guild = guild;
	}
	
	/**
	 * Sets the guild by its ID. The instance is fetched by the client.
	 * 
	 * @param guildID - the ID of the guild to set
	 */
	public void setGuild(long guildID) {
		this.guild = Main.DISCORD_ENV.getClient().getGuildByID(guildID);
	}
	
	/**
	 * Gets a {@link GuildSetting} instance using its class. A parametrized type
	 * is used so the type returned will match the type represented by the given class.
	 * Unless the setting map was weirdly declared, the ClassCastException should never be
	 * thrown, that's why there is a SuppressWarnings unchecked on this method.
	 * 
	 * @param settingClass - the class of the setting to get
	 * @return an instance of {@link GuildSetting} representing the desired setting.
	 */
	@SuppressWarnings("unchecked")
	public <T extends GuildSetting<?>> T getSetting(Class<T> settingClass) {
		return (T) settings.get(settingClass);
	}
	
	/**
	 * This method is a shortcut for settings which value is an instance of IDLinkedObject.
	 * Returns the long ID of the value. The advantage of using this method instead of using
	 * <code>getSetting(&lt;IDLinkedObject&gt;.class).getValue().getLongID()</code> is that
	 * it can avoid NullPointerExceptions in case the value is null.
	 * @param settingClass - the class of the setting to get
	 * @return the long ID of the IDLinkedObject setting value.
	 */
	public long getLongIDForIDLinkedObjectSetting(Class<? extends GuildSetting<? extends IIDLinkedObject>> settingClass) {
		GuildSetting<? extends IIDLinkedObject> setting = getSetting(settingClass);
		return setting.getValue() != null ? setting.getValue().getLongID() : 0;
	}

	@Override
	public Iterator<GuildSetting<?>> iterator() {
		return settings.values().iterator();
	}
}
