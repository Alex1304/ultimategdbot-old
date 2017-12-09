package ultimategdbot.net.geometrydash;

/**
 * Entity representing one Geometry Dash user.
 * 
 * @author Alex1304
 *
 */
public class GDUser implements Comparable<GDUser> {
	
	/**
	 * User's nickname
	 */
	private String name;
	
	/**
	 * User's player ID
	 */
	private long playerID;
	
	/**
	 * User's secret coins
	 */
	private int secretCoins;
	
	/**
	 * User's user coins
	 */
	private int userCoins;
	
	/**
	 * User's stars
	 */
	private int stars;
	
	/**
	 * User's diamonds
	 */
	private int diamonds;
	
	/**
	 * User's demons
	 */
	private int demons;
	
	/**
	 * User's creator points.
	 */
	private int creatorPoints;
	
	/**
	 * User's YouTube channel link (only the part after https://www.youtube.com/channel/...)
	 */
	private String youtube;
	
	/**
	 * Global rank of user.
	 */
	private int globalRank;
	
	/**
	 * User's account ID
	 */
	private long accountID;
	
	/**
	 * User's role in-game
	 */
	private GDRole role;
	
	/**
	 * User's Twitter account tag
	 */
	private String twitter;
	
	/**
	 * User's Twitch username.
	 */
	private String twitch;
	
	/**
	 * Constructs a new instance with all fields provided.
	 * 
	 * @param name
	 *            - user's nickname
	 * @param playerID
	 *            - user's player ID
	 * @param secretCoins
	 *            - user's secret coins
	 * @param userCoins
	 *            - user's user coins
	 * @param stars
	 *            - user's stars
	 * @param diamonds
	 *            - user's diamonds
	 * @param demons
	 *            - user's demons
	 * @param creatorPoints
	 *            - user's creator points
	 * @param youtube
	 *            - user's YouTube channel link (only the part after
	 *            https://www.youtube.com/channel/...)
	 * @param globalRank
	 *            - global rank of user
	 * @param accountID
	 *            - user's account ID
	 * @param role
	 *            - user's role in-game
	 * @param twitter
	 *            - user's Twitter account tag
	 * @param twitch
	 *            - user's Twitch username.
	 */
	public GDUser(String name, long playerID, int secretCoins, int userCoins, int stars, int diamonds, int demons,
			int creatorPoints, String youtube, int globalRank, long accountID, GDRole role, String twitter,
			String twitch) {
		this.name = name;
		this.playerID = playerID;
		this.secretCoins = secretCoins;
		this.userCoins = userCoins;
		this.stars = stars;
		this.diamonds = diamonds;
		this.demons = demons;
		this.creatorPoints = creatorPoints;
		this.youtube = youtube;
		this.globalRank = globalRank;
		this.accountID = accountID;
		this.role = role;
		this.twitter = twitter;
		this.twitch = twitch;
	}
	
	/**
	 * Gets the user's nickname
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the user's player ID
	 * 
	 * @return String
	 */
	public long getPlayerID() {
		return playerID;
	}

	/**
	 * Gets the user's secret coins
	 * 
	 * @return String
	 */
	public int getSecretCoins() {
		return secretCoins;
	}

	/**
	 * Gets the user's user coins
	 * 
	 * @return String
	 */
	public int getUserCoins() {
		return userCoins;
	}

	/**
	 * Gets the user's stars
	 * 
	 * @return String
	 */
	public int getStars() {
		return stars;
	}

	/**
	 * Gets the user's diamonds
	 * 
	 * @return String
	 */
	public int getDiamonds() {
		return diamonds;
	}

	/**
	 * Gets the user's demons
	 * 
	 * @return String
	 */
	public int getDemons() {
		return demons;
	}

	/**
	 * Gets the user's creator points
	 * 
	 * @return String
	 */
	public int getCreatorPoints() {
		return creatorPoints;
	}

	/**
	 * Gets the user's YouTube channel link (only the part after
	 * https://www.youtube.com/channel/...)
	 * 
	 * @return String
	 */
	public String getYoutube() {
		return youtube;
	}

	/**
	 * Gets the global rank of user
	 * 
	 * @return String
	 */
	public int getGlobalRank() {
		return globalRank;
	}

	/**
	 * Gets the user's account ID
	 * 
	 * @return String
	 */
	public long getAccountID() {
		return accountID;
	}

	/**
	 * Gets the user's role in-game
	 * 
	 * @return String
	 */
	public GDRole getRole() {
		return role;
	}

	/**
	 * Gets the user's Twitter account tag
	 * 
	 * @return String
	 */
	public String getTwitter() {
		return twitter;
	}

	/**
	 * Gets the user's Twitch username
	 * 
	 * @return String
	 */
	public String getTwitch() {
		return twitch;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (accountID ^ (accountID >>> 32));
		return result;
	}
	
	/**
	 * Two users are equal if and only if they have the same accountID.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof GDUser))
			return false;
		GDUser other = (GDUser) obj;
		if (accountID != other.accountID)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "ID = " + accountID + " ; name = " + name + " ; stars = " + stars + "\n";
	}

	/**
	 * Used to sort users by their name.
	 * 
	 * @see {@link Comparable#compareTo(Object)}
	 */
	@Override
	public int compareTo(GDUser o) {
		return name.compareTo(o.name);
	}
}
