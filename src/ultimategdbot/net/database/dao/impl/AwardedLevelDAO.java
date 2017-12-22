package ultimategdbot.net.database.dao.impl;

import java.io.IOException;
import java.util.List;

import ultimategdbot.exceptions.RawDataMalformedException;
import ultimategdbot.net.database.dao.RelationalDAO;
import ultimategdbot.net.database.util.ResultInstanceBuilder;
import ultimategdbot.net.geometrydash.GDLevel;
import ultimategdbot.net.geometrydash.GDLevelFactory;
import ultimategdbot.net.geometrydash.GDServer;

/**
 * DAO that manages GD levels
 * 
 * @author Alex1304
 *
 */
public class AwardedLevelDAO implements RelationalDAO<GDLevel> {
	
	private static final String TABLE = "awarded_level";
	private static final ResultInstanceBuilder<GDLevel> RESULT_INSTANCE_BUILDER = rset -> {
		GDLevel level = null;
		try {
			level = GDLevelFactory.buildGDLevelFirstSearchResult(
					GDServer.fetchLevelByNameOrID(rset.getString("level_id")));
		} catch (RawDataMalformedException | IOException e) {
			e.printStackTrace();
			return null;
		}
		return level;
	};

	@Override
	public int insert(GDLevel obj) {
		return executeUpdate("INSERT INTO " + TABLE + " VALUES (now(), ?, ?, ?)", ps -> {
			ps.setLong(1, obj.getId());
			ps.setLong(2, obj.getDownloads());
			ps.setLong(3, obj.getLikes());
		});
	}
	
	@Override
	public int update(GDLevel obj) {
		return executeUpdate("UPDATE " + TABLE + " SET downloads = ?, likes = ? WHERE level_id = ?", ps -> {
			ps.setLong(1, obj.getDownloads());
			ps.setLong(2, obj.getLikes());
			ps.setLong(3, obj.getId());
		});
	}

	@Override
	public int delete(GDLevel obj) {
		return executeUpdate("DELETE FROM " + TABLE + " WHERE level_id = ?", ps -> {
			ps.setLong(1, obj.getId());
		});
	}

	@Override
	public GDLevel find(long id) {
		List<GDLevel> result = executeQuery("SELECT * FROM " + TABLE + " WHERE level_id = ?", ps -> {
			ps.setLong(1, id);
		}, RESULT_INSTANCE_BUILDER);
		
		return result.isEmpty() ? null : result.get(0);
	}
	
	/**
	 * Finds the entity with the most recent insert date.
	 * 
	 * @return an instance of the entity GDLevel
	 */
	public GDLevel findLastAwarded() {
		List<GDLevel> result = executeQuery("SELECT * FROM " + TABLE + " ORDER BY insert_date DESC LIMIT 1",
				RESULT_INSTANCE_BUILDER);

		return result.isEmpty() ? null : result.get(0);
	}
}
