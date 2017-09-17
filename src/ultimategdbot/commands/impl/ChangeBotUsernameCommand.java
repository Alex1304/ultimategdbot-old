package ultimategdbot.commands.impl;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.app.AppTools;
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
			AppTools.sendMessage(event.getChannel(), ":negative_squared_cross_mark: Username is empty!");
		else {
			Main.client.changeUsername(newName);
			AppTools.sendMessage(event.getChannel(), ":white_check_mark: Bot username changed!");
		}
	}

}
