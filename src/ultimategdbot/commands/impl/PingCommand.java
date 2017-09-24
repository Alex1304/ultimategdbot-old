package ultimategdbot.commands.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.app.AppTools;
import ultimategdbot.commands.Command;

/**
 * Ping command
 * 
 * @author Alex1304
 *
 */
public class PingCommand implements Command {

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

}
