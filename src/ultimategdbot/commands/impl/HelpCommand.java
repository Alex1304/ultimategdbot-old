package ultimategdbot.commands.impl;
import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.app.AppTools;
import ultimategdbot.app.Main;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.CommandHandler;
import ultimategdbot.exceptions.CommandFailedException;

public class HelpCommand implements Command {

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		String helpMsg = "**__General Commands:__**\n";
		for (Command comm : CommandHandler.commandMap.values())
			helpMsg += comm.getHelp();
		
		helpMsg += "\n\n__**Administrator commands** (you need the Administrator permission in this server to run them):__\n";
		for (Command comm : CommandHandler.adminCommandMap.values())
			helpMsg += comm.getHelp();
		
		if (event.getAuthor().equals(Main.superadmin)) {
			helpMsg += "\n\n__**Superadmin commands** (only the big boss "
					+ Main.superadmin.getName() + "#" + Main.superadmin.getDiscriminator() + " can run them :smirk:):__\n";
			for (Command comm : CommandHandler.superadminCommandMap.values())
				helpMsg += comm.getHelp();
		}
		
		AppTools.sendMessage(event.getChannel(), helpMsg);
	}

	@Override
	public String getHelp() {
		return "`g!help` - Displays this message\n";
	}

}
