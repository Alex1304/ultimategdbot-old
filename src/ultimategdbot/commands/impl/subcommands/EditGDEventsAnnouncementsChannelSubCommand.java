package ultimategdbot.commands.impl.subcommands;

import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.SubCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.GuildSettingsAsObject;

public class EditGDEventsAnnouncementsChannelSubCommand extends SubCommand<SetupEditSubCommand> {

	public EditGDEventsAnnouncementsChannelSubCommand(SetupEditSubCommand parentCommand) {
		super(parentCommand);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		GuildSettingsAsObject gso = this.getParentCommand().getParentCommand().getGso();
		gso.setGdEventsAnnouncementChannel(AppTools.stringToChannel(args.get(0), event.getGuild()));
		if (gso.getGdEventsAnnouncementChannel() == null)
			throw new CommandFailedException("This channel could not be found");
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
