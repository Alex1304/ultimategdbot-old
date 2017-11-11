package ultimategdbot.commands;

import static ultimategdbot.app.Main.CMD_PREFIX;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import ultimategdbot.commands.impl.AccountCommand;
import ultimategdbot.commands.impl.AnnouncementCommand;
import ultimategdbot.commands.impl.BotMessageCommand;
import ultimategdbot.commands.impl.ChangeBotUsernameCommand;
import ultimategdbot.commands.impl.CompareCommand;
import ultimategdbot.commands.impl.GDEventsCommand;
import ultimategdbot.commands.impl.ServerCountCommand;
import ultimategdbot.commands.impl.HelpCommand;
import ultimategdbot.commands.impl.InviteCommand;
import ultimategdbot.commands.impl.LevelCommand;
import ultimategdbot.commands.impl.PingCommand;
import ultimategdbot.commands.impl.ProfileCommand;
import ultimategdbot.commands.impl.RestartCommand;
import ultimategdbot.commands.impl.SetupCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.BotRoles;

/**
 * Bot commands are handled here, using the Discord API based on events
 * 
 * @author Alex1304
 *
 */
public class DiscordCommandHandler {

	/**
	 * Maps that associates text commands to their actions.
	 */
	public static final Map<String, CoreCommand> COMMAND_MAP = new HashMap<>();

	/**
	 * Constructor
	 */
	public DiscordCommandHandler() {
		loadCommandMap();
	}

	/**
	 * Loads the command map so they are recognized by the handler
	 */
	private void loadCommandMap() {
		// Superadmin commands
		registerCommand(new ChangeBotUsernameCommand(EnumSet.of(BotRoles.SUPERADMIN)));
		registerCommand(new AnnouncementCommand(EnumSet.of(BotRoles.SUPERADMIN)));
		registerCommand(new BotMessageCommand(EnumSet.of(BotRoles.SUPERADMIN)));
		
		// Moderators commands
		registerCommand(new RestartCommand(EnumSet.of(BotRoles.MODERATOR)));
		
		// Server admin commands
		registerCommand(new ServerOnlyCommand(new SetupCommand(EnumSet.of(BotRoles.SERVER_ADMIN))));
		
		// Beta-testers commands
		registerCommand(new ServerCountCommand(EnumSet.of(BotRoles.BETA_TESTER)));
		
		// Public commands
		registerCommand(new PingCommand(EnumSet.of(BotRoles.USER)));
		registerCommand(new ServerOnlyCommand(new GDEventsCommand(EnumSet.of(BotRoles.USER))));
		registerCommand(new HelpCommand(EnumSet.of(BotRoles.USER)));
		registerCommand(new InviteCommand(EnumSet.of(BotRoles.USER)));
		registerCommand(new LevelCommand(EnumSet.of(BotRoles.USER)));
		registerCommand(new CompareCommand(EnumSet.of(BotRoles.USER)));
		registerCommand(new AccountCommand(EnumSet.of(BotRoles.USER)));
		registerCommand(new ProfileCommand(EnumSet.of(BotRoles.USER)));
	}
	
	private void registerCommand(CoreCommand cmd) {
		COMMAND_MAP.put(cmd.getName(), cmd);
	}

	/**
	 * Handles messages sent in the guild and execute commands
	 * 
	 * @param event
	 */
	@EventSubscriber
	public void onMessageReceived(MessageReceivedEvent event) {
		// Note for error handling, you'll probably want to log failed commands
		// with a logger or sout
		// In most cases it's not advised to annoy the user with a reply incase
		// they didn't intend to trigger a
		// command anyway, such as a user typing ?notacommand, the bot should
		// not say "notacommand" doesn't exist in
		// most situations. It's partially good practise and partially developer
		// preference

		// Given a message "/test arg1 arg2", argArray will contain ["/test",
		// "arg1", "arg2"]
		String[] argArray = event.getMessage().getContent().split(" ");

		// First ensure at least the command and prefix is present, the arg
		// length can be handled by your command func
		if (argArray.length == 0)
			return;

		// Check if the first arg (the command) starts with the prefix defined
		// in the utils class
		if (!argArray[0].startsWith(CMD_PREFIX))
			return;

		// Extract the "command" part of the first arg out by just ditching the
		// first character
		String commandStr = argArray[0].substring(CMD_PREFIX.length()).toLowerCase();

		// Load the rest of the args in the array into a List for safer access
		List<String> argsList = new ArrayList<>(Arrays.asList(argArray));
		argsList.remove(0); // Remove the command

		try {
			if (COMMAND_MAP.containsKey(commandStr)) {
				if (BotRoles.isGrantedAll(event.getAuthor(), event.getChannel(), COMMAND_MAP.get(commandStr).getRolesRequired()))
					COMMAND_MAP.get(commandStr).runCommand(event, argsList);
				else
					throw new CommandFailedException("You don't have permission to use this command");
			}
		} catch (CommandFailedException e) {
			AppTools.sendMessage(event.getChannel(), ":negative_squared_cross_mark: " + e.getFailureReason());
		} catch (DiscordException e) {
			AppTools.sendMessage(event.getChannel(), ":negative_squared_cross_mark: Sorry, an error occured while running the command.\n```\n" + e.getErrorMessage() + "\n```");
			System.err.println(e.getErrorMessage());
		} catch (Exception e) {
			AppTools.sendMessage(event.getChannel(), "An internal error occured while running the command. Please try again "
					+ "later.");
			AppTools.sendDebugPMToSuperadmin(
					"An internal error occured in the command handler. See logs for more details\n"
							+ "Context info:\n"
							+ "```\n"
							+ "Guild: " + event.getGuild().getName() + " (" + event.getGuild().getLongID() + ")\n"
							+ "Channel: #" + event.getChannel().getName() + "\n"
							+ "Author: " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator()
									+ "(" + event.getAuthor().getLongID() + ")\n"
							+ "Full message: " + event.getMessage().getContent() + "\n"
							+ "```\n");
			e.printStackTrace();
		}
	}
}
