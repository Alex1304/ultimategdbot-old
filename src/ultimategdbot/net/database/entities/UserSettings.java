package ultimategdbot.net.database.entities;

/**
 * User settings entity. It mainly deals with account linking with Geometry Dash
 * and confirmation token storing.
 * 
 * @author Alex1304
 *
 */
public class UserSettings {
	
	/**
	 * Discord user ID
	 */
	private long userID;
	
	/**
	 * Geometry Dash account ID
	 */
	private long gdUserID;
	
	/**
	 * Whether the link between Discord and GD accounts is active.
	 * When it's false, it usually means that the user hasn't confirmed his account yet.
	 */
	private boolean linkActivated;
	
	/**
	 * Confirmation code required to activate the link
	 */
	private String confirmationToken;
	
	/**
	 * Constructs an instance of UserSettings by specifying every single attribute.
	 * 
	 * @param userID - Discord user ID
	 * @param gdUserID - Geometry Dash account ID
	 * @param linkActivated - Whether the link between Discord and GD accounts is active.
	 * @param confirmationToken - Confirmation code required to activate the link
	 */
	public UserSettings(long userID, long gdUserID, boolean linkActivated, String confirmationToken) {
		this.userID = userID;
		this.gdUserID = gdUserID;
		this.linkActivated = linkActivated;
		this.confirmationToken = confirmationToken;
	}
	
	/**
	 * Gets Discord user ID
	 * 
	 * @return long
	 */
	public long getUserID() {
		return userID;
	}

	/**
	 * Gets Geometry Dash account ID
	 * 
	 * @return long
	 */
	public long getGdUserID() {
		return gdUserID;
	}
	
	/**
	 * Sets Geometry Dash account ID
	 * 
	 * @param gdUserID - long
	 */
	public void setGdUserID(long gdUserID) {
		this.gdUserID = gdUserID;
	}
	
	/**
	 * Gets whether the link between Discord and GD accounts is active.
	 * 
	 * @return boolean
	 */
	public boolean isLinkActivated() {
		return linkActivated;
	}
	
	/**
	 * Sets whether the link between Discord and GD accounts is active.
	 * 
	 * @param linkActivated - boolean
	 */
	public void setLinkActivated(boolean linkActivated) {
		this.linkActivated = linkActivated;
	}
	
	/**
	 * Gets the confirmation code required to activate the link
	 * 
	 * @return String
	 */
	public String getConfirmationToken() {
		return confirmationToken;
	}
	
	/**
	 * Sets the confirmation code required to activate the link
	 * 
	 * @param confirmationToken - String
	 */
	public void setConfirmationToken(String confirmationToken) {
		this.confirmationToken = confirmationToken;
	}
	
}
