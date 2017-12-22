package ultimategdbot.net.database.dao.impl;

import java.util.List;

import ultimategdbot.net.database.dao.RelationalDAO;
import ultimategdbot.net.database.util.ResultInstanceBuilder;

/**
 * DAO that manages timely levels by their IDs
 * 
 * @author Alex1304
 *
 */
public class TimelyLevelDAO implements RelationalDAO<Long> {
	
	private static final String TABLE = "timely_level";
	private static final ResultInstanceBuilder<Long> RESULT_INSTANCE_BUILDER = rset -> {
		return rset.getLong("id");
	};

	@Override
	public int insert(Long obj) {
		return executeUpdate("INSERT INTO " + TABLE + " VALUES (?)", ps -> {
			ps.setLong(1, obj);
		});
	}
	
	@Override
	public int update(Long obj) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int delete(Long obj) {
		return executeUpdate("DELETE FROM " + TABLE + " WHERE id = ?", ps -> {
			ps.setLong(1, obj);
		});
	}

	@Override
	public Long find(long id) {
		List<Long> result = executeQuery("SELECT * FROM " + TABLE + " WHERE id = ?", ps -> {
			ps.setLong(1, id);
		}, RESULT_INSTANCE_BUILDER);

		return result.isEmpty() ? -1L : result.get(0);
	}
	
	public Long findLastTimely(boolean daily) {
		List<Long> result = executeQuery("SELECT * FROM " + TABLE + " WHERE id " + (daily ? "<" : ">")
				+ " 100000 ORDER BY id DESC LIMIT 1", RESULT_INSTANCE_BUILDER);

		return result.isEmpty() ? -1L : result.get(0);
	}
}
