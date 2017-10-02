package ultimategdbot.commands.impl.subcommands;

import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.SubCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.util.GuildSettingsAsObject;

public class ResetGDEventsSubscriberRoleSubCommand extends SubCommand<SetupResetSubCommand> {

	public ResetGDEventsSubscriberRoleSubCommand(SetupResetSubCommand parentCommand) {
		super(parentCommand);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		GuildSettingsAsObject gso = this.getParentCommand().getParentCommand().getGso();
		gso.setGdEventsSubscriberRole(null);
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
