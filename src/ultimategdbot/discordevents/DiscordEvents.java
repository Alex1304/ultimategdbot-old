package ultimategdbot.discordevents;

import java.util.List;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.impl.events.guild.GuildLeaveEvent;
import ultimategdbot.net.database.dao.impl.DAOFactory;
import ultimategdbot.net.database.entities.GuildSettings;
import ultimategdbot.net.database.util.SQLQueryExecutor;
import ultimategdbot.util.AppTools;

/**
 * Various Discord events are handled here
 * 
 * @author Alex1304
 *
 */
public class DiscordEvents implements SQLQueryExecutor<Long> {
	
	public List<Long> guildIDs = null;

	/**
	 * When the bot joins a new server, it inserts a new entry of guild settings
	 * in database if it doesn't exist already. If the server is new, it will also
	 * send a welcome message in the bot announcements default channel
	 * 
	 * @param event - contains information about the guild joined
	 */
	@EventSubscriber
	public void onGuildCreated(GuildCreateEvent event) {
		if (guildIDs == null) {
			guildIDs = executeQuery("SELECT guild_id FROM guild_settings", r -> r.getLong(1));
		}
		
		if (!guildIDs.contains(event.getGuild().getLongID())) {
			GuildSettings gs = DAOFactory.getGuildSettingsDAO().find(event.getGuild().getLongID());
			if (gs == null) {
				gs = new GuildSettings(event.getGuild());
				DAOFactory.getGuildSettingsDAO().insert(gs); // Database insertion of the guild
			}
			
			String joinMsg = "New guild joined : " + event.getGuild().getName()
					+ " (" + event.getGuild().getLongID() + ")";
			AppTools.sendDebugPMToSuperadmin(":white_check_mark: " + joinMsg);
			System.out.println(joinMsg);
		}
	}
	
	/**
	 * When the bot leaves a server, the associated guild settings entry is deleted
	 * from database.
	 * 
	 * @param event - contains info about the guild left
	 */
	@EventSubscriber
	public void onGuildLeft(GuildLeaveEvent event) {
		GuildSettings gs = DAOFactory.getGuildSettingsDAO().find(event.getGuild().getLongID());
		if (gs != null)
			DAOFactory.getGuildSettingsDAO().delete(gs);
		
		String leaveMsg = "Guild left : " + event.getGuild().getName()
				+ " (" + event.getGuild().getLongID() + ")";
		AppTools.sendDebugPMToSuperadmin(":negative_squared_cross_mark: " + leaveMsg);
		System.out.println(leaveMsg);
	}
}
