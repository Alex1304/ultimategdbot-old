package commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.RequestBuffer;

/**
 * Test command
 * 
 * @author alexandre
 *
 */
public class TestCommand implements Command {

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) {
        RequestBuffer.request(() -> {
    		event.getChannel().sendMessage("Hello World!");
        });
	}

}
