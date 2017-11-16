package ultimategdbot.commands.impl;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.CoreCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.net.database.dao.GDModlistDAO;
import ultimategdbot.net.geometrydash.GDModeratorFinder;
import ultimategdbot.net.geometrydash.GDRole;
import ultimategdbot.net.geometrydash.GDUser;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.BotRoles;
import ultimategdbot.util.ProgressMessage;

public class UpdateModListCommand extends CoreCommand {
	
	private static final long ESTIMATED_NB_ACCOUNTS = 8_000_000L;
	private static final int MAX_NB_TASKS = 500;
	private static final double NB_TASKS_FACTOR = 0.2;
	private static volatile long processedUsers = 0;
	private static long totalUsers;
	private ProgressMessage resultsPreviewMessage;
	private static ProgressMessage commandProgressMessage;
	private final Set<GDUser> RESULT = new HashSet<>();

	public UpdateModListCommand(EnumSet<BotRoles> rolesRequired) {
		super("updatemodlist", rolesRequired);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		if (args.size() != 2 && args.size() != 0)
			throw new CommandFailedException(this);
		
		long from, to, nbTasks;
		resultsPreviewMessage = new ProgressMessage(event.getChannel());
		commandProgressMessage = new ProgressMessage(event.getChannel());
		
		if (args.size() == 2) {
			try {
				from = Long.parseLong(args.get(0));
				to = Long.parseLong(args.get(1));
				if (from > to)
					throw new CommandFailedException("Second argument must be a larger "
							+ "numeric value than the first argument.");
			} catch (NumberFormatException e) {
				throw new CommandFailedException(this);
			}	
		} else {
			from = 1;
			to = ESTIMATED_NB_ACCOUNTS;
		}
		
		to++; // The last ID will be omitted if we don't put this instruction
		
		nbTasks = (long) Math.ceil((to - from) * NB_TASKS_FACTOR);
		if (nbTasks > MAX_NB_TASKS)
			nbTasks = MAX_NB_TASKS;
 		
		final Consumer<GDUser> actionOnModFound = user -> {
			RESULT.add(user);
			String message = "";
			for (GDUser u : RESULT)
				message += "> **" + u.getName() + "** (" + u.getAccountID()
					+ ") - *" + u.getRole().toString().substring(0, 3) + "*\n";
			resultsPreviewMessage.updateProgress(message);
		};
		
		totalUsers = to - from;
		processedUsers = 0;
		
		AppTools.sendMessage(event.getChannel(), "Moderator list updating process started...\n"
				+ nbTasks + " sub-processes have been created.");
		
		RESULT.clear();
		long startTime = System.currentTimeMillis();
		List<Thread> processes = new ArrayList<>();
		
		for (long i = 0 ; i < nbTasks ; i++) {
			Thread process = new Thread(new GDModeratorFinder((i * (to - from)) / nbTasks + from,
					((i + 1) * (to - from)) / nbTasks + from - 1,
					actionOnModFound));
			processes.add(process);
			process.start();
		}
		
		for (Thread process : processes) {
			try {
				process.join();
			} catch (InterruptedException e) {
			}
		}
		
		// Database update
		GDModlistDAO gdmldao = new GDModlistDAO();
		List<GDUser> usersAlreadyInDB = gdmldao.findAll();
		for (GDUser user : usersAlreadyInDB)
			if (!RESULT.contains(user) && user.getRole() == GDRole.USER) {
				gdmldao.delete(user);
				// TODO GD event : unmod
			}
		
		for (GDUser user : RESULT) {
			if (!usersAlreadyInDB.contains(user)) {
				gdmldao.insert(user);
				// TODO GD event : MOD?
			}
		}
		
		long execTimeSeconds = ((System.currentTimeMillis() - startTime) / 1000);
		AppTools.sendMessage(event.getChannel(), ":white_check_mark: Moderator list successfully updated!\n"
				+ "Execution time: " + execTimeSeconds / 3600 + " hours " + (execTimeSeconds / 60) % 60 + " minutes "
				+ execTimeSeconds % 60 + " seconds.");
	}
	
	public static void incrementAndShowProcessedUsers() {
		synchronized(UpdateModListCommand.class) {
			processedUsers++;
		}
		
		long rate = Math.round((processedUsers / (double) totalUsers) * 100);
		String progressText = "Processed " + processedUsers + " / " + totalUsers + " users... "
				+ "(" + rate + "%)";
		
		if (processedUsers % 10 == 0 || totalUsers == processedUsers)
			System.out.println(progressText);
		if (processedUsers % 1000 == 0 || totalUsers == processedUsers)
			commandProgressMessage.updateProgress(progressText);
	}

	@Override
	public String getHelp() {
		return "Iterates through the entire database of users and builds a list of all moderators in Geometry Dash.\n"
				+ "You can also specify a range of AccountIDs if you don't need to update the whole list."
				+ ":warning: Warning: This command takes a huge amount of time to process, as Geometry Dash counts "
				+ "over 7 millions of registered accounts. Please run this command only once from time to time !";
	}

	@Override
	public String[] getSyntax() {
		String[] syn = { "", "<from_accountID> <to_accountID>" };
		return syn;
	}

	@Override
	public String[] getExamples() {
		String[] ex = { "", "100 500000" };
		return ex;
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
