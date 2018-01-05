package ultimategdbot.net.database.dao.impl;

import java.util.List;

import sx.blah.discord.handle.obj.IGuild;
import ultimategdbot.app.Main;
import ultimategdbot.guildsettings.ChannelAwardedLevelsSetting;
import ultimategdbot.guildsettings.ChannelBotAnnouncementsSetting;
import ultimategdbot.guildsettings.ChannelGdModeratorsSetting;
import ultimategdbot.guildsettings.ChannelTimelyLevelsSetting;
import ultimategdbot.guildsettings.RoleAwardedLevelsSetting;
import ultimategdbot.guildsettings.RoleGdModeratorsSetting;
import ultimategdbot.guildsettings.RoleTimelyLevelsSetting;
import ultimategdbot.guildsettings.TagEveryoneOnBotAnnouncementSetting;
import ultimategdbot.net.database.dao.RelationalDAO;
import ultimategdbot.net.database.entities.GuildSettings;
import ultimategdbot.net.database.util.ResultInstanceBuilder;

/**
 * DAO that manages guild settings
 * 
 * @author Alex1304
 *
 */
public class GuildSettingsDAO implements RelationalDAO<GuildSettings> {
	
	private static final String TABLE = "guild_settings";
	private static final ResultInstanceBuilder<GuildSettings> RESULT_INSTANCE_BUILDER = rset -> {
		while (!Main.DISCORD_ENV.getClient().isReady()) {
			System.out.println("Client not ready, cannot load guild settings. Retrying in 5 seconds...");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}
		}
		
