package ultimategdbot.net.database.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import ultimategdbot.net.database.DatabaseConnection;
import ultimategdbot.net.database.entities.GuildSettings;

public class GuildSettingsDAO implements DAO<GuildSettings> {

	@Override
	public boolean insert(GuildSettings obj) {
		try {
			PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(
					"INSERT INTO guild_settings VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			
			ps.setLong(1, obj.getGuild().getLongID());
			ps.setLong(2, obj.getLongIDForIDLinkedObjectSetting(RoleAwardedLevelsSetting.class));
			ps.setLong(3, obj.getLongIDForIDLinkedObjectSetting(ChannelAwardedLevelsSetting.class));
			ps.setLong(4, obj.getLongIDForIDLinkedObjectSetting(RoleGdModeratorsSetting.class));
			ps.setLong(5, obj.getLongIDForIDLinkedObjectSetting(ChannelGdModeratorsSetting.class));
			ps.setLong(6, obj.getLongIDForIDLinkedObjectSetting(ChannelBotAnnouncementsSetting.class));
			ps.setBoolean(7, obj.getSetting(TagEveryoneOnBotAnnouncementSetting.class).getValue());
			ps.setLong(8, obj.getLongIDForIDLinkedObjectSetting(ChannelTimelyLevelsSetting.class));
			ps.setLong(9, obj.getLongIDForIDLinkedObjectSetting(RoleTimelyLevelsSetting.class));
			return ps.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean update(GuildSettings obj) {
		try {
			PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(
					"UPDATE guild_settings SET channel_awarded_levels = ?"
					+ ", channel_gd_moderators = ?, channel_bot_announcements = ?"
					+ ", role_awarded_levels = ?, role_gd_moderators = ?"
					+ ", tag_everyone_on_bot_announcement = ?, channel_timely_levels = ?"
					+ ", role_timely_levels = ? WHERE guild_id = ?");
			ps.setLong(1, obj.getLongIDForIDLinkedObjectSetting(ChannelAwardedLevelsSetting.class));
			ps.setLong(2, obj.getLongIDForIDLinkedObjectSetting(ChannelGdModeratorsSetting.class));
			ps.setLong(3, obj.getLongIDForIDLinkedObjectSetting(ChannelBotAnnouncementsSetting.class));
			ps.setLong(4, obj.getLongIDForIDLinkedObjectSetting(RoleAwardedLevelsSetting.class));
			ps.setLong(5, obj.getLongIDForIDLinkedObjectSetting(RoleGdModeratorsSetting.class));
			ps.setBoolean(6, obj.getSetting(TagEveryoneOnBotAnnouncementSetting.class).getValue());
			ps.setLong(7, obj.getLongIDForIDLinkedObjectSetting(ChannelTimelyLevelsSetting.class));
			ps.setLong(8, obj.getLongIDForIDLinkedObjectSetting(RoleTimelyLevelsSetting.class));
			ps.setLong(9, obj.getGuild().getLongID());
			return ps.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean delete(GuildSettings obj) {
		return deleteByID(obj.getGuild().getLongID());
	}
	
	public boolean deleteByID(long id) {
		try {
			PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(
					"DELETE FROM guild_settings WHERE guild_id = ?");
			ps.setLong(1, id);
			return ps.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public GuildSettings find(long id) {
		GuildSettings gs = null;

		try {
			PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(
					"SELECT * FROM guild_settings WHERE guild_id = ?");
			ps.setLong(1, id);
			ResultSet result = ps.executeQuery();
			if (result.first()) {
				IGuild guild = Main.DISCORD_ENV.getClient().getGuildByID(id);
				if (guild == null)
					deleteByID(id);
				else
					gs = new GuildSettings(guild,
							guild.getChannelByID(result.getLong("channel_awarded_levels")),
							guild.getChannelByID(result.getLong("channel_gd_moderators")),
							guild.getChannelByID(result.getLong("channel_bot_announcements")),
							guild.getRoleByID(result.getLong("role_awarded_levels")),
							guild.getRoleByID(result.getLong("role_gd_moderators")),
							result.getBoolean("tag_everyone_on_bot_announcement"),
							guild.getChannelByID(result.getLong("channel_timely_levels")),
							guild.getRoleByID(result.getLong("role_timely_levels")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return gs;
	}
	
	@Override
	public List<GuildSettings> findAll() {
		List<GuildSettings> gsList = new ArrayList<>();

		try {
			ResultSet result = DatabaseConnection.getInstance()
					.createStatement().executeQuery("SELECT * FROM guild_settings");
			while (result.next()) {
				IGuild guild = Main.DISCORD_ENV.getClient().getGuildByID(result.getLong("guild_id"));
				if (guild == null)
					deleteByID(result.getLong("guild_id"));
				else
					gsList.add(new GuildSettings(guild,
							guild.getChannelByID(result.getLong("channel_awarded_levels")),
							guild.getChannelByID(result.getLong("channel_gd_moderators")),
							guild.getChannelByID(result.getLong("channel_bot_announcements")),
							guild.getRoleByID(result.getLong("role_awarded_levels")),
							guild.getRoleByID(result.getLong("role_gd_moderators")),
							result.getBoolean("tag_everyone_on_bot_announcement"),
							guild.getChannelByID(result.getLong("channel_timely_levels")),
							guild.getRoleByID(result.getLong("role_timely_levels"))));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return gsList;
	}
	
	public GuildSettings findOrCreate(long id) {
		GuildSettings gs = find(id);
		if (gs == null) {
			gs = new GuildSettings(id);
			insert(gs);
		}
		
		return gs;
	}
}
