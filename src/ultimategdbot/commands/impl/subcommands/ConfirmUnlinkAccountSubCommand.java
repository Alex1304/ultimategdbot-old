package ultimategdbot.commands.impl.subcommands;

import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.app.Main;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.SubCommand;
import ultimategdbot.commands.impl.AccountCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.net.database.dao.impl.UserSettingsDAO;
import ultimategdbot.net.database.entities.UserSettings;
import ultimategdbot.util.AppTools;

/**
 * Confirms the unlink action with a code sent directly in chat
 * 
 * @author Alex1304
 *
 */
public class ConfirmUnlinkAccountSubCommand extends SubCommand<AccountCommand> {

	public ConfirmUnlinkAccountSubCommand(AccountCommand parentCommand) {
		super(parentCommand);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		if (args.isEmpty())
			throw new CommandFailedException(this.getParentCommand());
		
		UserSettingsDAO usdao = new UserSettingsDAO();
		UserSettings us = usdao.findOrCreate(event.getAuthor().getLongID());
		
		if (!us.isLinkActivated())
			throw new CommandFailedException("You are not yet linked to any GD account.");
		
		if (us.getConfirmationToken() == null ||
				us.getConfirmationToken().isEmpty())
			throw new CommandFailedException("You didn't request an unlink confirmation code yet. Please run `"
					+ Main.CMD_PREFIX + getParentCommand().getName() + " " + getParentCommand().getSyntax()[0].replace("link|", "") + "`"
					+ " to get one first.");
		
		if (!us.getConfirmationToken().equals(args.get(0)))
			throw new CommandFailedException("The code you provided is wrong. Please try again.\nIf you want me to "
					+ "send you a new confirmation code, run`" + Main.CMD_PREFIX + getParentCommand().getName() + " "
					+ getParentCommand().getSyntax()[1] + "` again.");
		
		
		usdao.delete(us);
		AppTools.sendMessage(event.getChannel(), ":white_check_mark: You have successfully unlinked your account !");
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
