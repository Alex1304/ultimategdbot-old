package ultimategdbot.commands.impl.subcommands;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.app.Main;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.SubCommand;
import ultimategdbot.commands.impl.AccountCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.exceptions.RawDataMalformedException;
import ultimategdbot.net.database.dao.impl.UserSettingsDAO;
import ultimategdbot.net.database.entities.UserSettings;
import ultimategdbot.net.geometrydash.GDMessage;
import ultimategdbot.net.geometrydash.GDMessageFactory;
import ultimategdbot.net.geometrydash.GDServer;
import ultimategdbot.net.geometrydash.GDUser;
import ultimategdbot.util.AppTools;

/**
 * Confirms account linking using a confirmation code sent to the user's GD account.
 * 
 * @author Alex1304
 *
 */
public class ActivateAccountSubCommand extends SubCommand<AccountCommand> {
	
	public static final String CONFIRM_SUBJECT = "confirm";

	public ActivateAccountSubCommand(AccountCommand parentCommand) {
		super(parentCommand);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		UserSettingsDAO usdao = new UserSettingsDAO();
		UserSettings us = usdao.findOrCreate(event.getAuthor().getLongID());
		GDUser userInstance = us.getGDUserInstance();
		
		if (us.isLinkActivated())
			throw new CommandFailedException("You are already linked to a GD account.");
		
		if (us.getConfirmationToken() == null ||
				us.getConfirmationToken().isEmpty() || userInstance == null)
			throw new CommandFailedException("You didn't request a link confirmation code yet. Please run `"
					+ Main.CMD_PREFIX + getParentCommand().getName() + " "
					+ getParentCommand().getSyntax()[0] + "` to get one first.");
		
		List<GDMessage> inbox = null;
		
		try {
			inbox = GDMessageFactory.buildAllGDMessagesFromFirstPage(GDServer.fetchBotPrivateMessages());
		} catch (RawDataMalformedException | IOException e) {
			e.printStackTrace();
			throw new CommandFailedException("I was unable to read my messages in Geometry Dash, probably because "
					+ "GD servers are down at the moment. Please try again later.");
		}
		
		Optional<GDMessage> confirmMsg = inbox.stream().filter(message -> message.getAuthor().equals(userInstance) &&
				message.getSubject().equalsIgnoreCase(CONFIRM_SUBJECT)).findFirst();
		
		if (!confirmMsg.isPresent())
			throw new CommandFailedException("I can't find your confirmation message. Make sure you've successfully sent"
					+ " it to account 'UltimateGDBot' in the game.");
		
		GDMessage confirmMsgWithBody = null;
		
		try {
			 confirmMsgWithBody = GDMessageFactory.buildOneGDMessage(
					GDServer.downloadBotPrivateMessageByID(confirmMsg.get().getId()));
		} catch (RawDataMalformedException | IOException e) {
			e.printStackTrace();
			throw new CommandFailedException("I was unable to read my messages in Geometry Dash, probably because "
					+ "GD servers are down at the moment. Please try again later.");
		}
		
		if (!us.getConfirmationToken().equals(confirmMsgWithBody.getBody()))
			throw new CommandFailedException("The code you sent to my Geometry Dash private messages don't match with "
					+ "the one I sent to you previously here. Please try again.\nIf you want me to send you a new "
					+ "confirmation code, run `" + Main.CMD_PREFIX + getParentCommand().getName() + " "
					+ getParentCommand().getSyntax()[0] + "` again.");
		
		us.setConfirmationToken(null);
		us.setLinkActivated(true);
		usdao.update(us);
		
		String gdusername = us.getGDUserInstance() != null ? us.getGDUserInstance().getName() : "(unknown)";
		AppTools.sendMessage(event.getChannel(), ":white_check_mark: Success! You are now linked to the "
				+ "Geometry Dash account **" + gdusername + "** ! You now have access to the following:\n"
				+ "-> You can use `" + Main.CMD_PREFIX + "profile` and `" + Main.CMD_PREFIX + "checkmod` "
				+ "without arguments to show your own info!\n"
				+ "-> Others can directly @ mention you to display your stats/mod status instead of "
				+ "searching for your in-game name!\n"
				+ "-> You will now appear in server leaderboards! Use `" + Main.CMD_PREFIX + "help leaderboard`"
				+ "for details.");
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
