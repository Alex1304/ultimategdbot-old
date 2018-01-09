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
import ultimategdbot.net.database.entities.StatGrindEvent;
import ultimategdbot.net.geometrydash.Stat;
import ultimategdbot.util.AppTools;

/**
 * Sub-command for stat grind event which initializes a new event.
 * 
 * @author Alex1304
 *
 */
public class StatGrindEventInitSubCommand extends SubCommand<StatGrindEventCommand> {
	
	private static final long MILLISECONDS_IN_ONE_DAY = 86_400_000;

	public StatGrindEventInitSubCommand(StatGrindEventCommand parentCommand) {
		super(parentCommand);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		Stat stat = null;
		if (args.size() < 2 || (stat = statByName(args.get(0))) == null)
			throw new CommandFailedException(this.getParentCommand());
		
		int duration = -1;
		try {
			duration = Integer.parseInt(args.get(1));
		} catch (NumberFormatException e) {}
		
		if (duration < 1 || duration > 90)
			throw new CommandFailedException("Duration must be between 1 and 90 days.");
		
		long dateStart = System.currentTimeMillis();
		
		StatGrindEvent sge = DAOFactory.getStatGrindEventDAO().findByGuild(event.getGuild().getLongID());
		if (sge != null && sge.getDateEnd() > dateStart)
			throw new CommandFailedException("There is already an event running.");
		
		sge = new StatGrindEvent(0, event.getGuild(), dateStart, dateStart + MILLISECONDS_IN_ONE_DAY * duration, stat);
		DAOFactory.getStatGrindEventDAO().insert(sge);
		
		AppTools.sendMessage(event.getChannel(), ":white_check_mark: Event successfully initialized! It will begin "
					+ "tomorrow and will end in " + duration + " days! Users can already join the event by using `"
					+ Main.CMD_PREFIX + getParentCommand().getName() + " join`!");
	}
	
	private static Stat statByName(String name) {
		try {
			return Stat.valueOf(name.toUpperCase());
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
	
	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
