package ultimategdbot.net.geometrydash;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class GDLevel {
	
	private long id;
	private String name;
	private String creator;
	private String description;
	private int stars;
	private boolean featured;
	private boolean epic;
	private int downloads;
	private int likes;
	
	public GDLevel(long id, String name, String creator, String description, int stars, boolean featured, boolean epic,
			int downloads, int likes) {
		super();
		this.id = id;
		this.name = name;
		this.creator = creator;
		this.description = description;
		this.stars = stars;
		this.featured = featured;
		this.epic = epic;
		this.downloads = downloads;
		this.likes = likes;
	}

	public GDLevel(String levelData, String creatorData) {
		String[] splittedLvlData = levelData.split(":");
		Map<Integer, String> structuredLvlData = new HashMap<>();
		
		for (int i = 0 ; i < splittedLvlData.length ; i += 2) {
			structuredLvlData.put(Integer.parseInt(splittedLvlData[i]), splittedLvlData[i+1]);
		}
		
		this.id = Long.parseLong(structuredLvlData.get(1));
		this.name = structuredLvlData.get(2);
		this.description = new String(Base64.getUrlDecoder().decode(structuredLvlData.get(3)));
		this.stars = Integer.parseInt(structuredLvlData.get(39));
		
		String[] creators = creatorData.split("\\|");
		String creatorID = structuredLvlData.get(6);
		int i = 0;
		while (this.creator == null && i < creators.length) {
			if (creators[i].split(":")[0].equals(creatorID))
				this.creator = creators[i].split(":")[1];
			i++;
		}
		
		this.featured = !structuredLvlData.get(19).equals("0");
		this.epic = structuredLvlData.get(42).equals("1");
		this.downloads = Integer.parseInt(structuredLvlData.get(10));
		this.likes = Integer.parseInt(structuredLvlData.get(14));
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

	public void displayInfo() {
		System.out.println("Level ID: " + getId());
		System.out.println("Name: " + getName());
		System.out.println("Description: " + getDescription());
		System.out.println("Creator: " + getCreator());
		System.out.println("Stars: " + getStars());
		System.out.println("Featured? " + isFeatured());
		System.out.println("Epic? " + isEpic());
		System.out.println("Downloads: " + getDownloads());
		System.out.println("Likes: " + getLikes());
	}
	
	public static void main(String[] args) {
		String awarded = GDServer.fetchNewAwardedLevels();
		System.out.println(awarded);
		String[] lvlsCreatorsSongs = awarded.split("#");
		
		String[] lvls = lvlsCreatorsSongs[0].split("\\|");
		
		for (int i = 0 ; i < lvls.length ; i++) {
			GDLevel lvl = new GDLevel(lvls[i], lvlsCreatorsSongs[1]);
			lvl.displayInfo();
			System.out.println();
		}
	}
}
