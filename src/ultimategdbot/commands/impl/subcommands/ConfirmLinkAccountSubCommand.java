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
import ultimategdbot.exceptions.RawDataMalformedException;
import ultimategdbot.net.database.dao.UserSettingsDAO;
import ultimategdbot.net.database.entities.UserSettings;
import ultimategdbot.net.geometrydash.GDServer;
import ultimategdbot.net.geometrydash.GDUserFactory;
import ultimategdbot.util.AppTools;

/**
 * Confirms account linking using a confirmation code sent to the user's GD account.
 * 
 * @author Alex1304
 *
 */
public class ConfirmLinkAccountSubCommand extends SubCommand<AccountCommand> {

	public ConfirmLinkAccountSubCommand(AccountCommand parentCommand) {
		super(parentCommand);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		if (args.isEmpty())
			throw new CommandFailedException(this.getParentCommand());
		
		UserSettingsDAO usdao = new UserSettingsDAO();
		UserSettings us = usdao.findOrCreate(event.getAuthor().getLongID());
		
		if (us.isLinkActivated())
			throw new CommandFailedException("You are already linked to a GD account.");
		
		if (us.getConfirmationToken() == null ||
				us.getConfirmationToken().isEmpty())
			throw new CommandFailedException("You didn't request a link confirmation code yet. Please run `"
					+ Main.CMD_PREFIX + getParentCommand().getName() + " "
					+ getParentCommand().getSyntax()[0] + "` to get one first.");
		
		if (!us.getConfirmationToken().equals(args.get(0)))
			throw new CommandFailedException("The code you provided don't match with the one I sent to your Geometry Dash "
					+ "private messages. Please try again.\nIf you want me to send you a new confirmation code, run `"
					+ Main.CMD_PREFIX + getParentCommand().getName() + " "
					+ getParentCommand().getSyntax()[0] + "` again.");
		
		us.setConfirmationToken(null);
		us.setLinkActivated(true);
		usdao.update(us);
		
		String gdusername;
		try {
			gdusername = GDUserFactory.buildGDUserFromProfileRawData(GDServer.fetchUserProfile(us.getGdUserID())).getName();
		} catch (RawDataMalformedException | IOException e) {
			gdusername = "(unknown)";
		}
		AppTools.sendMessage(event.getChannel(), ":white_check_mark: Success! You are now linked to the "
				+ "Geometry Dash account **" + gdusername + "** !");
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
