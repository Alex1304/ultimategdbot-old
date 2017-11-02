package ultimategdbot.net.geometrydash;

import java.util.Comparator;

public class GDLevelFeaturedScoreComparator implements Comparator<GDLevel> {

	@Override
	public int compare(GDLevel o1, GDLevel o2) {
		return o1.getFeatured() != o2.getFeatured() ? o2.getFeatured() - o1.getFeatured()
				: (int) o2.getId() - (int) o1.getId();
	}

}
