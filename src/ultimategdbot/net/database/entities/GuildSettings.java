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
import ultimategdbot.guildsettings.GuildSetting;
import ultimategdbot.guildsettings.RoleAwardedLevelsSetting;
import ultimategdbot.guildsettings.RoleGdModeratorsSetting;
import ultimategdbot.guildsettings.TagEveryoneOnBotAnnouncementSetting;

public class GuildSettings implements Iterable<GuildSetting<?>> {
	
	private Map<Class<? extends GuildSetting<?>>, GuildSetting<?>> settings;
	private IGuild guild;
	
	public GuildSettings(IGuild guild) {
		this(guild, null, null, null, null, null, null);
	}

	public GuildSettings(long guildID) {
		this(Main.DISCORD_ENV.getClient().getGuildByID(guildID));
	}

	public GuildSettings(IGuild guild, IChannel channelAwardedLevels, IChannel channelGDModerators,
			IChannel channelBotAnnouncements, IRole roleAwardedLevels, IRole roleGDModerators,
			Boolean tagEveryoneOnBotAnnouncement) {
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
	}

	public IGuild getGuild() {
		return guild;
	}

	public void setGuild(IGuild guild) {
		this.guild = guild;
	}

	public void setGuild(long guildID) {
		this.guild = Main.DISCORD_ENV.getClient().getGuildByID(guildID);
	}
	
	@SuppressWarnings("unchecked")
	public <T> GuildSetting<T> getSetting(Class<? extends GuildSetting<T>> settingClass) {
		return (GuildSetting<T>) settings.get(settingClass);
	}
	
	public long getLongIDForIDLinkedObjectSetting(Class<? extends GuildSetting<? extends IIDLinkedObject>> settingClass) {
		GuildSetting<? extends IIDLinkedObject> setting = getSetting(settingClass);
		return setting.getValue() != null ? setting.getValue().getLongID() : 0;
	}

	@Override
	public Iterator<GuildSetting<?>> iterator() {
		return settings.values().iterator();
	}
}
