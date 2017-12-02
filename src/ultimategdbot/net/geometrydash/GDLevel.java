package ultimategdbot.net.geometrydash;

/**
 * Represents one level in Geometry Dash. All statistics and attributes of a Geometry
 * Dash level are defined here (name, creator, difficulty, stars, length, etc)
 * 
 * @author Alex1304
 *
 */
public class GDLevel {
	
	/**
	 * The ID of the level
	 */
	private long id;
	
	/**
	 * The name of the level
	 */
	private String name;
	
	/**
	 * The name of the creator who created this level
	 */
	private String creator;
	
	/**
	 * The level description
	 */
	private String description;
	
	/**
	 * The level difficulty
	 */
	private Difficulty difficulty;
	
	/**
	 * If it's a Demon, the type of Demon difficulty
	 */
	private DemonDifficulty demonDifficulty;
	
	/**
	 * The number of stars assigned to the level
	 */
	private short stars;
	
	/**
	 * The featured score of the level, or a value &lt;= 0 if not featured
	 */
	private int featuredScore;
	
	/**
	 * Whether the level is marked as Epic
	 */
	private boolean epic;
	
	/**
	 * Amount of downloads for the level
	 */
	private long downloads;
	
	/**
	 * Amount of likes for the level
	 */
	private long likes;
	
	/**
	 * The length of the level
	 */
	private Length length;
	
	/**
	 * Constructs an instance of GDLevel by providing all of its attributes at
	 * once.
	 * 
	 * @param id
	 *            - the ID of the level
	 * @param name
	 *            - the name of the level
	 * @param creator
	 *            - the name of the user who created this level
	 * @param description
	 *            - the level description
	 * @param difficulty
	 *            - the level difficulty
	 * @param demonDifficulty
	 *            - if it's a Demon, the type of Demon difficulty
	 * @param stars
	 *            - the number of stars assigned to the level
	 * @param featuredScore
	 *            - the featured score of the level, or a value &lt;= 0 if not
	 *            featured
	 * @param epic
	 *            - whether the level is marked as Epic
	 * @param downloads
	 *            - amount of downloads for the level
	 * @param likes
	 *            - amount of likes for the level
	 * @param length
	 *            - the length of the level
	 */
	public GDLevel(long id, String name, String creator, String description, Difficulty difficulty,
			DemonDifficulty demonDifficulty, short stars, int featuredScore, boolean epic, long downloads,
			long likes, Length length) {
		this.id = id;
		this.name = name;
		this.creator = creator;
		this.description = description;
		this.difficulty = difficulty;
		this.demonDifficulty = demonDifficulty;
		this.stars = stars;
		this.featuredScore = featuredScore;
		this.epic = epic;
		this.downloads = downloads;
		this.likes = likes;
		this.length = length;
	}
	
	/**
	 * Gets the ID of the level
	 * 
	 * @return long
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * Gets the name of the level
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the name of the creator who created this level
	 * 
	 * @return String
	 */
	public String getCreator() {
		return creator;
	}
	
	/**
	 * Gets the level description
	 * 
	 * @return String
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Gets the level difficulty
	 * 
	 * @return Difficulty
	 */
	public Difficulty getDifficulty() {
		return difficulty;
	}
	
	/**
	 * Gets the type of Demon difficulty
	 * 
	 * @return DemonDifficulty
	 */
	public DemonDifficulty getDemonDifficulty() {
		return demonDifficulty;
	}
	
	/**
	 * Gets the number of stars assigned to the level
	 * 
	 * @return
	 */
	public short getStars() {
		return stars;
	}
	
	/**
	 * Whether the level is featured.
	 * 
	 * @return boolean
	 */
	public boolean isFeatured() {
		return featuredScore > 0;
	}
	
	/**
	 * Gets the featured score of the level, or a value &lt;= 0 if not featured
	 * 
	 * @return int
	 */
	public int getFeaturedScore() {
		return featuredScore;
	}
	
	/**
	 * Whether the level is marked as Epic
	 * 
	 * @return boolean
	 */
	public boolean isEpic() {
		return epic;
	}
	
	/**
	 * Gets the amount of downloads for the level
	 * 
	 * @return long
	 */
	public long getDownloads() {
		return downloads;
	}

	/**
	 * Gets the amount of likes for the level
	 * 
	 * @return long
	 */
	public long getLikes() {
		return likes;
	}
	
	/**
	 * Gets the length of the level
	 * 
	 * @return Length
	 */
	public Length getLength() {
		return length;
	}
	
	/**
	 * Whether the level is Awarded, i.e the amount of stars is greater than 0.
	 * @return
	 */
	public boolean isAwarded() {
		return stars > 0;
	}
	
	@Override
	public String toString() {
		return "**__" + name + "__** by **" + creator + "** (" + id + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	
	/**
	 * Two levels are considered as equal if and only if they have both
	 * the same ID
	 * 
	 * @see {@link Object#equals(Object)}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GDLevel other = (GDLevel) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	/**
	 * The regular equals() definition in this class does
	 * only compare the level ID. This method does compare
	 * more fields : stars, difficulty, featured, epic, etc.
	 * 
	 * @param obj - the other object to compare to
	 * @return true if the current object is equal to obj along
	 * the specified criterias.
	 */
	public boolean stateEquals(Object obj) {
		if (!this.equals(obj))
			return false;
		
		GDLevel other = (GDLevel) obj;
		if (difficulty != other.difficulty)
			return false;
		if (demonDifficulty != other.demonDifficulty)
			return false;
		if (epic != other.epic)
			return false;
		if (featuredScore != other.featuredScore)
			return false;
		if (stars != other.stars)
			return false;
		return true;
	}
	
//	Some testing...
//	
//	public void displayInfo() {
//		System.out.println("Level ID: " + getId());
//		System.out.println("Name: " + getName());
//		System.out.println("Description: " + getDescription());
//		System.out.println("Creator: " + getCreator());
//		System.out.println("Stars: " + getStars());
//		System.out.println("Featured? " + isFeatured());
//		System.out.println("Epic? " + isEpic());
//		System.out.println("Downloads: " + getDownloads());
//		System.out.println("Likes: " + getLikes());
//		System.out.println();
//	}
//	
//	public static void main(String[] args) {
//		String awarded = GDServer.fetchNewAwardedLevels();
//		
//		try {
//			for (GDLevel lvl : GDLevelFactory.buildAllGDLevelsSearchResults(awarded)) {
//				lvl.displayInfo();
//			}
//		} catch (RawDataMalformedException e) {
//			System.err.println("Malformed raw data: " + awarded);
//		}
//	}
}
