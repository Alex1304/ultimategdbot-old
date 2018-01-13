package ultimategdbot.commands.impl.subcommands;

import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.SubCommand;
import ultimategdbot.commands.impl.StatGrindEventCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.net.database.dao.impl.DAOFactory;
import ultimategdbot.net.database.entities.JoinSGE;
import ultimategdbot.net.database.entities.StatGrindEvent;
import ultimategdbot.util.AppTools;

/**
 * Sub-command to leave stat grind events
 * 
 * @author Alex1304
 */
public class StatGrindEventLeaveSubCommand extends SubCommand<StatGrindEventCommand> {

	public StatGrindEventLeaveSubCommand(StatGrindEventCommand parentCommand) {
		super(parentCommand);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		StatGrindEvent ongoingEventInGuild = DAOFactory.getStatGrindEventDAO()
				.findByGuild(event.getGuild().getLongID());
		
		if (ongoingEventInGuild == null)
			throw new CommandFailedException("There is no event running in this server.");
		
		JoinSGE userJoined = DAOFactory.getJoinSGEDAO().find(event.getAuthor(), ongoingEventInGuild);
		if (userJoined == null)
			throw new CommandFailedException("You have already left the event currently running in this server "
					+ "or you never joined.");
		
		DAOFactory.getJoinSGEDAO().delete(userJoined);
		
		AppTools.sendMessage(event.getChannel(), ":white_check_mark: You have successfully left the event!");
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
