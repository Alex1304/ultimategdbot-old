package ultimategdbot.exceptions;

/**
 * Exception thrown when someone is triggering a command which he is not allowed to use
 * (for example a random user trying to execute a superadmin-only command)
 * 
 * @author Alex1304
 *
 */
public class CommandFailedException extends Exception {

	private static final long serialVersionUID = -1502808798318227533L;
	
	private String denialReason;
	
	public CommandFailedException(String denialReason) {
		this.denialReason = denialReason;
	}

	public String getDenialReason() {
		return denialReason;
	}

	public void setDenialReason(String denialReason) {
		this.denialReason = denialReason;
	}
	
}
