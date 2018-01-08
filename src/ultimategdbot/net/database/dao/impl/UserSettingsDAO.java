package ultimategdbot.net.database.dao.impl;

import java.util.List;
import java.util.stream.Collectors;

import sx.blah.discord.handle.obj.IUser;
import ultimategdbot.net.database.dao.RelationalDAO;
import ultimategdbot.net.database.entities.UserSettings;
import ultimategdbot.net.database.util.ResultInstanceBuilder;
import ultimategdbot.net.geometrydash.Stat;

/**
 * DAO that manages user settings
 * 
 * @author Alex1304
 *
 */
public class UserSettingsDAO implements RelationalDAO<UserSettings> {

	private static final String TABLE = "user_settings";
	private static final ResultInstanceBuilder<UserSettings> RESULT_INSTANCE_BUILDER = rset -> {
		return new UserSettings(rset.getLong("user_id"),
				rset.getLong("gd_user_id"),
				rset.getBoolean("link_activated"),
				rset.getString("confirmation_token"));
	};

	@Override
	public int insert(UserSettings obj) {
		return executeUpdate("INSERT INTO " + TABLE + " VALUES (?, ?, ?, ?)", ps -> {
			ps.setLong(1, obj.getUserID());
			ps.setLong(2, obj.getGdUserID());
			ps.setBoolean(3, obj.isLinkActivated());
			ps.setString(4, obj.getConfirmationToken());
		});
	}
	
	@Override
	public int update(UserSettings obj) {
		return executeUpdate("UPDATE " + TABLE + " SET gd_user_id = ?, link_activated = ?, "
				+ "confirmation_token = ? WHERE user_id = ?", ps -> {
			ps.setLong(1, obj.getGdUserID());
			ps.setBoolean(2, obj.isLinkActivated());
			ps.setString(3, obj.getConfirmationToken());
			ps.setLong(4, obj.getUserID());
		});
	}

	@Override
	public int delete(UserSettings obj) {
		return executeUpdate("DELETE FROM " + TABLE + " WHERE user_id = ?", ps -> {
			ps.setLong(1, obj.getUserID());
		});
	}

	@Override
	public UserSettings find(long id) {
		List<UserSettings> result = executeQuery("SELECT * FROM " + TABLE + " WHERE user_id = ?", ps -> {
			ps.setLong(1, id);
		}, RESULT_INSTANCE_BUILDER);
		
		if (result.isEmpty())
			return null;
		else
			return result.get(0);
	}
	
	/**
	 * Finds by GD user ID instead of Discord user ID.
	 * 
	 * @param id - the GD user ID too look for
	 * @return a GDUser instance corresponding to the user found
	 */
	public UserSettings findByGDUserID(long id) {
		List<UserSettings> result = executeQuery("SELECT * FROM " + TABLE + " WHERE gd_user_id = ?", ps -> {
			ps.setLong(1, id);
		}, RESULT_INSTANCE_BUILDER);
		
		if (result.isEmpty())
			return null;
		else
			return result.get(0);
	}
	
	/**
	 * Attempts to find the user settings for the given user ID. If not found,
	 * then a new entry is automatically created in database, and the
	 * corresponding instance of {@link UserSettings} is returned.
	 * 
	 * @param id
	 *            - the ID of the desired user
	 * @return an instance of UserSettings.
	 */
	public UserSettings findOrCreate(long id) {
		UserSettings us = find(id);
		if (us == null) {
			us = new UserSettings(id, -1, false, null);
			insert(us);
		}
		
		return us;
	}
	
	/**
	 * Fetches and returns the user settings for all of the Discord users specified
	 * if they have a linked account.
	 * 
	 * @param users - The list of Discord users to find settings
	 * @return a List of UserSettings non-null instances
	 */
	public List<UserSettings> findForLinkedUsers(List<IUser> users, Stat sortOnStat) {
		List<UserSettings> result = executeQuery("SELECT * FROM " + TABLE, RESULT_INSTANCE_BUILDER);
		return result.stream()
				.filter(us -> users.stream().map(IUser::getLongID).collect(Collectors.toList()).contains(us.getUserID()))
				.filter(us -> us != null && us.isLinkActivated())
				.collect(Collectors.toList());
	}
}
