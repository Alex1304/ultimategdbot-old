package ultimategdbot.commands.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.app.Main;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.CoreCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.exceptions.RawDataMalformedException;
import ultimategdbot.net.database.dao.UserSettingsDAO;
import ultimategdbot.net.database.entities.UserSettings;
import ultimategdbot.net.geometrydash.GDServer;
import ultimategdbot.net.geometrydash.GDUser;
import ultimategdbot.net.geometrydash.GDUserFactory;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.GDUtils;

public class ProfileCommand extends CoreCommand {

	public ProfileCommand() {
		super("profile");
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		try {
			GDUser user;
			if (args.isEmpty()) {
				UserSettings us = new UserSettingsDAO().find(event.getAuthor().getLongID());
				if (us == null || !us.isLinkActivated())
					throw new CommandFailedException("You are not yet linked to any Geometry Dash account.");
				user = GDUserFactory.buildGDUserFromProfileRawData(GDServer.fetchUserProfile(us.getGdUserID()));
			} else
				user = GDUserFactory.buildGDUserFromNameOrDiscordTag(AppTools.concatCommandArgs(args));
			
			if (user == null)
				throw new CommandFailedException("This user isn't linked to any Geometry Dash account.");
			
			AppTools.sendMessage(event.getChannel(), event.getAuthor().mention() + ", here is the profile of "
					+ "user **" + user.getName() + "** :", GDUtils.buildEmbedForGDUser("User profile",
							"https://i.imgur.com/ppg4HqJ.png", user));
			
		} catch (IOException e) {
			throw new CommandFailedException("Unable to communicate with Geometry Dash servers at the moment. "
					+ "Please try again later.");
		} catch (RawDataMalformedException e) {
			throw new CommandFailedException("This user couldn't be found on Geometry Dash servers. "
					+ "If the user you're looking for has a nickname containing spaces, you can replace them with "
					+ "underscores `_`");
		}
	}

	@Override
	public String getHelp() {
		return "Displays the Geometry Dash profile of the specified user. You can also @ mention a Discord user "
				+ "who has linked his GD account to display his stats.\nRunning this command without arguments "
				+ "will display the profile of the GD user you are linked to. An error message will be sent if"
				+ "you are referring to a Discord user who has not linked his GD account.";
	}

	@Override
	public String[] getSyntax() {
		String[] syn = { "", "<gd_username_or_playerID>", "<discord_user_mention>" };
		return syn;
	}

	@Override
	public String[] getExamples() {
		String[] ex = { "", "Alex1304", "16", "@" + Main.superadmin.getName() + "#" + Main.superadmin.getDiscriminator() };
		return ex;
	}

	@Override
	protected Map<String, Command> initSubCommandMap() {
		return null;
	}

}
