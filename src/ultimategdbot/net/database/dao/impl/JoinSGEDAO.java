package ultimategdbot.net.database.dao.impl;

import java.util.List;

import sx.blah.discord.handle.obj.IUser;
import ultimategdbot.app.Main;
import ultimategdbot.net.database.dao.RelationalDAO;
import ultimategdbot.net.database.entities.JoinSGE;
import ultimategdbot.net.database.entities.StatGrindEvent;
import ultimategdbot.net.database.util.ResultInstanceBuilder;

/**
 * DAO for users joining stat grind events.
 * 
 * @author Alex1304
 */
public class JoinSGEDAO implements RelationalDAO<JoinSGE> {

	private static final String TABLE = "join_sge";
	private static final ResultInstanceBuilder<JoinSGE> RESULT_INSTANCE_BUILDER = rset -> {
		IUser user = Main.DISCORD_ENV.getClient().fetchUser(rset.getLong("user_id"));
		if (user == null)
			return null;
		return new JoinSGE(user, rset.getLong("event_id"), rset.getTimestamp("join_date").getTime(),
				rset.getLong("curr_stat_value"));
	};


	@Override
	public int insert(JoinSGE obj) {
		return executeUpdate("INSERT INTO " + TABLE + " VALUES (?, ?, now(), ?)", ps -> {
			ps.setLong(1, obj.getUser().getLongID());
			ps.setLong(2, obj.getSge());
			ps.setLong(3, obj.getCurrStat());
		});
	}
	
	@Override
	public int update(JoinSGE obj) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int delete(JoinSGE obj) {
		return executeUpdate("DELETE FROM " + TABLE + " WHERE user_id = ? AND event_id = ?", ps -> {
			ps.setLong(1, obj.getUser().getLongID());
			ps.setLong(2, obj.getSge());
		});
	}

	@Override
	public JoinSGE find(long id) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Finds the list of all events the specified user is currently participating to
	 * 
	 * @param user - a Discord user
	 * @return a List of JoinSGE
	 */
	public List<JoinSGE> findEventsJoinedByUser(IUser user) {
		return executeQuery("SELECT * FROM " + TABLE + " WHERE user_id = ?", ps -> {
			ps.setLong(1, user.getLongID());
		}, RESULT_INSTANCE_BUILDER);
	}
	
	/**
	 * Finds the list of all users who have joined the specified event
	 * 
	 * @param sge - a stat grind event
	 * @return a List of JoinSGE
	 */
	public List<JoinSGE> findUsersForEvent(StatGrindEvent sge) {
		return executeQuery("SELECT * FROM " + TABLE + " WHERE event_id = ?", ps -> {
			ps.setLong(1, sge.getEventID());
		}, RESULT_INSTANCE_BUILDER);
	}
	
	/**
	 * Finds one relation for one specific user and one specific event.
	 * 
	 * @param user - a Discord user
	 * @param sge - a stat grind event
	 * @return the JoinSGE instance if user is poarticipating to sge (gives access to join date and
	 * stat value on join), or null if nothing found.
	 */
	public JoinSGE find(IUser user, StatGrindEvent sge) {
		List<JoinSGE> result = executeQuery("SELECT * FROM " + TABLE + " WHERE event_id = ? AND user_id = ?", ps -> {
			ps.setLong(1, sge.getEventID());
			ps.setLong(2, user.getLongID());
		}, RESULT_INSTANCE_BUILDER);
		
		return result.isEmpty() ? null : result.get(0);
	}
}
