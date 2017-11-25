package ultimategdbot.commands.impl.subcommands;

import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.app.Main;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.SubCommand;
import ultimategdbot.commands.impl.SetupCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.guildsettings.GuildSetting;
import ultimategdbot.guildsettings.GuildSettingNames;
import ultimategdbot.net.database.entities.GuildSettings;
import ultimategdbot.util.AppTools;

public class SetupInfoSubCommand extends SubCommand<SetupCommand> {

	public SetupInfoSubCommand(SetupCommand parentCommand) {
		super(parentCommand);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		if (args.size() < 1)
			throw new CommandFailedException(this.getParentCommand());
		
		GuildSettings settings = this.getParentCommand().getSettings();
		Class<? extends GuildSetting<?>> targetSettingClass = GuildSettingNames.get(args.get(0));
		
		if (targetSettingClass == null)
			throw new CommandFailedException("\"" + args.get(0) + "\" is not a setting name! Please run "
					+ Main.CMD_PREFIX + this.getParentCommand().getName() + " without arguments to see the list "
					+ "of settings");
		
		AppTools.sendMessage(event.getChannel(), "`" + args.get(0) + "` - "
				+ settings.getSetting(targetSettingClass).getInfo());
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
