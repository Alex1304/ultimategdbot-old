package ultimategdbot.commands.impl;
import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.app.Main;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.DiscordCommandHandler;
import ultimategdbot.commands.CoreCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.util.AppTools;

public class HelpCommand extends CoreCommand {

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		String helpMsg = "**__General Commands:__**\n";
		for (CoreCommand comm : DiscordCommandHandler.commandMap.values())
			helpMsg += comm.getHelp();
		
		helpMsg += "\n__**Administrator commands** (you need the Administrator permission in this server to run them):__\n";
		for (CoreCommand comm : DiscordCommandHandler.adminCommandMap.values())
			helpMsg += comm.getHelp();
		
		if (event.getAuthor().equals(Main.superadmin)) {
			helpMsg += "\n__**Superadmin commands** (only the big boss "
					+ Main.superadmin.getName() + "#" + Main.superadmin.getDiscriminator() + " can run them :smirk:):__\n";
			for (CoreCommand comm : DiscordCommandHandler.superadminCommandMap.values())
				helpMsg += comm.getHelp();
		}
		
		AppTools.sendMessage(event.getChannel(), helpMsg);
	}

	@Override
	public String getHelp() {
		return "`g!help` - Displays this message\n";
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
