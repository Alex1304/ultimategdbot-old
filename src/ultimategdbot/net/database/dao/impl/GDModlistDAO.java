package ultimategdbot.net.database.dao.impl;

import java.io.IOException;
import java.util.List;

import ultimategdbot.exceptions.RawDataMalformedException;
import ultimategdbot.net.database.dao.RelationalDAO;
import ultimategdbot.net.database.util.ResultInstanceBuilder;
import ultimategdbot.net.geometrydash.GDServer;
import ultimategdbot.net.geometrydash.GDUser;
import ultimategdbot.net.geometrydash.GDUserFactory;

/**
 * DAO that manages GD moderator users
 * 
 * @author Alex1304
 *
 */
public class GDModlistDAO implements RelationalDAO<GDUser> {
	
	private static final String TABLE = "gd_mod_list";
	private static final ResultInstanceBuilder<GDUser> RESULT_INSTANCE_BUILDER = rset -> {
		GDUser user = null;
		try {
			user = GDUserFactory.buildGDUserFromProfileRawData(
					GDServer.fetchUserProfile(rset.getLong("account_id")));
		} catch (RawDataMalformedException | IOException e) {
			return null;
		}
		return user;
	};

	@Override
	public int insert(GDUser obj) {
		return executeUpdate("INSERT INTO " + TABLE + " VALUES (?)", ps -> {
			ps.setLong(1, obj.getAccountID());
		});
	}
	
	@Override
	public int update(GDUser obj) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int delete(GDUser obj) {
		return executeUpdate("DELETE FROM " + TABLE + " WHERE account_id = ?", ps -> {
			ps.setLong(1, obj.getAccountID());
		});
	}

	@Override
	public GDUser find(long id) {
		List<GDUser> result = executeQuery("SELECT * FROM " + TABLE + " WHERE account_id = ?", ps -> {
			ps.setLong(1, id);
		}, RESULT_INSTANCE_BUILDER);

		return result.isEmpty() ? null : result.get(0);
	}
	
	public List<GDUser> findAll() {
		return executeQuery("SELECT * FROM " + TABLE, RESULT_INSTANCE_BUILDER);
	}

	public int deleteRange(long min, long max) {
		if (min > max)
			throw new IllegalArgumentException();
		
		return executeUpdate("DELETE FROM " + TABLE + " WHERE account_id >= ? AND account_id <= ?", ps -> {
			ps.setLong(1, min);
			ps.setLong(2, max);
		});
	}
}