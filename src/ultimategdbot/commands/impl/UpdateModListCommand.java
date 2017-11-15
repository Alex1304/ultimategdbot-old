package ultimategdbot.commands.impl;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.CoreCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.net.database.dao.GDModlistDAO;
import ultimategdbot.net.geometrydash.GDUser;
import ultimategdbot.net.geometrydash.GDUserFinder;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.BotRoles;

public class UpdateModListCommand extends CoreCommand {
	
	private static final long ESTIMATED_NB_ACCOUNTS = 8_000_000L;
	private static final int NB_TASKS = 30;
	private IMessage PROGRESS_MESSAGE = null;
	private final List<GDUser> RESULT = new ArrayList<>();

	public UpdateModListCommand(EnumSet<BotRoles> rolesRequired) {
		super("updatemodlist", rolesRequired);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		final Consumer<GDUser> actionOnModFound = user -> {
			RESULT.add(user);
			String message = "";
			for (GDUser u : RESULT)
				message += "Result found: " + u.getName() + " - " + u.getAccountID()
					+ " - " + u.getRole() + "\n";
			
			if (PROGRESS_MESSAGE == null)
				PROGRESS_MESSAGE = AppTools.sendMessage(event.getChannel(), message);
			else
				PROGRESS_MESSAGE.edit(message);
		};
		
		AppTools.sendMessage(event.getChannel(), "Moderator list updating process started...");
		
		RESULT.clear();
		long startTime = System.currentTimeMillis();
		List<GDUserFinder> processes = new ArrayList<>();
		
		for (long i = 0 ; i < NB_TASKS ; i++) {
			GDUserFinder process = new GDUserFinder(i * (ESTIMATED_NB_ACCOUNTS / NB_TASKS), 
					(i + 1) * (ESTIMATED_NB_ACCOUNTS / NB_TASKS),
					actionOnModFound);
			processes.add(process);
			process.fork();
		}
		
		for (GDUserFinder process : processes) {
			process.join();
		}
		
		GDModlistDAO gdmldao = new GDModlistDAO();
		gdmldao.truncate();
		for (GDUser user : RESULT)
			gdmldao.insert(user);

		AppTools.sendMessage(event.getChannel(), ":white_check_mark: Moderator list successfully updated!\n"
				+ "Execution time: " + ((System.currentTimeMillis() - startTime) / 1000) + " seconds.");
	}

	@Override
	public String getHelp() {
		return "Iterates through the entire database of users and builds a list of all moderators in Geometry Dash.\n"
				+ ":warning: Warning: This command takes a huge amount of time to process, as Geometry Dash counts "
				+ "over 8 million of registered accounts. Please run this command only once from time to time !";
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
