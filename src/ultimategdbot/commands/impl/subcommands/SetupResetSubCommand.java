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
import ultimategdbot.net.database.dao.GuildSettingsDAO;
import ultimategdbot.net.database.entities.GuildSettings;
import ultimategdbot.util.AppTools;

/**
 * Allows to reset a specific setting field in bot server setup
 * 
 * @author alexandre
 *
 */
public class SetupResetSubCommand extends SubCommand<SetupCommand> {

	public SetupResetSubCommand(SetupCommand parentCommand) {
		super(parentCommand);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		if (args.size() != 1)
			throw new CommandFailedException(this.getParentCommand());
		
		GuildSettingsDAO gsdao = this.getParentCommand().getGsdao();
		GuildSettings settings = this.getParentCommand().getSettings();
		Class<? extends GuildSetting<?>> targetSettingClass = GuildSettingNames.get(args.get(0));
		
		if (targetSettingClass == null)
			throw new CommandFailedException("\"" + args.get(0) + "\" is not a setting name! Please run "
					+ Main.CMD_PREFIX + this.getParentCommand().getName() + " without arguments to see the list "
					+ "of settings");
		
		settings.getSetting(targetSettingClass).resetValue();

		gsdao.update(settings);
		AppTools.sendMessage(event.getChannel(), ":white_check_mark: The setting `" + args.get(0) + "` has been reset!");
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
