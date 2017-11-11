package ultimategdbot.commands.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.app.Main;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.CoreCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.BotRoles;

public class RestartCommand extends CoreCommand {

	public RestartCommand(EnumSet<BotRoles> rolesRequired) {
		super("restart", rolesRequired);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		if (System.getenv().containsKey("RESTART_UNSUPPORTED"))
			throw new CommandFailedException("This command is unsupported due to host restrictions.");
		
		try {
			final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
			File currentJar = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
	
			/* is it a jar file? */
			if(!currentJar.getName().endsWith(".jar"))
				return;
	
			/* Build command: java -jar application.jar */
			final List<String> command = new ArrayList<String>();
			command.add(javaBin);
			command.add("-jar");
			command.add(currentJar.getPath());
			
			AppTools.sendMessage(event.getChannel(), "Restarting...");
	
			final ProcessBuilder builder = new ProcessBuilder(command);
			builder.start();
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommandFailedException("Unable to restart the bot because an internal error occured. Please contact"
					+ " the developer or report that in the Beta Testers server.");
		}
	}

	@Override
	public String getHelp() {
		return "Restarts the bot completely.";
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
