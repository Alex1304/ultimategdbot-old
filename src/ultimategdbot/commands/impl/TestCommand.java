package ultimategdbot.commands.impl;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.app.AppTools;
import ultimategdbot.commands.Command;

/**
 * Test command
 * 
 * @author Alex1304
 *
 */
public class TestCommand implements Command {

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) {
        AppTools.sendMessage(event.getChannel(), "Hello World!");
	}

}
