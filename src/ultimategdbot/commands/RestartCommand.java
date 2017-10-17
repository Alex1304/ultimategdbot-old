package ultimategdbot.commands;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.app.Main;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.util.AppTools;

public class RestartCommand extends BetaTestersCoreCommand {

	public RestartCommand() {
		super("restart");
	}

	@Override
	public void runBetaTestersCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		if (System.getenv().containsKey("RESTART_UNSUPPORTED"))
			throw new CommandFailedException("This command is unsupported due to host restrictions.");
		
		try {
			AppTools.sendMessage(event.getChannel(), "Restarting...");
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
	
			final ProcessBuilder builder = new ProcessBuilder(command);
				builder.start();
			System.exit(0);
		} catch (URISyntaxException | IOException e) {
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
