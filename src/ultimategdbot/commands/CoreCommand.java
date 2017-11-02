package ultimategdbot.commands;

/**
 * Core commands are commands directly triggered by users, generally using a prefix.
 * 
 * @author Alex1304
 *
 */
public abstract class CoreCommand extends AbstractCommand {
	
	private String name;
	
	public CoreCommand(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the command help text
	 * 
	 * @return String containing the help text
	 */
	public abstract String getHelp();

	/**
	 * Gets the command example arguments
	 * 
	 * @return String containing the help text
	 */
	public abstract String[] getSyntax();

	/**
	 * Gets the command usage examples
	 * 
	 * @return String containing the help text
	 */
	public abstract String[] getExamples();
	
	/**
	 * Get {@link CoreCommand} name
	 * 
	 * @return String name
	 */
	public String getName() {
		return name;
	}
}
