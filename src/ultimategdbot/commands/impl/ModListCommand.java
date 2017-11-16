package ultimategdbot.commands.impl;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.app.Main;
import ultimategdbot.commands.Command;
import ultimategdbot.commands.CoreCommand;
import ultimategdbot.exceptions.CommandFailedException;
import ultimategdbot.net.database.dao.GDModlistDAO;
import ultimategdbot.net.geometrydash.GDRole;
import ultimategdbot.net.geometrydash.GDUser;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.BotRoles;

public class ModListCommand extends CoreCommand {

	public ModListCommand(EnumSet<BotRoles> rolesRequired) {
		super("modlist", rolesRequired);
		if (rolesRequired.contains(BotRoles.SERVER_ADMIN))
			name = "public" + name;
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		String responseMods = "**__Moderators:__**\n", responseElders = "**__Elder Moderators:__**\n";
		GDModlistDAO gdmldao = new GDModlistDAO();
		List<GDUser> userlist = gdmldao.findAll();
		Collections.sort(userlist, (o1, o2) -> o1.getName().compareTo(o2.getName()));
		
		for (GDUser user : userlist) {
			if (user.getRole() == GDRole.ELDER_MODERATOR)
				responseElders += user.getName() + "\n";
			else
				responseMods += user.getName() + "\n";
		}
		
		if (name.startsWith("public"))
			AppTools.sendMessage(event.getChannel(), responseElders + "\n" + responseMods);
		else
			if (AppTools.sendMessage(Main.DISCORD_ENV.getClient().getOrCreatePMChannel(event.getAuthor()),
					responseElders + "\n" + responseMods) != null)
				if (!event.getChannel().isPrivate())
					AppTools.sendMessage(event.getChannel(), event.getAuthor().mention()
							+ ", check your private messages!");
			else
				AppTools.sendMessage(event.getChannel(), event.getAuthor().mention()
					+ ", I was unable to send the response to your private messages. Make sure you "
					+ "didn't disable them and that you didn't block me. Alternatively, you can ask "
					+ "a server administrator to run `" + Main.CMD_PREFIX + "public" + name + "` "
					+ "to display the list here instead.");
	}

	@Override
	public String getHelp() {
		return "Displays the official moderator list of Geometry Dash. "
				+ (name.startsWith("public") ? "Unlike the regular modlist command, the "
				+ "response will be sent in the current channel." : "The response will be "
				+ "sent to your private messages.");
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
