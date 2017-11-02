package ultimategdbot.commands;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.app.AppParams;
import ultimategdbot.app.Main;
import ultimategdbot.exceptions.CommandFailedException;

public abstract class BetaTestersCoreCommand extends CoreCommand {

	public BetaTestersCoreCommand(String name) {
		super(name);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		if (event.getAuthor().getLongID() != AppParams.SUPERADMIN_ID
				&& !event.getAuthor().getRolesForGuild(Main.betaTestersGuild).contains(Main.betaTestersRole))
			throw new CommandFailedException("This command can only be used by certified bot beta-testers. Join "
					+ "the official beta testers server at " + Main.BETA_TESTERS_GUILD_INVITE_LINK
					+ " for more details");
		
		runBetaTestersCommand(event, args);
	}
	
	/**
	 * This is called in the regular runCommand() method, so no need to check for superadmin privilege
	 * @param event
	 * @param args
	 */
	public abstract void runBetaTestersCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException;
}
