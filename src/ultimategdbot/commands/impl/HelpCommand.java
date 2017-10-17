package ultimategdbot.commands.impl;
import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.app.Main;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.CoreCommand;
import ultimategdbot.commands.DiscordCommandHandler;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.util.AppTools;

public class HelpCommand extends CoreCommand {

	public HelpCommand() {
		super("help");
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		if (!args.isEmpty()) {
			String cmdName = AppTools.concatCommandArgs(args);
			Map<String, CoreCommand> cmdMap = null;
			if (DiscordCommandHandler.adminCommandMap.containsKey(cmdName))
				cmdMap = DiscordCommandHandler.adminCommandMap;
			else if (DiscordCommandHandler.betaTestersCommandMap.containsKey(cmdName))
				cmdMap = DiscordCommandHandler.betaTestersCommandMap;
			else if (DiscordCommandHandler.superadminCommandMap.containsKey(cmdName))
				cmdMap = DiscordCommandHandler.superadminCommandMap;
			else if (DiscordCommandHandler.commandMap.containsKey(cmdName))
				cmdMap = DiscordCommandHandler.commandMap;
			
			if (cmdMap == null)
				throw new CommandFailedException("Unable to display help for this command as it does not exist");
			
			CoreCommand cmd = cmdMap.get(AppTools.concatCommandArgs(args));
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
				for (String ex : cmd.getExamples())
					message += "`" + Main.CMD_PREFIX + cmdName + (ex.isEmpty() ? "" : " ") + ex + "`\n";
			} else {
				message += "\nYou don't need to specify any arguments to run this command.";
			}
			
			AppTools.sendMessage(event.getChannel(), message);
			return;
		}
		
		String helpMsg = "__**Public Commands:** (can be used by everyone):__\n";
		for (String comm : DiscordCommandHandler.commandMap.keySet())
			helpMsg += "`" + Main.CMD_PREFIX + comm + "`, ";
		helpMsg = helpMsg.substring(0, helpMsg.length() - 2);
		helpMsg += "\n";
		
		helpMsg += "\n__**Administrator commands** (you need the Administrator permission in this server to run them):__\n";
		for (String comm : DiscordCommandHandler.adminCommandMap.keySet())
			helpMsg += "`" + Main.CMD_PREFIX + comm + "`, ";
		helpMsg = helpMsg.substring(0, helpMsg.length() - 2);
		helpMsg += "\n";
		
		if (event.getAuthor().equals(Main.superadmin) || event.getAuthor().getRolesForGuild(Main.betaTestersGuild).contains(Main.betaTestersRole)) {
			helpMsg += "\n__**Beta-testers commands** (you need the Certified Beta Testers role in the official bot server to run them):__\n";
			for (String comm : DiscordCommandHandler.betaTestersCommandMap.keySet())
				helpMsg += "`" + Main.CMD_PREFIX + comm + "`, ";
			helpMsg = helpMsg.substring(0, helpMsg.length() - 2);
			helpMsg += "\n";
		}
		
		if (event.getAuthor().equals(Main.superadmin)) {
			helpMsg += "\n__**Superadmin commands** (only the bot developer "
					+ Main.superadmin.getName() + "#" + Main.superadmin.getDiscriminator() + " can run them):__\n";
			for (String comm : DiscordCommandHandler.superadminCommandMap.keySet())
				helpMsg += "`" + Main.CMD_PREFIX + comm + "`, ";
			helpMsg = helpMsg.substring(0, helpMsg.length() - 2);
			helpMsg += "\n";
		}
		
		helpMsg += "\nRun `" + Main.CMD_PREFIX + "help <command_name>` to get detailed help on a specific command.";
		
		AppTools.sendMessage(event.getChannel(), helpMsg);
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
