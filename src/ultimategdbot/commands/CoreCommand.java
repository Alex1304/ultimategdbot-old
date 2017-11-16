package ultimategdbot.commands;

import java.util.EnumSet;

import ultimategdbot.util.BotRoles;

/**
 * Core commands are commands directly triggered by users, generally using a prefix.
 * 
 * @author Alex1304
 *
 */
public abstract class CoreCommand extends AbstractCommand {
	
	protected String name;
	private EnumSet<BotRoles> rolesRequired;
	
	public CoreCommand(String name, EnumSet<BotRoles> rolesRequired) {
		this.name = name;
		this.rolesRequired = rolesRequired;
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
	
	/**
	 * Get {@link EnumSet} rolesRequired
	 * 
	 * @return EnumSet<BotRoles> the roles required to execute this command
	 */
	public EnumSet<BotRoles> getRolesRequired() {
		return rolesRequired;
	}
}
