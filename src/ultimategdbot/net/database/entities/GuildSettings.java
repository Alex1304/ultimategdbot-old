package ultimategdbot.net.database.entities;

/**
 * Contains some information about guilds necessary for the bot to work properly
 * 
 * @author Alex1304
 *
 */
public class GuildSettings {
	
	private long guildId;
	private long gdeventSubscriberRoleId;
	private long gdeventSubscriberChannelId;
	
	public GuildSettings() {}
	
	public GuildSettings(long guildId, long gdeventSubscriberRoleId,
			long gdeventSubscriberChannelId) {
		super();
		this.guildId = guildId;
		this.gdeventSubscriberRoleId = gdeventSubscriberRoleId;
		this.gdeventSubscriberChannelId = gdeventSubscriberChannelId;
	}

	public long getGuildId() {
		return guildId;
	}

	public void setGuildId(long guildId) {
		this.guildId = guildId;
	}

	public long getGdeventSubscriberRoleId() {
		return gdeventSubscriberRoleId;
	}

	public void setGdeventSubscriberRoleId(long gdeventSubscriberRoleId) {
		this.gdeventSubscriberRoleId = gdeventSubscriberRoleId;
	}

	public long getGdeventSubscriberChannelId() {
		return gdeventSubscriberChannelId;
	}

	public void setGdeventSubscriberChannelId(long gdeventSubscriberChannelId) {
		this.gdeventSubscriberChannelId = gdeventSubscriberChannelId;
	}
	
}
