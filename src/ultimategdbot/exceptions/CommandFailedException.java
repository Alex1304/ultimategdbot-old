package ultimategdbot.exceptions;

import ultimategdbot.app.Main;
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
		String syntax = "";
		for (String s : cmd.getSyntax())
			syntax += "`"+ Main.CMD_PREFIX + cmd.getName() + (s.isEmpty() ? "" : " ") + s + "`\n";
		
		this.failureReason = "Incorrect usage!\n" + syntax + "\n" + "Run `" + Main.CMD_PREFIX + "help "
			+ cmd.getName() + "` " + "if you have any trouble with this command.";
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
