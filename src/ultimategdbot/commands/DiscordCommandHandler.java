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
import ultimategdbot.app.Main;
import ultimategdbot.commands.impl.AccountCommand;
import ultimategdbot.commands.impl.AnnouncementCommand;
import ultimategdbot.commands.impl.BotMessageCommand;
import ultimategdbot.commands.impl.ChangeBotUsernameCommand;
import ultimategdbot.commands.impl.CheckModCommand;
import ultimategdbot.commands.impl.CompareCommand;
import ultimategdbot.commands.impl.GDEventsCommand;
import ultimategdbot.commands.impl.HelpCommand;
import ultimategdbot.commands.impl.InviteCommand;
import ultimategdbot.commands.impl.KillCommand;
import ultimategdbot.commands.impl.LevelCommand;
import ultimategdbot.commands.impl.ModListCommand;
import ultimategdbot.commands.impl.PingCommand;
import ultimategdbot.commands.impl.ProfileCommand;
import ultimategdbot.commands.impl.RestartCommand;
import ultimategdbot.commands.impl.ServerCountCommand;
import ultimategdbot.commands.impl.SetupCommand;
import ultimategdbot.commands.impl.ShutdownCommand;
import ultimategdbot.commands.impl.UpdateModListCommand;
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
		registerCommand(new SingleRunningCommand(new UpdateModListCommand(EnumSet.of(BotRoles.SUPERADMIN))));
		registerCommand(new ShutdownCommand(EnumSet.of(BotRoles.SUPERADMIN)));
		
		// Moderators commands
		registerCommand(new RestartCommand(EnumSet.of(BotRoles.MODERATOR)));
		registerCommand(new KillCommand(EnumSet.of(BotRoles.BETA_TESTER)));
		
		// Server admin commands
		registerCommand(new ServerOnlyCommand(new SetupCommand(EnumSet.of(BotRoles.SERVER_ADMIN))));
		registerCommand(new ModListCommand(EnumSet.of(BotRoles.SERVER_ADMIN)));
		
		// Beta-testers commands
		registerCommand(new ServerCountCommand(EnumSet.of(BotRoles.USER)));
		registerCommand(new ModListCommand(EnumSet.of(BotRoles.USER)));
		registerCommand(new CheckModCommand(EnumSet.of(BotRoles.USER)));
		
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
		
		String[] argArray = event.getMessage().getContent().split(" ");

		if (argArray.length == 0)
			return;

		if (!argArray[0].startsWith(CMD_PREFIX))
			return;

		String commandStr = argArray[0].substring(CMD_PREFIX.length()).toLowerCase();

		List<String> argsList = new ArrayList<>(Arrays.asList(argArray));
		argsList.remove(0);
		
		Main.THREADS.addExistingThread(generateThreadName(event), new CommandThread(event, commandStr, argsList));
		Main.THREADS.startIfNew(generateThreadName(event));
	}
	
	public static String generateThreadName(MessageReceivedEvent event) {
		return "command_m" + event.getMessageID() + "_c" + event.getChannel().getLongID()
		+ "_a" + event.getAuthor().getLongID();
	}
}
