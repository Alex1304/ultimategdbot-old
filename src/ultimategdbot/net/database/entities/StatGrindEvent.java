package ultimategdbot.net.database.entities;

import sx.blah.discord.handle.obj.IGuild;
import ultimategdbot.net.geometrydash.Stat;

/**
 * Entity that represents one stat grind event.
 * 
 * @author Alex1304
 *
 */
public class StatGrindEvent {
	
	/**
	 * The event unique ID
	 */
	private long eventID;
	
	/**
	 * The guild where the event happens
	 */
	private IGuild guild;
	
	/**
	 * Timestamp of the beginning of the event
	 */
	private long dateBegin;
	
	/**
	 * Timestamp of the end of the event
	 */
	private long dateEnd;
	
	/**
	 * The type of stats the players are supposed to grind during this event.
	 */
	private Stat statType;

	/**
	 * Constructs a new instance with all attributes provided.
	 * 
	 * @param eventID
	 *            - the event unique ID
	 * @param guild
	 *            - the guild where the event happens
	 * @param dateBegin
	 *            - timestamp of the beginning of the event
	 * @param dateEnd
	 *            - timestamp of the end of the event
	 * @param statType
	 *            - the type of stats the players are supposed to grind during
	 *            this event.
	 */
	public StatGrindEvent(long eventID, IGuild guild, long dateBegin, long dateEnd, Stat statType) {
		this.eventID = eventID;
		this.guild = guild;
		this.dateBegin = dateBegin;
		this.dateEnd = dateEnd;
		this.statType = statType;
	}

	/**
	 * Gets the event unique ID
	 * 
	 * @return long
	 */
	public long getEventID() {
		return eventID;
	}

	/**
	 * Gets the guild where the event happens
	 * 
	 * @return IGuild
	 */
	public IGuild getGuild() {
		return guild;
	}

	/**
	 * Gets the timestamp of the beginning of the event
	 * 
	 * @return long
	 */
	public long getDateBegin() {
		return dateBegin;
	}

	/**
	 * Gets the timestamp of the end of the event
	 * 
	 * @return long
	 */
	public long getDateEnd() {
		return dateEnd;
	}

	/**
	 * Gets the type of stats the players are supposed to grind during this
	 * event.
	 * 
	 * @return Stat
	 */
	public Stat getStatType() {
		return statType;
	}

}
