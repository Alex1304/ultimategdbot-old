package ultimategdbot.commands.impl.subcommands;

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

public class SetupEditSubCommand extends SubCommand<SetupCommand> {

	public SetupEditSubCommand(SetupCommand parentCommand) {
		super(parentCommand);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		GuildSettingsDAO gsdao = this.getParentCommand().getGsdao();
		GuildSettingsAsObject gso = this.getParentCommand().getGso();
		
		String invalidValue = null;
		switch (args.get(0)) {
			case "gdevents_subscriber_role":
				gso.setGdEventsSubscriberRole(AppTools.stringToRole(args.get(1), event.getGuild()));
				if (gso.getGdEventsSubscriberRole() == null)
					invalidValue = "This role could not be found";
				break;
			case "gdevents_announcements_channel":
				gso.setGdEventsAnnouncementChannel(AppTools.stringToChannel(args.get(1), event.getGuild()));
				if (gso.getGdEventsAnnouncementChannel() == null)
					invalidValue = "This channel could not be found";
				break;
			default:
				throw new CommandFailedException("\"" + args.get(1) + "\" is not a setting name! "
						+ "Type `g!setup` without arguments to see the available settings.\n ");
		}

		if (invalidValue != null)
			throw new CommandFailedException(invalidValue);

		gsdao.update(gso.getGuildSettingsDBEntity());
		AppTools.sendMessage(event.getChannel(), ":white_check_mark: Settings updated!");
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
