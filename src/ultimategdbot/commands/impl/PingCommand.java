package ultimategdbot.commands.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.CoreCommand;
import ultimategdbot.util.AppTools;

/**
 * Ping command
 * 
 * @author Alex1304
 *
 */
public class PingCommand extends CoreCommand {

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) {
		long currMillis = LocalDateTime.now().getLong(ChronoField.MILLI_OF_DAY);
		long millis = currMillis - event.getMessage().getTimestamp().getLong(ChronoField.MILLI_OF_DAY);
        AppTools.sendMessage(event.getChannel(), "Pong! :ping_pong:\n"
        		+ "Response time: `" + millis + " ms`");
	}

	@Override
	public String getHelp() {
		return "`g!ping` - Gives the bot response time in milliseconds\n";
	}

	@Override
	public Map<String, Command> initSubCommandMap() {
		return null;
	}

}
