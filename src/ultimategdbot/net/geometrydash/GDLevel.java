package ultimategdbot.net.geometrydash;

public class GDLevel {
	
	private long id;
	private String name;
	private String creator;
	private String description;
	private Difficulty difficulty;
	private int stars;
	private boolean featured;
	private boolean epic;
	private int downloads;
	private int likes;
	private Length length;
	
	public GDLevel(long id, String name, String creator, String description, Difficulty difficulty, int stars, boolean featured, boolean epic,
			int downloads, int likes, Length length) {
		super();
		this.id = id;
		this.name = name;
		this.creator = creator;
		this.description = description;
		this.difficulty = difficulty;
		this.stars = stars;
		this.featured = featured;
		this.epic = epic;
		this.downloads = downloads;
		this.likes = likes;
		this.length = length;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCreator() {
		return creator;
	}

	public String getDescription() {
		return description;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public int getStars() {
		return stars;
	}

	public boolean isFeatured() {
		return featured;
	}

	public boolean isEpic() {
		return epic;
	}
	
	public int getDownloads() {
		return downloads;
	}

	public int getLikes() {
		return likes;
	}
	
	public Length getLength() {
		return length;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((difficulty == null) ? 0 : difficulty.hashCode());
		result = prime * result + downloads;
		result = prime * result + (epic ? 1231 : 1237);
		result = prime * result + (featured ? 1231 : 1237);
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + likes;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + stars;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GDLevel other = (GDLevel) obj;
		if (creator == null) {
			if (other.creator != null)
				return false;
		} else if (!creator.equals(other.creator))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (difficulty != other.difficulty)
			return false;
		if (downloads != other.downloads)
			return false;
		if (epic != other.epic)
			return false;
		if (featured != other.featured)
			return false;
		if (id != other.id)
			return false;
		if (likes != other.likes)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (stars != other.stars)
			return false;
		return true;
	}
	
	public boolean hasSameIDThan(GDLevel otherLevel) {
		return this.id == otherLevel.id;
	}
	
	@Override
	public String toString() {
		return ""
			+ "Level ID: " + getId() + "\n"
			+ "Name: " + getName() + "\n"
			+ "Description: " + getDescription() + "\n"
			+ "Creator: " + getCreator() + "\n"
			+ "Stars: " + getStars() + "\n"
			+ "Featured? " + isFeatured() + "\n"
			+ "Epic? " + isEpic() + "\n"
			+ "Downloads: " + getDownloads() + "\n"
			+ "Likes: " + getLikes()
		;
	}
	
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
