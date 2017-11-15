package ultimategdbot.commands.impl;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
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
	}

	@Override
	public void runCommand(MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		String responseMods = "**__Moderators:__**\n", responseElders = "**__Elder Moderators__**\n";
		GDModlistDAO gdmldao = new GDModlistDAO();
		List<GDUser> userlist = gdmldao.findAll();
		Collections.sort(userlist, (o1, o2) -> o1.getName().compareTo(o2.getName()));
		
		for (GDUser user : userlist) {
			if (user.getRole() == GDRole.ELDER_MODERATOR)
				responseElders += user.getName() + "\n";
			else
				responseMods += user.getName() + "\n";
		}
		
		AppTools.sendMessage(event.getChannel(), responseElders + "\n\n" + responseMods); 
	}

	@Override
	public String getHelp() {
		return "Displays the official moderator list of Geometry Dash";
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
