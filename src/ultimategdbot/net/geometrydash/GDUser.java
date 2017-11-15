package ultimategdbot.net.geometrydash;

public class GDUser {

	private String name;
	private long playerID;
	private int secretCoins;
	private int userCoins;
	private int stars;
	private int diamonds;
	private int demons;
	private int creatorPoints;
	private String youtube;
	private int globalRank;
	private long accountID;
	private GDRole role;
	private String twitter;
	private String twitch;

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

	public String getName() {
		return name;
	}

	public long getPlayerID() {
		return playerID;
	}

	public int getSecretCoins() {
		return secretCoins;
	}

	public int getUserCoins() {
		return userCoins;
	}

	public int getStars() {
		return stars;
	}

	public int getDiamonds() {
		return diamonds;
	}

	public int getDemons() {
		return demons;
	}

	public int getCreatorPoints() {
		return creatorPoints;
	}

	public String getYoutube() {
		return youtube;
	}

	public int getGlobalRank() {
		return globalRank;
	}

	public long getAccountID() {
		return accountID;
	}

	public GDRole getRole() {
		return role;
	}

	public String getTwitter() {
		return twitter;
	}

	public String getTwitch() {
		return twitch;
	}
}
