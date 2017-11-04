package ultimategdbot.commands;

import java.util.Map;

public abstract class EmbeddedCoreCommand extends CoreCommand {
	
	protected CoreCommand cmd;

	public EmbeddedCoreCommand(CoreCommand cmd) {
		super(cmd.getName());
		this.cmd = cmd;
		this.setSubCommandMap(initSubCommandMap());
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
		if (cmd == null)
			return null;
		return cmd.initSubCommandMap();
	}

}