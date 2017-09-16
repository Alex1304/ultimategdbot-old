package commands;

import java.util.List;

import app.Main;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.RequestBuffer;

public class ChangeBotUsernameCommand implements Command {

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandUsageDeniedException {
		if (event.getAuthor().getLongID() != 272872694473687041L)
			throw new CommandUsageDeniedException();
		
		String newName = "";
		for (String arg : args)
			newName += (arg + " ");
		
		if (newName.isEmpty())
			RequestBuffer.request(() -> {
	    		event.getChannel().sendMessage(":negative_squared_cross_mark: Username is empty!");
	        });
		else {
			Main.client.changeUsername(newName);
			RequestBuffer.request(() -> {
	    		event.getChannel().sendMessage(":white_check_mark: Bot username changed!");
	        });
		}
	}

}
