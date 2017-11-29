package ultimategdbot.commands.impl;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.BotInviteBuilder;
import ultimategdbot.app.AppParams;
import ultimategdbot.app.Main;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.CoreCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.BotRoles;

/**
 * Shows the bot invite link to the user.
 * 
 * @author Alex1304
 *
 */
public class InviteCommand extends CoreCommand {

	public InviteCommand(EnumSet<BotRoles> rolesRequired) {
		super("invite", rolesRequired);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		BotInviteBuilder bib = new BotInviteBuilder(Main.DISCORD_ENV.getClient());
		bib.withClientID("" + 
				(Main.isTestEnvironment() ? AppParams.CLIENT_TEST_ID : AppParams.CLIENT_ID));
		
		bib.withPermissions(EnumSet.of(
				Permissions.SEND_MESSAGES,
				Permissions.EMBED_LINKS,
				Permissions.READ_MESSAGE_HISTORY,
				Permissions.MANAGE_SERVER,
				Permissions.MENTION_EVERYONE,
				Permissions.MANAGE_ROLES,
				Permissions.READ_MESSAGES));
		AppTools.sendMessage(event.getChannel(), "Add me to your server using this link (make sure "
				+ "to authorize me all of the listed permissions so I can work properly on your server, "
				+ "except 'Manage server' and 'Mention everyone' which are optional):\n"
				+ bib.build());
	}

	@Override
	public String getHelp() {
		return "Gives you the invite link to add me to your server!";
	}

	@Override
	public String[] getSyntax() {
		return null;
	}

	@Override
	public String[] getExamples() {
		return null;
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
