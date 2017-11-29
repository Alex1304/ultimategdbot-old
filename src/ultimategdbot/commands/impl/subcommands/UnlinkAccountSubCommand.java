package ultimategdbot.commands.impl.subcommands;

import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.app.Main;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.SubCommand;
import ultimategdbot.commands.impl.AccountCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.net.database.dao.UserSettingsDAO;
import ultimategdbot.net.database.entities.UserSettings;
import ultimategdbot.util.AppTools;

/**
 * Allows the user to unlink his Geometry Dash account.
 * Generates a short confirmation code sent directly in chat so the user
 * can confirm his action
 * 
 * @author alexandre
 *
 */
public class UnlinkAccountSubCommand extends SubCommand<AccountCommand> {
	
	private static final int UNLINK_CONFIRM_TOKEN_LENGTH = 5;

	public UnlinkAccountSubCommand(AccountCommand parentCommand) {
		super(parentCommand);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		UserSettingsDAO usdao = new UserSettingsDAO();
		UserSettings us = usdao.findOrCreate(event.getAuthor().getLongID());
		
		if (!us.isLinkActivated())
			throw new CommandFailedException("You are not yet linked to any GD account.");
		
		String token = AppTools.generateAlphanumericToken(UNLINK_CONFIRM_TOKEN_LENGTH);
		us.setConfirmationToken(token);
		usdao.update(us);
		
		AppTools.sendMessage(event.getChannel(), ":warning: Are you sure you want to unlink your Geometry Dash account? "
				+ "If so, run `" + Main.CMD_PREFIX + getParentCommand().getName() + " "
				+ getParentCommand().getSyntax()[2].replace("confirmlink|", "").replace("<confirmation_code>", token)
				+ "` to confirm your action.");
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
