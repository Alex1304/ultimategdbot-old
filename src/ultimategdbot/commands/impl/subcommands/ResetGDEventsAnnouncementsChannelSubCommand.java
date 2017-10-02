package ultimategdbot.commands.impl.subcommands;

import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.SubCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.util.GuildSettingsAsObject;

public class ResetGDEventsAnnouncementsChannelSubCommand extends SubCommand<SetupResetSubCommand> {

	public ResetGDEventsAnnouncementsChannelSubCommand(SetupResetSubCommand parentCommand) {
		super(parentCommand);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		GuildSettingsAsObject gso = this.getParentCommand().getParentCommand().getGso();
		gso.setGdEventsAnnouncementChannel(null);
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		// TODO Auto-generated method stub
		return null;
	}

}
