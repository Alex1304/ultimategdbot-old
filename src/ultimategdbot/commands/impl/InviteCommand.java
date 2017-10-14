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

public class InviteCommand extends CoreCommand {

	public InviteCommand() {
		super("invite");
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		BotInviteBuilder bib = new BotInviteBuilder(Main.client);
		bib.withClientID("" + 
				(Main.isTestEnvironment() ? AppParams.CLIENT_TEST_ID : AppParams.CLIENT_ID));
		
		bib.withPermissions(EnumSet.of(
				Permissions.SEND_MESSAGES,
				Permissions.MANAGE_SERVER,
				Permissions.MANAGE_ROLES,
				Permissions.READ_MESSAGES));
		AppTools.sendMessage(event.getChannel(), "Add me to your server using this link (make sure "
				+ "to authorize me all of the listed permissions so I can work properly on your server!):\n"
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
