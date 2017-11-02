package ultimategdbot.util;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import ultimategdbot.app.Main;
import ultimategdbot.net.database.entities.GuildSettings;

public class GuildSettingsAsObject {
	
	private GuildSettings guildSettingsDBEntity;
	
	private IGuild thisGuild;
	private IChannel gdEventsAnnouncementChannel;
	private IRole gdEventsSubscriberRole;

	public GuildSettingsAsObject(GuildSettings gs) {
		super();
		this.guildSettingsDBEntity = gs;
		this.thisGuild = Main.client.getGuildByID(gs.getGuildId());
		
		if (thisGuild != null) {
			this.gdEventsAnnouncementChannel = thisGuild.getChannelByID(gs.getGdeventSubscriberChannelId());
			this.gdEventsSubscriberRole = thisGuild.getRoleByID(gs.getGdeventSubscriberRoleId());
		}
	}
	
	public boolean isNull() {
		return thisGuild == null;
	}

	public IRole getGdEventsSubscriberRole() {
		return gdEventsSubscriberRole;
	}

	public GuildSettings getGuildSettingsDBEntity() {
		return guildSettingsDBEntity;
	}

	public IGuild getThisGuild() {
		return thisGuild;
	}

	public IChannel getGdEventsAnnouncementChannel() {
		return gdEventsAnnouncementChannel;
	}

	public void setThisGuild(IGuild thisGuild) {
		this.thisGuild = thisGuild;
		this.guildSettingsDBEntity.setGuildId(thisGuild.getLongID());
	}

	public void setGdEventsAnnouncementChannel(IChannel gdEventsAnnouncementChannel) {
		this.gdEventsAnnouncementChannel = gdEventsAnnouncementChannel;
		this.guildSettingsDBEntity.setGdeventSubscriberChannelId(
				gdEventsAnnouncementChannel == null ? 0 : gdEventsAnnouncementChannel.getLongID());
	}

	public void setGdEventsSubscriberRole(IRole gdEventsSubscriberRole) {
		this.gdEventsSubscriberRole = gdEventsSubscriberRole;
		this.guildSettingsDBEntity.setGdeventSubscriberRoleId(
				gdEventsSubscriberRole == null ? 0 : gdEventsSubscriberRole.getLongID());
	}
}
