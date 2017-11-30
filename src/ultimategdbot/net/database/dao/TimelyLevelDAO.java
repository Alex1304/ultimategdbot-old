package ultimategdbot.net.database.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ultimategdbot.net.database.DatabaseConnection;

/**
 * DAO that manages timely levels by their IDs
 * 
 * @author Alex1304
 *
 */
public class TimelyLevelDAO implements DAO<Long> {

	@Override
	public boolean insert(Long obj) {
		try {
			PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(
					"INSERT INTO timely_level VALUES (?)");
			ps.setLong(1, obj);
			return ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean update(Long obj) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean delete(Long obj) {
		try {
			PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(
					"DELETE FROM timely_level WHERE id = ?");
			ps.setLong(1, obj);
			return ps.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Long find(long id) {
		long res = -1;

		try {
			PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(
					"SELECT * FROM timely_level WHERE id = ?");
			ps.setLong(1, id);
			ResultSet result = ps.executeQuery();
			if (result.last())
				res = result.getLong("id");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	@Override
	public List<Long> findAll() {
		List<Long> res = new ArrayList<>();

		try {
			ResultSet result = DatabaseConnection.getInstance()
					.createStatement().executeQuery("SELECT * FROM timely_level");
			while (result.next())
				res.add(result.getLong("id"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	public Long findLastTimely(boolean daily) {
		long res = -1;

		try {
			PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(
					"SELECT * FROM timely_level WHERE id " + (daily ? "<" : ">") + " 100000 ORDER BY id DESC LIMIT 1");
			ResultSet result = ps.executeQuery();
			if (result.last()) {
				res = result.getLong("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return res;
	}
}
