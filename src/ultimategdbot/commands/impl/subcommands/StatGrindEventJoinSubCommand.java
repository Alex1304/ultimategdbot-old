package ultimategdbot.commands.impl.subcommands;

import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.app.Main;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.SubCommand;
import ultimategdbot.commands.impl.StatGrindEventCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.net.database.dao.impl.DAOFactory;
import ultimategdbot.net.database.entities.JoinSGE;
import ultimategdbot.net.database.entities.StatGrindEvent;
import ultimategdbot.net.database.entities.UserSettings;
import ultimategdbot.net.geometrydash.GDUser;
import ultimategdbot.util.AppTools;

/**
 * Sub-command to join stat grind events
 * 
 * @author Alex1304
 *
 */
public class StatGrindEventJoinSubCommand extends SubCommand<StatGrindEventCommand> {

	public StatGrindEventJoinSubCommand(StatGrindEventCommand parentCommand) {
		super(parentCommand);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		StatGrindEvent ongoingEventInGuild = DAOFactory.getStatGrindEventDAO()
				.findByGuild(event.getGuild().getLongID());
		
		if (ongoingEventInGuild == null)
			throw new CommandFailedException("There is no event running in this server.");
		
		UserSettings us = DAOFactory.getUserSettingsDAO().find(event.getAuthor().getLongID());
		GDUser userGDAcc = null;
		if (us != null)
			userGDAcc = us.getGDUserInstance();
		
		if (userGDAcc == null)
			throw new CommandFailedException("You have to link your GD account first. To do so, see "
					+ "`" + Main.CMD_PREFIX + "help account`");
		
		if (DAOFactory.getJoinSGEDAO().find(event.getAuthor(), ongoingEventInGuild) != null)
			throw new CommandFailedException("You have already joined the event currently running in this server.");
		
		
		DAOFactory.getJoinSGEDAO().insert(new JoinSGE(event.getAuthor(), ongoingEventInGuild.getEventID(), 0,
				ongoingEventInGuild.getStatType().forUser(userGDAcc)));
		
		AppTools.sendMessage(event.getChannel(), ":white_check_mark: You are now part of the event!");
	}
	
	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
