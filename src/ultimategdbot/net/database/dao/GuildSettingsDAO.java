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
	public boolean insert(GuildSettings obj) {
		try {
			PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(
					"INSERT INTO guild_settings VALUES (?, ?, ?)");
			ps.setLong(1, obj.getGuildId());
			ps.setLong(2, obj.getGdeventSubscriberRoleId());
			ps.setLong(3, obj.getGdeventSubscriberChannelId());
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
					"UPDATE guild_settings SET gdevent_awarded_subscriber_roleid = ?"
					+ ", gdevent_awarded_subscriber_channelid = ? WHERE guild_id = ?");
			ps.setLong(1, obj.getGdeventSubscriberRoleId());
			ps.setLong(2, obj.getGdeventSubscriberChannelId());
			ps.setLong(3, obj.getGuildId());
			return ps.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean delete(GuildSettings obj) {
		try {
			PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(
					"DELETE FROM guild_settings WHERE guild_id = ?");
			ps.setLong(1, obj.getGuildId());
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
			if (result.first())
				gs = new GuildSettings(id, result.getLong("gdevent_awarded_subscriber_roleid"),
						result.getLong("gdevent_awarded_subscriber_channelid"));
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
			while (result.next())
				gsList.add(new GuildSettings(result.getLong("guild_id"),
						result.getLong("gdevent_awarded_subscriber_roleid"),
						result.getLong("gdevent_awarded_subscriber_channelid")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return gsList;
	}
	
	public GuildSettings findOrCreate(long id) {
		GuildSettings gs = find(id);
		if (gs == null) {
			gs = new GuildSettings(id, 0, 0);
			insert(gs);
		}
		
		return gs;
	}
}
