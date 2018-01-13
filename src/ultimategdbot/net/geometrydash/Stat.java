package ultimategdbot.net.geometrydash;

import java.util.function.Function;

/**
 * Enumerates the different game stats
 * 
 * @author Alex1304
 *
 */
public enum Stat {
	STARS(GDUser::getStars),
	DIAMONDS(GDUser::getDiamonds),
	UCOINS(GDUser::getUserCoins),
	SCOINS(GDUser::getSecretCoins),
	DEMONS(GDUser::getDemons),
	CP(GDUser::getCreatorPoints);
	
	/**
	 * Function that associates a GD user with the stat represented by the
	 * current object
	 */
	private Function<GDUser, Integer> userStat;
	
	private Stat(Function<GDUser, Integer> userStat) {
		this.userStat = userStat;
	}
	
	/**
	 * Gets the value of the stat represented by the current object for the
	 * specified user
	 * 
	 * @param user
	 *            - a GD user
	 * @return int
	 */
	public int forUser(GDUser user) {
		return this.userStat.apply(user);
	}
}
