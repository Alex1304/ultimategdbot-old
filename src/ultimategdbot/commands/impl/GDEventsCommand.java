package ultimategdbot.commands.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.commands.Command;
import ultimategdbot.exceptions.CommandFailedException;

public class GDEventsCommand implements Command {
	
	public static Set<Long> subscribers = new HashSet<>();
	
	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		switch (args.get(0)) {
			case "subscribe":
				subscribers.add(event.getAuthor().getLongID());
				break;
			case "unsubscribe":
				subscribers.remove(event.getAuthor().getLongID());
				break;
			default:
				throw new CommandFailedException("Correct usage:\n`g!gdevents subscribe|unsubscribe awarded`");
		}
	}

}
