package ultimategdbot.commands.impl;

import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.app.Main;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.SuperadminCoreCommand;
import ultimategdbot.util.AppTools;

/**
 * Command to change the bot's usernamme
 * 
 * @author Alex1304
 *
 */
public class ChangeBotUsernameCommand extends SuperadminCoreCommand {

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

	@Override
	public String getHelp() {
		return "`g!changebotusername <new_name>` - Changes the bot username\n";
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
