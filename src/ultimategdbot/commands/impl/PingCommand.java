package ultimategdbot.commands.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.CoreCommand;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.BotRoles;

/**
 * Used to check if the bot is correctly responding to commands. Displays the bot response time in
 * milliseconds.
 * 
 * @author Alex1304
 *
 */
public class PingCommand extends CoreCommand {

	public PingCommand(EnumSet<BotRoles> rolesRequired) {
		super("ping", rolesRequired);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) {
		long currMillis = LocalDateTime.now().getLong(ChronoField.MILLI_OF_DAY);
		long millis = currMillis - event.getMessage().getTimestamp().getLong(ChronoField.MILLI_OF_DAY);
        AppTools.sendMessage(event.getChannel(), "Pong! :ping_pong:\n"
        		+ "Response time: `" + millis + " ms`");
	}

	@Override
	public String getHelp() {
		return "Gives the bot response time in milliseconds";
	}

	@Override
	public String[] getSyntax() {
		return null;
	}

	@Override
	public String[] getExamples() {
		return null;
	}

	@Override
	public Map<String, Command> initSubCommandMap() {
		return null;
	}

}
