package ultimategdbot.net.database.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ultimategdbot.net.database.DatabaseConnection;
import ultimategdbot.net.database.entities.GuildSettings;
import ultimategdbot.net.database.entities.UserSettings;

public class UserSettingsDAO implements DAO<UserSettings> {

	@Override
	public boolean insert(UserSettings obj) {
		try {
			PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(
					"INSERT INTO user_settings VALUES (?, ?, ?, ?)");
			ps.setLong(1, obj.getUserID());
			ps.setLong(2, obj.getGdUserID());
			ps.setBoolean(3, obj.isLinkActivated());
			ps.setString(4, obj.getConfirmationToken());
			return ps.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean update(UserSettings obj) {
		try {
			PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(
					"UPDATE user_settings SET gd_user_id = ?, link_activated = ?, confirmation_token = ? WHERE user_id = ?");
			ps.setLong(1, obj.getGdUserID());
			ps.setBoolean(2, obj.isLinkActivated());
			ps.setString(3, obj.getConfirmationToken());
			ps.setLong(4, obj.getUserID());
			return ps.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean delete(UserSettings obj) {
		try {
			PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(
					"DELETE FROM user_settings WHERE user_id = ?");
			ps.setLong(1, obj.getUserID());
			return ps.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public UserSettings find(long id) {
		UserSettings us = null;

		try {
			ResultSet result = DatabaseConnection.getInstance()
					.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
					.executeQuery("SELECT * FROM user_settings WHERE user_id = " + id);
			if (result.first())
				us = new UserSettings(id,
						result.getLong("gd_user_id"),
						result.getBoolean("link_activated"),
						result.getString("confirmation_token"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return us;
	}
	
	public List<UserSettings> findAll() {
		List<UserSettings> usList = new ArrayList<>();

		try {
			ResultSet result = DatabaseConnection.getInstance()
					.createStatement().executeQuery("SELECT * FROM user_settings");
			while (result.next())
				usList.add(new UserSettings(result.getLong("user_id"),
						result.getLong("gd_user_id"),
						result.getBoolean("link_activated"),
						result.getString("confirmation_token")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return usList;
	}
	
	public UserSettings findOrCreate(long id) {
		UserSettings us = find(id);
		if (us == null) {
			us = new UserSettings(id, -1, false, null);
			insert(us);
		}
		
		return us;
	}
}
