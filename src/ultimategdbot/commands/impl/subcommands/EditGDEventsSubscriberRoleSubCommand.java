package ultimategdbot.commands.impl.subcommands;

import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.SubCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.GuildSettingsAsObject;

public class EditGDEventsSubscriberRoleSubCommand extends SubCommand<SetupEditSubCommand> {

	public EditGDEventsSubscriberRoleSubCommand(SetupEditSubCommand parentCommand) {
		super(parentCommand);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		GuildSettingsAsObject gso = this.getParentCommand().getParentCommand().getGso();
		gso.setGdEventsSubscriberRole(AppTools.stringToRole(args.get(0), event.getGuild()));
		if (gso.getGdEventsSubscriberRole() == null)
			throw new CommandFailedException("This role could not be found");
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
