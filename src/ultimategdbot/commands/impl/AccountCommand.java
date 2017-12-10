package ultimategdbot.commands.impl;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.app.Main;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.CoreCommand;
import ultimategdbot.commands.impl.subcommands.ActivateAccountSubCommand;
import ultimategdbot.commands.impl.subcommands.ConfirmUnlinkAccountSubCommand;
import ultimategdbot.commands.impl.subcommands.LinkAccountSubCommand;
import ultimategdbot.commands.impl.subcommands.UnlinkAccountSubCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.util.BotRoles;

/**
 * Command that allows the user to link / unlink his Geometry Dash account
 * 
 * @author Alex1304
 *
 */
public class AccountCommand extends CoreCommand {

	public AccountCommand(EnumSet<BotRoles> rolesRequired) {
		super("account", rolesRequired);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		if (args.isEmpty() || !triggerSubCommand(args.get(0), event, args.subList(1, args.size())))
			throw new CommandFailedException(this);
	}

	@Override
	public String getHelp() {
		return "Allows you to link your Geometry Dash account with your Discord account.\n"
				+ "First, you have to provide the GD account username you want to link using"
				+ " `" + Main.CMD_PREFIX + name + " link <gd_username>`. Then, "
				+ "you will be invited to send a specific message to the bot account "
				+ "('UltimateGDBot' in-game). "
				+ "After that, you can use the following command to activate the link:"
				+ "`" + Main.CMD_PREFIX + getName() + " activate`, and you're good to go!";
	}

	@Override
	public String[] getSyntax() {
		String[] syn = { "link <gd_username>", "unlink", "activate", "confirmunlink" };
		return syn;
	}

	@Override
	public String[] getExamples() {
		String[] ex = { "link Alex1304", "confirmlink", "unlink" };
		return ex;
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		Map<String, Command> map = new HashMap<>();
		map.put("link", new LinkAccountSubCommand(this));
		map.put("unlink", new UnlinkAccountSubCommand(this));
		map.put("activate", new ActivateAccountSubCommand(this));
		map.put("confirmunlink", new ConfirmUnlinkAccountSubCommand(this));
		return map;
	}

}
