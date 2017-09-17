package ultimategdbot.commands.impl;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.RequestBuffer;
import ultimategdbot.app.Main;
import ultimategdbot.commands.SuperadminCommand;

/**
 * Command to change the bot's usernamme
 * 
 * @author Alex1304
 *
 */
public class ChangeBotUsernameCommand extends SuperadminCommand {

	@Override
	public void runSuperadminCommand(MessageReceivedEvent event, List<String> args) {
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