		GuildSettings gs = null;
		IGuild guild = Main.DISCORD_ENV.getClient().getGuildByID(rset.getLong("guild_id"));
		if (guild == null)
			new GuildSettingsDAO().deleteByID(rset.getLong("guild_id"));
		else
			gs = new GuildSettings(guild,
					guild.getChannelByID(rset.getLong("channel_awarded_levels")),
					guild.getChannelByID(rset.getLong("channel_gd_moderators")),
					guild.getChannelByID(rset.getLong("channel_bot_announcements")),
					guild.getRoleByID(rset.getLong("role_awarded_levels")),
					guild.getRoleByID(rset.getLong("role_gd_moderators")),
					rset.getBoolean("tag_everyone_on_bot_announcement"),
					guild.getChannelByID(rset.getLong("channel_timely_levels")),
					guild.getRoleByID(rset.getLong("role_timely_levels")));
		return gs;
	};

	@Override
	public int insert(GuildSettings obj) {
		return executeUpdate("INSERT INTO " + TABLE + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", ps -> {
			ps.setLong(1, obj.getGuild().getLongID());
			ps.setLong(2, obj.getLongIDForIDLinkedObjectSetting(RoleAwardedLevelsSetting.class));
			ps.setLong(3, obj.getLongIDForIDLinkedObjectSetting(ChannelAwardedLevelsSetting.class));
			ps.setLong(4, obj.getLongIDForIDLinkedObjectSetting(RoleGdModeratorsSetting.class));
			ps.setLong(5, obj.getLongIDForIDLinkedObjectSetting(ChannelGdModeratorsSetting.class));
			ps.setLong(6, obj.getLongIDForIDLinkedObjectSetting(ChannelBotAnnouncementsSetting.class));
			ps.setBoolean(7, obj.getSetting(TagEveryoneOnBotAnnouncementSetting.class).getValue());
			ps.setLong(8, obj.getLongIDForIDLinkedObjectSetting(ChannelTimelyLevelsSetting.class));
			ps.setLong(9, obj.getLongIDForIDLinkedObjectSetting(RoleTimelyLevelsSetting.class));
		});
	}
	
	@Override
	public int update(GuildSettings obj) {
		return executeUpdate("UPDATE " + TABLE + " SET channel_awarded_levels = ?"
				+ ", channel_gd_moderators = ?, channel_bot_announcements = ?"
				+ ", role_awarded_levels = ?, role_gd_moderators = ?"
				+ ", tag_everyone_on_bot_announcement = ?, channel_timely_levels = ?"
				+ ", role_timely_levels = ? WHERE guild_id = ?", ps -> {
			ps.setLong(1, obj.getLongIDForIDLinkedObjectSetting(ChannelAwardedLevelsSetting.class));
			ps.setLong(2, obj.getLongIDForIDLinkedObjectSetting(ChannelGdModeratorsSetting.class));
			ps.setLong(3, obj.getLongIDForIDLinkedObjectSetting(ChannelBotAnnouncementsSetting.class));
			ps.setLong(4, obj.getLongIDForIDLinkedObjectSetting(RoleAwardedLevelsSetting.class));
			ps.setLong(5, obj.getLongIDForIDLinkedObjectSetting(RoleGdModeratorsSetting.class));
			ps.setBoolean(6, obj.getSetting(TagEveryoneOnBotAnnouncementSetting.class).getValue());
			ps.setLong(7, obj.getLongIDForIDLinkedObjectSetting(ChannelTimelyLevelsSetting.class));
			ps.setLong(8, obj.getLongIDForIDLinkedObjectSetting(RoleTimelyLevelsSetting.class));
			ps.setLong(9, obj.getGuild().getLongID());
		});
	}

	@Override
	public int delete(GuildSettings obj) {
		return deleteByID(obj.getGuild().getLongID());
	}
	
	/**
	 * Deletes a row in the table representing a set of guild settings where the
	 * guild ID corresponds to the given ID.
	 * 
	 * @param id
	 *            - the ID of the row to delete
	 * @return the number of rows deleted, or -1 if the query failed
	 */
	public int deleteByID(long id) {
		return executeUpdate("DELETE FROM " + TABLE + " WHERE guild_id = ?", ps -> {
			ps.setLong(1, id);
		});
	}

	@Override
	public GuildSettings find(long id) {
		List<GuildSettings> result = executeQuery("SELECT * FROM " + TABLE + " WHERE guild_id = ?", ps -> {
			ps.setLong(1, id);
		}, RESULT_INSTANCE_BUILDER);

		return result.isEmpty() ? null : result.get(0);
	}
	
	/**
	 * Returns the guild settings with the setting "channel_awarded_levels" provided.
	 * 
	 * @return a List of GuildSettings
	 */
	public List<GuildSettings> findAllWithChannelAwardedLevelsSetup() {
		return executeQuery("SELECT * FROM " + TABLE + " WHERE channel_awarded_levels > 0",
				RESULT_INSTANCE_BUILDER);
	}
	
	/**
	 * Returns the guild settings with the setting "channel_gd_moderators" provided.
	 * 
	 * @return a List of GuildSettings
	 */
	public List<GuildSettings> findAllWithChannelGDModeratorsSetup() {
		return executeQuery("SELECT * FROM " + TABLE + " WHERE channel_gd_moderators > 0",
				RESULT_INSTANCE_BUILDER);
	}
	
	/**
	 * Returns the guild settings with the setting "channel_timely_levels" provided.
	 * 
	 * @return a List of GuildSettings
	 */
	public List<GuildSettings> findAllWithChannelTimelyLevelsSetup() {
		return executeQuery("SELECT * FROM " + TABLE + " WHERE channel_timely_levels > 0",
				RESULT_INSTANCE_BUILDER);
	}
	
	public List<GuildSettings> findAll() {
		return executeQuery("SELECT * FROM " + TABLE, RESULT_INSTANCE_BUILDER);
	}
	
	/**
	 * Attempts to find the guild settings for the given guild ID. If not found,
	 * then a new entry is automatically created in database, and the
	 * corresponding instance of {@link GuildSettings} is returned.
	 * 
	 * @param id
	 *            - the ID of the desired guild
	 * @return an instance of GuildSettings.
	 */
	public GuildSettings findOrCreate(long id) {
		GuildSettings gs = find(id);
		if (gs == null) {
			gs = new GuildSettings(id);
			insert(gs);
		}
		
		return gs;
	}
}
