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
import ultimategdbot.exceptions.InvalidCharacterException;
import ultimategdbot.net.database.dao.UserSettingsDAO;
import ultimategdbot.net.database.entities.UserSettings;
import ultimategdbot.net.geometrydash.GDServer;
import ultimategdbot.net.geometrydash.GDUserFactory;
import ultimategdbot.util.AppTools;

public class LinkAccountSubCommand extends SubCommand<AccountCommand> {
	
	private static final String LINK_MESSAGE_SUBJECT = "Discord account link confirmation";
	private static final int TOKEN_LENGTH = 8;

	public LinkAccountSubCommand(AccountCommand parentCommand) {
		super(parentCommand);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		if (args.isEmpty())
			throw new CommandFailedException(this.getParentCommand());
		
		try {
			UserSettingsDAO usdao = new UserSettingsDAO();
			UserSettings us = usdao.findOrCreate(event.getAuthor().getLongID());
			
			if (us.isLinkActivated())
				throw new CommandFailedException("You are already linked to a GD account! Please unlink your existing one "
						+ "if you want to link another GD account.");
			
			long recipientID = GDUserFactory.findAccountIDForGDUser(args.get(0));
			
			String token = AppTools.generateAlphanumericToken(TOKEN_LENGTH);
			us.setConfirmationToken(token);
			us.setGdUserID(recipientID);
			usdao.update(us);
		
			if (recipientID == -1)
				throw new CommandFailedException("This user couldn't be found on Geometry Dash servers.");
			int retCode = Integer.parseInt(GDServer.sendMessageFromBotToGDUser(recipientID, LINK_MESSAGE_SUBJECT,
					confirmMessageBody(token)).replace("\n", ""));
			if (retCode == 1)
				AppTools.sendMessage(event.getChannel(), ":white_check_mark: A confirmation message has been sent to "
						+ "the GD account '" + args.get(0) + "'. It contains a confirmation code that you are supposed "
						+ "to enter through the command `" + Main.CMD_PREFIX + getParentCommand().getName() + " "
						+ getParentCommand().getSyntax()[1].replace("|confirmunlink", "") + "`. If you deleted the "
						+ "message by accident, you can repeat this command and a new confirmation code will be "
						+ "sent to you.");
			else
				throw new CommandFailedException("Hum... Something went wrong when trying to send the confirmation "
						+ "message to the Geometry Dash user '" + args.get(0) + "'. Please "
						+ "check the following:\n- This user has direct messages enabled for everyone\n"
						+ "- This user hasn't blocked 'UltimateGDBot' on Geometry Dash\n");
		} catch (IOException|NumberFormatException e) {
			throw new CommandFailedException("Unable to communicate with Geometry Dash servers at the moment. "
					+ "Please try again later.");
		} catch (InvalidCharacterException e) {
			throw new CommandFailedException("Character encoding error.");
		}
	}
	
	private static String confirmMessageBody(String token) {
		return "Here is your confirmation code to link your GD and Discord accounts : " + token + ". Note : if this message was unsolicited, "
				+ "please ignore/delete it. If you are getting spammed by unsolicited messages from me, you are free to block me.";
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}