package ultimategdbot.net.geometrydash;

import java.util.function.Function;

import ultimategdbot.util.Emoji;

/**
 * Enumerates the different game stats
 * 
 * @author Alex1304
 *
 */
public enum Stat {
	STARS(GDUser::getStars, Emoji.STAR),
	DIAMONDS(GDUser::getDiamonds, Emoji.DIAMOND),
	UCOINS(GDUser::getUserCoins, Emoji.USERCOIN),
	SCOINS(GDUser::getSecretCoins, Emoji.SECRETCOIN),
	DEMONS(GDUser::getDemons, Emoji.DEMON),
	CP(GDUser::getCreatorPoints, Emoji.CREATOR_POINTS);
	
	/**
	 * Function that returns the value of the stat represented by the current
	 * object for the GDUser given as input
	 */
	private Function<GDUser, Integer> function;
	
	/**
	 * Emoji that represents the stat
	 */
	private Emoji emoji;
	
	private Stat(Function<GDUser, Integer> function, Emoji emoji) {
		this.function = function;
		this.emoji = emoji;
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
		return this.function.apply(user);
	}
	
	public Function<GDUser, Integer> getFunction() {
		return function;
	}

	/**
	 * Gets the emoji that represents the stat
	 * 
	 * @return Emoji
	 */
	public Emoji getEmoji() {
		return emoji;
	}
}
