package ultimategdbot.commands;

import java.util.EnumSet;

import ultimategdbot.util.BotRoles;

/**
 * Core commands are commands directly triggered by users, generally using a prefix.
 * Those commands are supposed to show up in the help menu, and don't have any
 * parent command.
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
	 * Get the name of the command
	 * 
	 * @return String name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the {@link EnumSet} of roles required to run this command
	 * 
	 * @return EnumSet<BotRoles> the roles required
	 */
	public EnumSet<BotRoles> getRolesRequired() {
		return rolesRequired;
	}
}
