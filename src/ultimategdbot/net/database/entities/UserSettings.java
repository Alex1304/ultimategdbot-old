package ultimategdbot.net.database.entities;

public class UserSettings {
	
	private long userID;
	private long gdUserID;
		
	public UserSettings(long userID, long gdUserID) {
		super();
		this.userID = userID;
		this.gdUserID = gdUserID;
	}

	public long getUserID() {
		return userID;
	}

	public long getGdUserID() {
		return gdUserID;
	}

	public void setGdUserID(long gdUserID) {
		this.gdUserID = gdUserID;
	}
	
}
