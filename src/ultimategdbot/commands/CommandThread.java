package ultimategdbot.commands;

import static ultimategdbot.commands.DiscordCommandHandler.COMMAND_MAP;

import java.util.List;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.exceptions.ThreadKilledException;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.BotRoles;
import ultimategdbot.util.KillableThread;

/**
 * Every command are launched in a separate thread.
 * 
 * @author Alex1304
 *
 */
public class CommandThread extends KillableThread {
	
	private MessageReceivedEvent event;
	private List<String> args;
	private String cmdName;
	
	public CommandThread(MessageReceivedEvent event, String cmdName, List<String> args) {
		this.event = event;
		this.args = args;
		this.cmdName = cmdName;
	}
	
	@Override
	public void run(KillableThread thisThread) throws ThreadKilledException {

		try {
			if (COMMAND_MAP.containsKey(cmdName)) {
				if (BotRoles.isGrantedAll(event.getAuthor(), event.getChannel(),
						COMMAND_MAP.get(cmdName).getRolesRequired())) {
					RequestBuffer.request(() -> event.getChannel().setTypingStatus(true));
					COMMAND_MAP.get(cmdName).runCommand(event, args);
				}
				else
					throw new CommandFailedException("You don't have permission to use this command");
			}
		} catch (CommandFailedException e) {
			AppTools.sendMessage(event.getChannel(), ":negative_squared_cross_mark: " + e.getFailureReason());
		} catch (DiscordException e) {
			AppTools.sendMessage(event.getChannel(), ":negative_squared_cross_mark: Sorry, an error occured"
					+ " while running the command.\n```\n" + e.getErrorMessage() + "\n```");
			System.err.println(e.getErrorMessage());
		} catch (Exception e) {
			AppTools.sendMessage(event.getChannel(), "An internal error occured while running the command."
					+ " Please try again later.");
			AppTools.sendDebugPMToSuperadmin(
					"An internal error occured in the command handler. See logs for more details\n"
							+ "Context info:\n"
							+ "```\n"
							+ "Guild: " + event.getGuild().getName() + " (" + event.getGuild().getLongID() + ")\n"
							+ "Channel: #" + event.getChannel().getName() + "\n"
							+ "Author: " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator()
									+ "(" + event.getAuthor().getLongID() + ")\n"
							+ "Full message: " + event.getMessage().getContent() + "\n"
							+ "```\n");
			e.printStackTrace();
		} finally {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {}
			RequestBuffer.request(() -> event.getChannel().setTypingStatus(false));
		}
	}

}
