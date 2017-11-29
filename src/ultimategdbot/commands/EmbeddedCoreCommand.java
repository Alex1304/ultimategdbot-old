package ultimategdbot.commands;

import java.util.Map;

/**
 * Adds extra verifications to the command before running it.
 * More generally used to perform a certain action before, after, 
 * or instead of the action that the original command was supposed to
 * perform.
 * 
 * @author Alex1304
 *
 */
public abstract class EmbeddedCoreCommand extends CoreCommand {
	
	protected CoreCommand cmd;

	public EmbeddedCoreCommand(CoreCommand cmd) {
		super(cmd.getName(), cmd.getRolesRequired());
		this.cmd = cmd;
		this.setSubCommandMap(cmd.getSubCommandMap());
	}

	@Override
	public String getHelp() {
		return cmd.getHelp();
	}

	@Override
	public String[] getSyntax() {
		return cmd.getSyntax();
	}

	@Override
	public String[] getExamples() {
		return cmd.getExamples();
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}