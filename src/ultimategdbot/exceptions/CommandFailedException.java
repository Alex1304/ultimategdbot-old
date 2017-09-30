package ultimategdbot.exceptions;

import ultimategdbot.commands.CoreCommand;

/**
 * Exception thrown when someone is triggering a command which he is not allowed to use
 * (for example a random user trying to execute a superadmin-only command)
 * 
 * @author Alex1304
 *
 */
public class CommandFailedException extends Exception {

	private static final long serialVersionUID = -1502808798318227533L;
	
	private String failureReason;
	
	public CommandFailedException(CoreCommand cmd) {
		this.failureReason = "Incorrect usage!\n " + cmd.getHelp();
	}
	
	public CommandFailedException(String failureReason) {
		this.failureReason = failureReason;
	}

	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}
	
}
