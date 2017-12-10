package ultimategdbot.commands.impl.subcommands;

import java.io.IOException;
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
import ultimategdbot.net.geometrydash.GDUserFactory;
import ultimategdbot.util.AppTools;

/**
 * Generates a confirmation code and sends it to the requested GD user.
 * The command will fail if it hasn't permissions to send private messages
 * through GD to this user.
 * 
 * @author Alex1304
 *
 */
public class LinkAccountSubCommand extends SubCommand<AccountCommand> {
	
	private static final int TOKEN_LENGTH = 6;

	public LinkAccountSubCommand(AccountCommand parentCommand) {
		super(parentCommand);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		if (args.isEmpty())
			throw new CommandFailedException(this.getParentCommand());
		
		String concatArgs = AppTools.concatCommandArgs(args);
		UserSettingsDAO usdao = new UserSettingsDAO();
		UserSettings us = usdao.findOrCreate(event.getAuthor().getLongID());
		
		if (us.isLinkActivated())
			throw new CommandFailedException("You are already linked to a GD account.");
		
		try {
			long accountID = GDUserFactory.findAccountIDForGDUser(concatArgs);
			UserSettings usForAccID = usdao.findByGDUserID(accountID);
			if (usForAccID != null && usForAccID.isLinkActivated())
				throw new CommandFailedException("This Geometry Dash account has already been linked by someone else.");
			
			String token = AppTools.generateAlphanumericToken(TOKEN_LENGTH);
			us.setConfirmationToken(token);
			us.setGdUserID(accountID);
			usdao.update(us);

			String gdusername = us.getGDUserInstance() != null ? us.getGDUserInstance().getName() : "(unknown)";
			AppTools.sendMessage(event.getChannel(), ":white_check_mark: Account registered! One more step: you must confirm you "
					+ "are effectively the owner of the account **" + gdusername + "**.\n"
					+ "To do that, go on Geometry Dash, find the user 'UltimateGDBot' and send it a private message "
					+ "containing the following:\n"
					+ "```\n"
					+ "Subject: Confirm\n"
					+ "Body: " + token + "\n"
					+ "```\n"
					+ "Finally, run the command `" + Main.CMD_PREFIX + getParentCommand().getName() + " activate`"
					+ " to finalize the linking process!");
		} catch (IOException e) {
			throw new CommandFailedException("Unable to communicate with Geometry Dash servers at the moment."
					+ " Please try again later.");
		}
	}
	
	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
