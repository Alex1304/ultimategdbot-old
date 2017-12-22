package ultimategdbot.commands.impl;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.app.Main;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.CoreCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.gdevents.users.UserModdedGDEvent;
import ultimategdbot.gdevents.users.UserUnmoddedGDEvent;
import ultimategdbot.net.database.dao.impl.DAOFactory;
import ultimategdbot.net.database.dao.impl.GDModlistDAO;
import ultimategdbot.net.geometrydash.GDModeratorFinder;
import ultimategdbot.net.geometrydash.GDRole;
import ultimategdbot.net.geometrydash.GDUser;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.BotRoles;
import ultimategdbot.util.ProgressMessage;

/**
 * This command will permit granted users to update the Geometry Dash
 * moderator database. It scans through a specified range of accountIDs
 * and checks for each of them whether the GD Moderator badge is present.
 * It uses multiple threads to gain time and performance.
 * If it's the case, then the user will show up in the result message and
 * will be added to the database if not already. Same for the opposite : 
 * if a user is present in the mod database but hasn't the mod badge in-game,
 * he will be removed from the database. In both cases, a user mod/unmod GD event
 * will be dispatched for each change made on the database.
 * 
 * @author Alex1304
 *
 */
public class UpdateModListCommand extends CoreCommand {
	
	private static final int DEFAULT_THREAD_COUNT = 100;
	private static final int DEFAULT_STEP = 1000;
	private static volatile long processedUsers = 0;
	private static long totalUsers;
	private static int step;
	private ProgressMessage resultsPreviewMessage;
	private static ProgressMessage commandProgressMessage;
	private final Set<GDUser> RESULT = new HashSet<>();

	public UpdateModListCommand(EnumSet<BotRoles> rolesRequired) {
		super("updatemodlist", rolesRequired);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		if (args.size() < 2)
			throw new CommandFailedException(this);
		
		long from, to, nbTasks;
		resultsPreviewMessage = new ProgressMessage(event.getChannel());
		commandProgressMessage = new ProgressMessage(event.getChannel());
		
		try {
			from = Long.parseLong(args.get(0));
			to = Long.parseLong(args.get(1));
			nbTasks = (args.size() >= 3) ? Long.parseLong(args.get(2)) : DEFAULT_THREAD_COUNT;
			step = (args.size() >= 4) ? Integer.parseInt(args.get(3)) : DEFAULT_STEP;
			if (from > to)
				throw new CommandFailedException("Second argument must be a larger "
						+ "numeric value than the first argument.");
		} catch (NumberFormatException e) {
			throw new CommandFailedException(this);
		}
		
		to++; // The last ID will be omitted if we don't put this instruction
 		
		// If the result preview message exceeds 2000 characters, it will
		// be released and a new one will be made without the users contained
		// in this list.
		final List<GDUser> ignoredUsers = new ArrayList<>();
		final Consumer<GDUser> actionOnModFound = user -> {
			RESULT.add(user);
			String message = "";
			for (GDUser u : RESULT)
				if (!ignoredUsers.contains(u))
					message += "> **" + u.getName() + "** (" + u.getAccountID()
						+ ") - *" + u.getRole().toString().substring(0, 3) + "*\n";
			if (message.length() >= 2000) {
				resultsPreviewMessage.release();
				ignoredUsers.addAll(RESULT);
			}
			resultsPreviewMessage.updateProgress(message);
		};
		
		totalUsers = to - from;
		processedUsers = 0;
		
		AppTools.sendMessage(event.getChannel(), "Moderator list updating process started...\n"
				+ nbTasks + " threads have been created.");
		
		RESULT.clear();
		long startTime = System.currentTimeMillis();
		List<GDModeratorFinder> threads = new ArrayList<>();
		
		for (long i = 0 ; i < nbTasks ; i++) {
			GDModeratorFinder process = new GDModeratorFinder((i * (to - from)) / nbTasks + from,
					((i + 1) * (to - from)) / nbTasks + from - 1,
					actionOnModFound);
			threads.add(process);
			process.start();
		}
		
		for (Thread process : threads) {
			try {
				process.join();
			} catch (InterruptedException e) {
				for (GDModeratorFinder t : threads)
					t.kill();
				throw new CommandFailedException("Interrupted by user.");
			}
		}
		
		// Database update
		GDModlistDAO gdmldao = DAOFactory.getGDModlistDAO();
		List<GDUser> usersAlreadyInDB = gdmldao.findAll();
		for (GDUser user : usersAlreadyInDB)
			if (!RESULT.contains(user) && user.getRole() == GDRole.USER) {
				gdmldao.delete(user);
				Main.GD_EVENT_DISPATCHER.dispatch(new UserUnmoddedGDEvent(user));
			}
		
		for (GDUser user : RESULT) {
			if (!usersAlreadyInDB.contains(user)) {
				gdmldao.insert(user);
				Main.GD_EVENT_DISPATCHER.dispatch(new UserModdedGDEvent(user));
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
		if (processedUsers % step == 0 || totalUsers == processedUsers)
			commandProgressMessage.updateProgress(progressText);
	}

	@Override
	public String getHelp() {
		return "Iterates through the database of users which AccountID is in the specified range and builds a list "
				+ "of moderators found in Geometry Dash.\n"
				+ "You can also specify the number of threads (sub-tasks) and the progress message step (i.e "
				+ "how often the progress indicator message should update) to optimize execution time."
				+ ":warning: Warning: If you specify a high range to scan, this command may take a huge amount "
				+ "of time to process. You or a moderator can interrupt it at anytime using the command `"
				+ Main.CMD_PREFIX +"kill`.";
	}

	@Override
	public String[] getSyntax() {
		String[] syn = { "<from_accountID> <to_accountID> [<thread_count>] [<progress_message_step>]" };
		return syn;
	}

	@Override
	public String[] getExamples() {
		String[] ex = { "100 500000", "5000 20000 50 500" };
		return ex;
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
