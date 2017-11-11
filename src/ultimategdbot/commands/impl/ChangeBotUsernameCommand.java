package ultimategdbot.commands.impl;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.app.Main;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.CoreCommand;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.BotRoles;

/**
 * Command to change the bot's usernamme
 * 
 * @author Alex1304
 *
 */
public class ChangeBotUsernameCommand extends CoreCommand {

	public ChangeBotUsernameCommand(EnumSet<BotRoles> rolesRequired) {
		super("changebotusername", rolesRequired);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) {
		String newName = "";
		for (String arg : args)
			newName += (arg + " ");
		
		if (newName.isEmpty())
			AppTools.sendMessage(event.getChannel(), ":negative_squared_cross_mark: Username is empty!");
		else {
			Main.DISCORD_ENV.getClient().changeUsername(newName);
			AppTools.sendMessage(event.getChannel(), ":white_check_mark: Bot username changed!");
		}
	}

	@Override
	public String getHelp() {
		return "Changes the bot username";
	}

	@Override
	public String[] getSyntax() {
		String[] res = { "<new_name>" };
		return res;
	}

	@Override
	public String[] getExamples() {
		String[] res = { "HyperCoolGDBot" };
		return res;
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
