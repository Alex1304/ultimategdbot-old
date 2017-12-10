package ultimategdbot.net.geometrydash;

/**
 * Entity representing a Geometry Dash private message
 * 
 * @author Alex1304
 *
 */
public class GDMessage {
	
	/**
	 * Message ID
	 */
	private long id;
	
	/**
	 * User who sent the message
	 */
	private GDUser author;
	
	/**
	 * User who received the message
	 */
	private GDUser recipient;
	
	/**
	 * Message subject
	 */
	private String subject;
	
	/**
	 * Message body
	 */
	private String body;

	/**
	 * Constructs a new GDMessage with all fields provided
	 * 
	 * @param id
	 *            - message ID
	 * @param author
	 *            - user who sent the message
	 * @param recipient
	 *            - user who received the message
	 * @param subject
	 *            - message subject
	 * @param body
	 *            - message body
	 */
	public GDMessage(long id, GDUser author, GDUser recipient, String subject, String body) {
		this.id = id;
		this.author = author;
		this.recipient = recipient;
		this.subject = subject;
		this.body = body;
	}

	/**
	 * Gets the message ID
	 * 
	 * @return long
	 */
	public long getId() {
		return id;
	}

	/**
	 * Gets the user who sent the message
	 * 
	 * @return GDUser
	 */
	public GDUser getAuthor() {
		return author;
	}

	/**
	 * Gets the user who received the message
	 * 
	 * @return GDUser
	 */
	public GDUser getRecipient() {
		return recipient;
	}

	/**
	 * Gets the message subject
	 * 
	 * @return String
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Gets the message body
	 * @return String
	 */
	public String getBody() {
		return body;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof GDMessage))
			return false;
		GDMessage other = (GDMessage) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GDMessage [id=" + id + ", author=" + author + ", recipient=" + recipient + ", subject=" + subject
				+ ", body=" + body + "]\n";
	}
}
