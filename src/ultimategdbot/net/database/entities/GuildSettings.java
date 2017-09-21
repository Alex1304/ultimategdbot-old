package ultimategdbot.net.database.entities;

/**
 * Contains some information about guilds necessary for the bot to work properly
 * 
 * @author Alex1304
 *
 */
public class GuildSettings {
	
	private long guildId;
	private long gdeventAwardedSubscriberRoleId;
	private long gdeventAwardedSubscriberChannelId;
	
	public GuildSettings() {}
	
	public GuildSettings(long guildId, long gdeventAwardedSubscriberRoleId,
			long gdeventAwardedSubscriberChannelId) {
		super();
		this.guildId = guildId;
		this.gdeventAwardedSubscriberRoleId = gdeventAwardedSubscriberRoleId;
		this.gdeventAwardedSubscriberChannelId = gdeventAwardedSubscriberChannelId;
	}

	public long getGuildId() {
		return guildId;
	}

	public void setGuildId(long guildId) {
		this.guildId = guildId;
	}

	public long getGdeventAwardedSubscriberRoleId() {
		return gdeventAwardedSubscriberRoleId;
	}

	public void setGdeventAwardedSubscriberRoleId(long gdeventAwardedSubscriberRoleId) {
		this.gdeventAwardedSubscriberRoleId = gdeventAwardedSubscriberRoleId;
	}

	public long getGdeventAwardedSubscriberChannelId() {
		return gdeventAwardedSubscriberChannelId;
	}

	public void setGdeventAwardedSubscriberChannelId(long gdeventAwardedSubscriberChannelId) {
		this.gdeventAwardedSubscriberChannelId = gdeventAwardedSubscriberChannelId;
	}
	
}
