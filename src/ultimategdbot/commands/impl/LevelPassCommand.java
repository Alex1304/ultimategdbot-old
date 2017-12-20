package ultimategdbot.commands.impl;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.CoreCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.exceptions.RawDataMalformedException;
import ultimategdbot.net.geometrydash.GDLevel;
import ultimategdbot.net.geometrydash.GDLevelFactory;
import ultimategdbot.net.geometrydash.GDServer;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.BotRoles;
import ultimategdbot.util.GDLevelPassCipher;

public class LevelPassCommand extends CoreCommand {

	public LevelPassCommand(EnumSet<BotRoles> rolesRequired) {
		super("levelpass", rolesRequired);
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		try {
			String searchRawData = GDServer.fetchLevelByNameOrID(AppTools.concatCommandArgs(args));
			GDLevel level = GDLevelFactory.buildGDLevelFirstSearchResult(searchRawData);
			
			String downloadRawData = GDServer.downloadLevel(level.getId());
			String creator = level.getCreator();
			level = GDLevelFactory.buildDownloadedGDLevel(downloadRawData);
			level.setCreator(creator);
			
			if (level.isCopyable()) {
				if (level.requiresPasscode())
					AppTools.sendMessage(event.getChannel(), "Copy passcode for level " + level + ":\n**"
							+ GDLevelPassCipher.format(level.getPass()) + "**");
				else
					throw new CommandFailedException("This level doesn't require a password to be"
							+ " copied!");
			} else
				throw new CommandFailedException("This level is not copyable.");
			
		} catch (RawDataMalformedException e) {
			e.printStackTrace();
			throw new CommandFailedException("Level not found.");
		} catch (IOException e) {
			throw new CommandFailedException("Unable to communicate with Geometry Dash servers at the moment."
					+ " Please try again later.");
		}
	}

	@Override
	public String getHelp() {
		return "Displays the passcode to copy a specific level in Geometry Dash. An error message will"
				+ " be displayed if the level doesn't require a passcode or isn't copyable.";
	}

	@Override
	public String[] getSyntax() {
		String[] syn = { "[<level_name_or_ID>]" };
		return syn;
	}

	@Override
	public String[] getExamples() {
		String[] ex = { "The Nightmare", "38693063" };
		return ex;
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
