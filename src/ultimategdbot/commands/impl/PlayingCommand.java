package ultimategdbot.commands.impl;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.RequestBuffer;
import ultimategdbot.app.Main;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.CoreCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.BotRoles;

/**
 * Command that allows the user to edit the Playing status of the bot.
 * 
 * @author Alex1304
 *
 */
public class PlayingCommand extends CoreCommand {
	
	public PlayingCommand(EnumSet<BotRoles> rolesRequired) {
		super("playing", rolesRequired);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		if (args.isEmpty())
			throw new CommandFailedException(this);
		final String text = AppTools.concatCommandArgs(args);
		final boolean reset = text.trim().equals("--reset");
		
		RequestBuffer.request(() -> {
			if (reset)
				Main.DISCORD_ENV.getClient().changePlayingText(null);
			else
				Main.DISCORD_ENV.getClient().changePlayingText(text);
		});
		
		if (reset)
			AppTools.sendMessage(event.getChannel(), ":white_check_mark: Playing status has been reset!");
		else
			AppTools.sendMessage(event.getChannel(), ":white_check_mark: Playing status set to: "
					+ "*" + text + "*");
	}

	@Override
	public String getHelp() {
		return "Sets a new \"Playing\" status for the bot. Use `--reset` argument to clear the playing message";
	}

	@Override
	public String[] getSyntax() {
		String[] syn = { "--reset", "<game>" };
		return syn;
	}

	@Override
	public String[] getExamples() {
		String[] ex = { "Geometry Dash", "--reset" };
		return ex;
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
