package ultimategdbot.commands.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.app.Main;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.CoreCommand;
import ultimategdbot.commands.impl.subcommands.ConfirmLinkAccountSubCommand;
import ultimategdbot.commands.impl.subcommands.ConfirmUnlinkAccountSubCommand;
import ultimategdbot.commands.impl.subcommands.LinkAccountSubCommand;
import ultimategdbot.commands.impl.subcommands.UnlinkAccountSubCommand;
import ultimategdbot.exceptions.CommandFailedException;

public class AccountCommand extends CoreCommand {

	public AccountCommand() {
		super("account");
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		if (args.isEmpty() || !triggerSubCommand(args.get(0), event, args.subList(1, args.size())))
			throw new CommandFailedException(this);
	}

	@Override
	public String getHelp() {
		return "Allows you to link your Geometry Dash account with your Discord account.\n"
				+ "First, you have to provide the GD account username you want to link. Then, "
				+ "a private message will be sent to the provided GD account containing a confirmation "
				+ "code. Once you've received this message, go back to Discord and use the command "
				+ "`" + Main.CMD_PREFIX + getName() + " " + getSyntax()[2] + "`";
	}

	@Override
	public String[] getSyntax() {
		String[] syn = { "link <gd_username>", "unlink", "confirmlink|confirmunlink <confirmation_code>" };
		return syn;
	}

	@Override
	public String[] getExamples() {
		String[] ex = { "link Alex1304", "confirmlink 9s7Px8Av", "unlink Alex1304" };
		return ex;
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		Map<String, Command> map = new HashMap<>();
		map.put("link", new LinkAccountSubCommand(this));
		map.put("unlink", new UnlinkAccountSubCommand(this));
		map.put("confirmlink", new ConfirmLinkAccountSubCommand(this));
		map.put("confirmunlink", new ConfirmUnlinkAccountSubCommand(this));
		return map;
	}

}
