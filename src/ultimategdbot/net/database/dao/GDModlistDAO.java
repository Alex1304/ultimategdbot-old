package ultimategdbot.net.database.dao;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ultimategdbot.exceptions.RawDataMalformedException;
import ultimategdbot.net.database.DatabaseConnection;
import ultimategdbot.net.geometrydash.GDServer;
import ultimategdbot.net.geometrydash.GDUser;
import ultimategdbot.net.geometrydash.GDUserFactory;

public class GDModlistDAO implements DAO<GDUser> {

	@Override
	public boolean insert(GDUser obj) {
		try {
			PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(
					"INSERT INTO gd_mod_list VALUES (?)");
			ps.setLong(1, obj.getAccountID());
			return ps.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean update(GDUser obj) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean delete(GDUser obj) {
		throw new UnsupportedOperationException();
	}
	
	public boolean truncate() {
		try {
			PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(
					"TRUNCATE TABLE gd_mod_list");
			return ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public GDUser find(long id) {
		GDUser res = null;

		try {
			PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(
					"SELECT * FROM gd_mod_list WHERE account_id = ?");
			ps.setLong(1, id);
			ResultSet result = ps.executeQuery();
			if (result.last())
				res = GDUserFactory.buildGDUserFromProfileRawData(GDServer.fetchUserProfile(id));
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (RawDataMalformedException | IOException e) {
			return null;
		}
		
		return res;
	}
	
	@Override
	public List<GDUser> findAll() {
		List<GDUser> gsList = new ArrayList<>();

		try {
			ResultSet result = DatabaseConnection.getInstance()
					.createStatement().executeQuery("SELECT * FROM gd_mod_list");
			while (result.next())
				try {
					gsList.add(GDUserFactory.buildGDUserFromProfileRawData(GDServer.fetchUserProfile(
							result.getLong("account_id"))));
				} catch (RawDataMalformedException | IOException e) {
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return gsList;
	}
}