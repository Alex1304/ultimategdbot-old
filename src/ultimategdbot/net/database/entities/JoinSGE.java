package ultimategdbot.net.database.entities;

import sx.blah.discord.handle.obj.IUser;

/**
 * Represents a Discord user joining a stat grind event
 * 
 * @author Alex1304
 *
 */
public class JoinSGE {

	/**
	 * The user joining the event
	 */
	private IUser user;
	
	/**
	 * The ID of the event itself
	 */
	private long sge;
	
	/**
	 * Timestamp of when the user joined
	 */
	private long joinDate;
	
	/**
	 * The current stat of the user when he joins the event
	 */
	private long currStat;
	
	/**
	 * Constructor specifying all attributes
	 * 
	 * @param user - the user joining the event
	 * @param sge - the ID of the said event
	 */
	public JoinSGE(IUser user, long sge, long joinDate, long currStat) {
		this.user = user;
		this.sge = sge;
		this.joinDate = joinDate;
		this.currStat = currStat;
	}

	/**
	 * Gets the user
	 * 
	 * @return IUser
	 */
	public IUser getUser() {
		return user;
	}

	/**
	 * Gets the ID of the stat grind event
	 * 
	 * @return StatGrindEvent
	 */
	public long getSge() {
		return sge;
	}

	/**
	 * Gets the current stat of the user when he joins the event.
	 * 
	 * @return long
	 */
	public long getCurrStat() {
		return currStat;
	}

	/**
	 * Gets the timestamp of when the user joined
	 * 
	 * @return long
	 */
	public long getJoinDate() {
		return joinDate;
	}
}
