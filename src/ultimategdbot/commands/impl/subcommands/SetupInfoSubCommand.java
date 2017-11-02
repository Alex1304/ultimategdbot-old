package ultimategdbot.commands.impl.subcommands;

import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.SubCommand;
import ultimategdbot.commands.impl.SetupCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.Settings;

public class SetupInfoSubCommand extends SubCommand<SetupCommand> {

	public SetupInfoSubCommand(SetupCommand parentCommand) {
		super(parentCommand);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		if (args.size() < 1)
			throw new CommandFailedException(this.getParentCommand());
		
		try {
			Settings settingToDisplay = Settings.valueOf(args.get(0).toUpperCase());
			AppTools.sendMessage(event.getChannel(), "`" + settingToDisplay.toString() + "` - "
					+ settingToDisplay.getInfo());
		} catch (IllegalArgumentException e) {
			throw new CommandFailedException("\"" + args.get(0) + "\" is not a setting name! "
				+ "Type `g!setup` without arguments to see the available settings.\n ");
		}
		
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
