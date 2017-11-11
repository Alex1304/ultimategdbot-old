package ultimategdbot.commands.impl;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.app.Main;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.CoreCommand;
import ultimategdbot.commands.DiscordCommandHandler;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.BotRoles;

public class HelpCommand extends CoreCommand {

	public HelpCommand(EnumSet<BotRoles> rolesRequired) {
		super("help", rolesRequired);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		Map<String, CoreCommand> cmdMap = DiscordCommandHandler.COMMAND_MAP;
		if (!args.isEmpty()) {
			String cmdName = AppTools.concatCommandArgs(args);
			
			if (!DiscordCommandHandler.COMMAND_MAP.containsKey(cmdName))
				throw new CommandFailedException("Unable to display help for this command as it does not exist");
			
			if (!BotRoles.isGrantedAll(event.getAuthor(), event.getChannel(), cmdMap.get(cmdName).getRolesRequired()))
				throw new CommandFailedException("You don't have permission to view help for this command");
			
			CoreCommand cmd = cmdMap.get(cmdName);
			String message = "";
			if (cmd.getSyntax() != null) {
				message += "**__Syntax:__**\n";
				message += "*Note: text between `[]` means **optional** argument(s). "
						+ "Text between `<>` is supposed to be replaced by what the text says. "
						+ "Words separated by `|` means that you may type only one of them.*\n";
				for (String syn : cmd.getSyntax())
					message += "`" + Main.CMD_PREFIX + cmdName + (syn.isEmpty() ? "" : " ") + syn + "`\n";
			}
			
			message += "\n**__Description__**\n";
			message += cmd.getHelp() + "\n";
			
			if (cmd.getExamples() != null) {
				message += "\n**__Usage examples:__**\n";
				message += "```";
				for (String ex : cmd.getExamples())
					message += Main.CMD_PREFIX + cmdName + (ex.isEmpty() ? "" : " ") + ex + "\n";
				message += "```";
			} else {
				message += "\nYou don't need to specify any arguments to run this command.";
			}
			
			AppTools.sendMessage(event.getChannel(), message);
			return;
		}
		
		String helpMsg = "";
		final Map<BotRoles, String> commandListHeadings = new HashMap<>();
		commandListHeadings.put(BotRoles.USER,
				"__**Public Commands:** (can be used by everyone):__\n");
		commandListHeadings.put(BotRoles.BETA_TESTER,
				"__**Beta-Testers Commands:** (can only be used by people with the Beta-Testers role in the dev server):__\n");
		commandListHeadings.put(BotRoles.SERVER_ADMIN,
				"__**Server Admin Commands:** (can only be used by people with Administrator permission in this server):__\n");
		commandListHeadings.put(BotRoles.MODERATOR,
				"__**Bot Moderator Commands:** (can only be used by people with the Bot Moderator role in the dev server):__\n");
		commandListHeadings.put(BotRoles.SUPERADMIN,
				"__**Developer Commands:** (can be used by the bot developer):__\n");
		
		for (BotRoles role : BotRoles.values()) {
			if (BotRoles.isGranted(event.getAuthor(), event.getChannel(), role)) {
				helpMsg += commandListHeadings.get(role);
				helpMsg += commandListFilteredBy(cmdMap,
						cmd -> BotRoles.getHighestBotRoleInSet(cmd.getRolesRequired()).equals(role));
			}
		}
		
		helpMsg += "\nRun `" + Main.CMD_PREFIX + "help <command_name>` to get detailed help on a specific command.";
		
		AppTools.sendMessage(event.getChannel(), helpMsg);
	}
	
	private String commandListFilteredBy(Map<String, CoreCommand> cmdMap, Predicate<CoreCommand> filter) {
		String res = "";
		boolean noCmd = true;
		for (Entry<String, CoreCommand> comm : cmdMap.entrySet())
			if (filter.test(comm.getValue())) {
				res += "`" + Main.CMD_PREFIX + comm.getKey() + "`, ";
				noCmd = false;
			}
		
		if (noCmd)
			res += "*(No commands to display)*";
		else
			res = (res.length() < 2) ? "" : res.substring(0, res.length() - 2);
		
		res += "\n\n";
		
		return res;
	}

	@Override
	public String getHelp() {
		return "Without arguments, displays the list of all available commands. Specifying a command "
				+ "name will display detailed info on the syntax and example usages of the command.";
	}

	@Override
	public String[] getSyntax() {
		String[] res = { "[<command_name_without_prefix>]" };
		return res;
	}

	@Override
	public String[] getExamples() {
		String[] res = { "", "ping" };
		return res;
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
