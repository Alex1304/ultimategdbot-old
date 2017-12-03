package ultimategdbot.net.database.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ultimategdbot.net.database.DatabaseConnection;
import ultimategdbot.net.geometrydash.DemonDifficulty;
import ultimategdbot.net.geometrydash.Difficulty;
import ultimategdbot.net.geometrydash.GDLevel;
import ultimategdbot.net.geometrydash.Length;

/**
 * DAO that manages GD levels
 * 
 * @author Alex1304
 *
 */
public class GDLevelDAO implements DAO<GDLevel> {

	@Override
	public boolean insert(GDLevel obj) {
		try {
			PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(
					"INSERT INTO gd_level VALUES (now(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setLong(1, obj.getId());
			ps.setInt(2, obj.getFeaturedScore());
			ps.setBoolean(3, obj.isEpic());
			ps.setString(4, obj.getName());
			ps.setString(5, obj.getCreator());
			ps.setString(6, obj.getDescription());
			ps.setShort(7, (short) obj.getDifficulty().ordinal());
			ps.setShort(8, (short) obj.getDemonDifficulty().ordinal());
			ps.setShort(9, obj.getStars());
			ps.setLong(10, obj.getDownloads());
			ps.setLong(11, obj.getLikes());
			ps.setShort(12, (short) obj.getLength().ordinal());
			return ps.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean update(GDLevel obj) {
		try {
			PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(
					"UPDATE gd_level SET featured = ?, is_epic = ?, name = ?, creator = ?"
					+ ", description = ?, difficulty = ?, demon_difficulty = ?"
					+ ", stars = ?, downloads = ?, likes = ?, length = ? WHERE level_id = ?");
			ps.setInt(1, obj.getFeaturedScore());
			ps.setBoolean(2, obj.isEpic());
			ps.setString(3, obj.getName());
			ps.setString(4, obj.getCreator());
			ps.setString(5, obj.getDescription());
			ps.setShort(6, (short) obj.getDifficulty().ordinal());
			ps.setShort(7, (short) obj.getDifficulty().ordinal());
			ps.setShort(8, obj.getStars());
			ps.setLong(9, obj.getDownloads());
			ps.setLong(10, obj.getLikes());
			ps.setShort(11, (short) obj.getLength().ordinal());
			ps.setLong(12, obj.getId());
			return ps.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean delete(GDLevel obj) {
		try {
			PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(
					"DELETE FROM gd_level WHERE level_id = ?");
			ps.setLong(1, obj.getId());
			return ps.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public GDLevel find(long id) {
		GDLevel gs = null;

		try {
			PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(
					"SELECT * FROM gd_level WHERE level_id = ?");
			ps.setLong(1, id);
			ResultSet result = ps.executeQuery();
			if (result.last())
				gs = new GDLevel(id,
					result.getString("name"),
					result.getString("creator"),
					result.getString("description"),
					Difficulty.values()[result.getShort("difficulty")],
					DemonDifficulty.values()[result.getShort("demon_difficulty")],
					result.getShort("stars"),
					result.getInt("featured"),
					result.getBoolean("is_epic"),
					result.getLong("downloads"),
					result.getLong("likes"),
					Length.values()[result.getShort("length")],
					-1
				);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return gs;
	}
	
	@Override
	public List<GDLevel> findAll() {
		List<GDLevel> gsList = new ArrayList<>();

		try {
			ResultSet result = DatabaseConnection.getInstance()
					.createStatement().executeQuery("SELECT * FROM gd_level");
			while (result.next())
				gsList.add(new GDLevel(result.getLong("level_id"),
						result.getString("name"),
						result.getString("creator"),
						result.getString("description"),
						Difficulty.values()[result.getShort("difficulty")],
						DemonDifficulty.values()[result.getShort("demon_difficulty")],
						result.getShort("stars"),
						result.getInt("featured"),
						result.getBoolean("is_epic"),
						result.getLong("downloads"),
						result.getLong("likes"),
						Length.values()[result.getShort("length")],
						-1));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return gsList;
	}
	
	public GDLevel findLastAwarded() {
		GDLevel gs = null;

		try {
			PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(
					"SELECT * FROM gd_level ORDER BY insert_date DESC LIMIT 1");
			ResultSet result = ps.executeQuery();
			if (result.last()) {
				gs = new GDLevel(
					result.getLong("level_id"),
					result.getString("name"),
					result.getString("creator"),
					result.getString("description"),
					Difficulty.values()[result.getShort("difficulty")],
					DemonDifficulty.values()[result.getShort("demon_difficulty")],
					result.getShort("stars"),
					result.getInt("featured"),
					result.getBoolean("is_epic"),
					result.getLong("downloads"),
					result.getLong("likes"),
					Length.values()[result.getShort("length")],
					-1
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return gs;
	}
}
