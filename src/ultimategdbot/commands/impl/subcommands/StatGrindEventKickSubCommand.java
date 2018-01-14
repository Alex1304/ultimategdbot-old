package ultimategdbot.commands.impl.subcommands;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.ServerAdminOnlySubCommand;
import ultimategdbot.commands.impl.StatGrindEventCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.net.database.dao.impl.DAOFactory;
import ultimategdbot.net.database.entities.JoinSGE;
import ultimategdbot.net.database.entities.StatGrindEvent;
import ultimategdbot.util.AppTools;

/**
 * Allows server administrators to kick people from stat grind event (i.e force them to leave)
 * 
 * @author Alex1304
 *
 */
public class StatGrindEventKickSubCommand extends ServerAdminOnlySubCommand<StatGrindEventCommand> {

	public StatGrindEventKickSubCommand(StatGrindEventCommand parentCommand) {
		super(parentCommand);
	}

	@Override
	public void runServAdminSubCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		if (args.isEmpty())
			throw new CommandFailedException(this.getParentCommand());
		
		StatGrindEvent ongoingEventInGuild = DAOFactory.getStatGrindEventDAO()
				.findByGuild(event.getGuild().getLongID());
		
		if (ongoingEventInGuild == null)
			throw new CommandFailedException("There is no event running in this server.");
		
		IUser userToKick = null;
		try {
			userToKick = AppTools.parseUser(args.get(0));
		} catch (ParseException e) {
			throw new CommandFailedException("User not found or not valid.");
		}
		
		JoinSGE userJoined = DAOFactory.getJoinSGEDAO().find(userToKick, ongoingEventInGuild);
		if (userJoined == null)
			throw new CommandFailedException("This user is already not part of the event");
		
		DAOFactory.getJoinSGEDAO().delete(userJoined);
		
		AppTools.sendMessage(event.getChannel(), ":white_check_mark: Successfully kicked "
				+ AppTools.formatDiscordUsername(userToKick) + " from the event!");
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
