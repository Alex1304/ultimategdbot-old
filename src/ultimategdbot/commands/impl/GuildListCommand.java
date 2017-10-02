package ultimategdbot.commands.impl;

import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.MissingPermissionsException;
import ultimategdbot.app.Main;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.SuperadminCoreCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.util.AppTools;

public class GuildListCommand extends SuperadminCoreCommand {

	@Override
	public void runSuperadminCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		String message = "**UltimateGDBot is member of the following servers:**\n";
		for (IGuild guild : Main.client.getGuilds()) {
			message += "- " + guild.getName();
			try {
				message += " (" + guild.getExtendedInvites().get(0) + ")\n";
			} catch (MissingPermissionsException|IndexOutOfBoundsException e) {
				message += " (no invite found)\n";
			}
		}
		
		AppTools.sendMessage(event.getChannel(), message);
	}

	@Override
	public String getHelp() {
		return "`g!guildlist` - Displays the list of all the guilds received by the bot\n";
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
