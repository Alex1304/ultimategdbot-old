package ultimategdbot.net.database.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ultimategdbot.net.database.DatabaseConnection;
import ultimategdbot.net.database.entities.GuildSettings;

public class GuildSettingsDAO implements DAO<GuildSettings> {

	@Override
	public void insert(GuildSettings obj) {
		try {
			PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(
					"INSERT INTO guild_settings VALUES (?, ?, ?)");
			ps.setLong(1, obj.getGuildId());
			ps.setLong(2, obj.getGdeventAwardedSubscriberRoleId());
			ps.setLong(3, obj.getGdeventAwardedSubscriberChannelId());
			ps.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(GuildSettings obj) {
		try {
			PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(
					"UPDATE guild_settings SET gdevent_awarded_subscriber_roleid = ?"
					+ " AND gdevent_awarded_subscriber_channelid = ? WHERE guild_id = ?");
			ps.setLong(1, obj.getGdeventAwardedSubscriberRoleId());
			ps.setLong(2, obj.getGdeventAwardedSubscriberChannelId());
			ps.setLong(3, obj.getGuildId());
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(GuildSettings obj) {
		try {
			PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(
					"DELETE FROM guild_settings  WHERE guild_id = ?");
			ps.setLong(1, obj.getGuildId());
			ps.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public GuildSettings find(long id) {
		GuildSettings gs = null;

		try {
			ResultSet result = DatabaseConnection.getInstance()
					.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
					.executeQuery("SELECT * FROM guild_settings WHERE guild_id = " + id);
			if (result.first())
				gs = new GuildSettings(id, result.getLong("gdevent_awarded_subscriber_roleid"),
						result.getLong("gdevent_awarded_subscriber_channelid"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return gs;
	}
	
	public List<GuildSettings> findAll() {
		List<GuildSettings> gsList = new ArrayList<>();

		try {
			ResultSet result = DatabaseConnection.getInstance()
					.createStatement().executeQuery("SELECT * FROM guild_settings");
			while (result.next())
				gsList.add(new GuildSettings(result.getLong("guild_id"),
						result.getLong("gdevent_awarded_subscriber_roleid"),
						result.getLong("gdevent_awarded_subscriber_channelid")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return gsList;
	}
}
