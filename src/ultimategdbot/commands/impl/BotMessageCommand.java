package ultimategdbot.commands.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.CoreCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.exceptions.InvalidCharacterException;
import ultimategdbot.net.geometrydash.GDServer;
import ultimategdbot.net.geometrydash.GDUserFactory;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.BotRoles;

/**
 * This command allows the user to send a message using the bot through the private messaging system in Geometry Dash.
 * 
 * @author amiranda
 *
 */
public class BotMessageCommand extends CoreCommand {

	public BotMessageCommand(EnumSet<BotRoles> rolesRequired) {
		super("botmessage", rolesRequired);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {final int indexOfBody = args.indexOf("--body");
		final int indexOfSubject = args.indexOf("--subject");
		final int indexOfTo = args.indexOf("--to");
		
		// Checks if the three fields exist
		if (indexOfBody == -1 || indexOfSubject == -1 || indexOfTo == -1)
			throw new CommandFailedException(this);
		// Checks if the fields aren't duplicated
		if (indexOfBody != args.lastIndexOf("--body") || indexOfSubject != args.lastIndexOf("--subject")
				|| indexOfTo != args.lastIndexOf("--to"))
			throw new CommandFailedException(this);
		
		if (indexOfBody + 1 >= args.size() || indexOfSubject + 1 >= args.size() || indexOfTo + 1 >= args.size())
			throw new CommandFailedException(this);
		
		if (Math.abs(indexOfTo - indexOfSubject) < 2 || Math.abs(indexOfTo - indexOfBody) < 2 ||
				Math.abs(indexOfBody - indexOfSubject) < 2)
			throw new CommandFailedException(this);
		
		List<Integer> indexes = new ArrayList<>();
		indexes.add(indexOfBody);
		indexes.add(indexOfSubject);
		indexes.add(indexOfTo);
		Collections.sort(indexes);
		
		String to = AppTools.concatCommandArgs(args.subList(indexOfTo + 1,
				indexes.indexOf(indexOfTo) + 1 < indexes.size() ? indexes.get(indexes.indexOf(indexOfTo) + 1)
						: args.size()));
		String subject = AppTools.concatCommandArgs(args.subList(indexOfSubject + 1,
				indexes.indexOf(indexOfSubject) + 1 < indexes.size() ? indexes.get(indexes.indexOf(indexOfSubject) + 1)
						: args.size()));
		String body = AppTools.concatCommandArgs(args.subList(indexOfBody + 1,
				indexes.indexOf(indexOfBody) + 1 < indexes.size() ? indexes.get(indexes.indexOf(indexOfBody) + 1)
						: args.size()));
		
		try {
			long recipientID = GDUserFactory.findAccountIDForGDUser(to);
			if (recipientID == -1)
				throw new CommandFailedException("The user you're trying to send the message to couldn't be found on "
						+ "Geometry Dash servers.");
			int retCode = Integer.parseInt(GDServer.sendMessageFromBotToGDUser(recipientID, subject, body)
					.replace("\n", ""));
			if (retCode == 1)
				AppTools.sendMessage(event.getChannel(), ":white_check_mark: Message sent!");
			else
				throw new CommandFailedException("Hum... Something went wrong when trying to send the message. Please "
						+ "check the following:\n- Your recipient has direct messages enabled for everyone\n"
						+ "- Your recipient hasn't blocked you on Geometry Dash\n"
						+ "- Your subject and body are not too long (try to respect Geometry Dash's character limit"
						+ " to avoid any issues)");
		} catch (IOException e) {
			throw new CommandFailedException("Unable to communicate with Geometry Dash servers at the moment. "
					+ "Please try again later.");
		} catch (InvalidCharacterException e) {
			throw new CommandFailedException("Your message contains unsupported characters. Note that you can't "
					+ "use accentuated characters in GD messages!");
		}
	}

	@Override
	public String getHelp() {
		return "Sends a private message to any Geometry Dash user from the bot.";
	}

	@Override
	public String[] getSyntax() {
		String[] syn = { "--to <gd_username_or_@mention> --subject <message_subject> --body <message_body>" };
		return syn;
	}

	@Override
	public String[] getExamples() {
		String[] ex = { "--to Alex1304 --subject Welcome message --body Hello, what's up?" };
		return ex;
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
