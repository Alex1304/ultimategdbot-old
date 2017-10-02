package ultimategdbot.commands.impl.subcommands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.SubCommand;
import ultimategdbot.commands.impl.SetupCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.net.database.dao.GuildSettingsDAO;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.GuildSettingsAsObject;
import ultimategdbot.util.Settings;

public class SetupResetSubCommand extends SubCommand<SetupCommand> {

	public SetupResetSubCommand(SetupCommand parentCommand) {
		super(parentCommand);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		if (args.size() != 1)
			throw new CommandFailedException(this.getParentCommand());
		
		GuildSettingsDAO gsdao = this.getParentCommand().getGsdao();
		GuildSettingsAsObject gso = this.getParentCommand().getGso();
		
		if (!triggerSubCommand(args.get(0), event, args.subList(1, args.size())))
			throw new CommandFailedException("\"" + args.get(0) + "\" is not a setting name! "
				+ "Type `g!setup` without arguments to see the available settings.\n ");

		gsdao.update(gso.getGuildSettingsDBEntity());
		AppTools.sendMessage(event.getChannel(), ":white_check_mark: The setting `" + args.get(0) + "` has been reset!");
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		Map<String, Command> subcommands = new HashMap<>();
		subcommands.put(Settings.GDEVENTS_SUBSCRIBER_ROLE.toString(), new ResetGDEventsSubscriberRoleSubCommand(this));
		subcommands.put(Settings.GDEVENTS_ANNOUNCEMENTS_CHANNEL.toString(), new ResetGDEventsAnnouncementsChannelSubCommand(this));
		return subcommands;
	}

}
