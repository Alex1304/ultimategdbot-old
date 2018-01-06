package ultimategdbot.net.database.dao.impl;

import java.sql.Timestamp;
import java.util.List;

import sx.blah.discord.handle.obj.IGuild;
import ultimategdbot.app.Main;
import ultimategdbot.net.database.dao.RelationalDAO;
import ultimategdbot.net.database.entities.StatGrindEvent;
import ultimategdbot.net.database.util.ResultInstanceBuilder;
import ultimategdbot.net.geometrydash.Stat;

/**
 * DAO that manages GD levels
 * 
 * @author Alex1304
 *
 */
public class StatGrindEventDAO implements RelationalDAO<StatGrindEvent> {
	
	private static final String TABLE = "stat_grind_event";
	private static final ResultInstanceBuilder<StatGrindEvent> RESULT_INSTANCE_BUILDER = rset -> {
		StatGrindEvent sge = null;
		IGuild guild = Main.DISCORD_ENV.getClient().getGuildByID(rset.getLong("guild_id"));
		if (guild == null)
			return null;
		sge = new StatGrindEvent(rset.getLong("event_id"),
			guild,
			rset.getTimestamp("date_begin").getTime(),
			rset.getTimestamp("date_end").getTime(),
			Stat.valueOf(rset.getString("stat_type"))
		);
		return sge;
	};

	@Override
	public int insert(StatGrindEvent obj) {
		return executeUpdate("INSERT INTO " + TABLE + " VALUES (NULL, ?, NULL, ?, ?)", ps -> {
			ps.setLong(1, obj.getGuild().getLongID());
			ps.setTimestamp(2, new Timestamp(obj.getDateEnd()));
			ps.setString(3, obj.getStatType().toString());
		});
	}
	
	@Override
	public int update(StatGrindEvent obj) {
		return executeUpdate("UPDATE " + TABLE + " SET date_end = ? WHERE event_id = ?", ps -> {
			ps.setTimestamp(1, new Timestamp(obj.getDateEnd()));
			ps.setLong(2, obj.getEventID());
		});
	}

	@Override
	public int delete(StatGrindEvent obj) {
		return executeUpdate("DELETE FROM " + TABLE + " WHERE event_id = ?", ps -> {
			ps.setLong(1, obj.getEventID());
		});
	}

	@Override
	public StatGrindEvent find(long id) {
		List<StatGrindEvent> result = executeQuery("SELECT * FROM " + TABLE + " WHERE event_id = ?", ps -> {
			ps.setLong(1, id);
		}, RESULT_INSTANCE_BUILDER);
		
		return result.isEmpty() ? null : result.get(0);
	}
	
	public StatGrindEvent findByGuild(long id) {
		List<StatGrindEvent> result = executeQuery("SELECT * FROM " + TABLE + " WHERE guild_id = ?", ps -> {
			ps.setLong(1, id);
		}, RESULT_INSTANCE_BUILDER);
		
		return result.isEmpty() ? null : result.get(0);
	}
}
