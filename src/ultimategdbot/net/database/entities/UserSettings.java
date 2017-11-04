package ultimategdbot.net.database.entities;

public class UserSettings {
	
	private long userID;
	private long gdUserID;
	private boolean linkActivated;
	private String confirmationToken;
		
	public UserSettings(long userID, long gdUserID, boolean linkActivated, String confirmationToken) {
		this.userID = userID;
		this.gdUserID = gdUserID;
		this.linkActivated = linkActivated;
		this.confirmationToken = confirmationToken;
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

	public boolean isLinkActivated() {
		return linkActivated;
	}

	public void setLinkActivated(boolean linkActivated) {
		this.linkActivated = linkActivated;
	}

	public String getConfirmationToken() {
		return confirmationToken;
	}

	public void setConfirmationToken(String confirmationToken) {
		this.confirmationToken = confirmationToken;
	}
	
}
