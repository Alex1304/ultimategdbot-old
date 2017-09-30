package ultimategdbot.commands;

/**
 * Core commands are commands directly triggered by users, generally using a prefix.
 * 
 * @author Alex1304
 *
 */
public abstract class CoreCommand extends AbstractCommand {

	/**
	 * Gets the command help text
	 * 
	 * @return String containing the help text
	 */
	public abstract String getHelp();
}
