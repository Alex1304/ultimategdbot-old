package ultimategdbot.discordevents;

import java.util.List;
import java.util.Optional;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.impl.events.guild.GuildLeaveEvent;
import ultimategdbot.net.database.dao.impl.DAOFactory;
import ultimategdbot.net.database.dao.impl.GuildSettingsDAO;
import ultimategdbot.net.database.entities.GuildSettings;
import ultimategdbot.util.AppTools;

/**
 * Various Discord events are handled here
 * 
 * @author Alex1304
 *
 */
public class DiscordEvents {
	
	private static List<GuildSettings> allGuildSettings = null;
	
	static {
		new Thread(() -> loadAllGuildSettings()).start();
	}
	
	/**
	 * Loads all guild settings at once to avoid making 1 query for every single server.
	 * The whole bot is supposed to stop working if this method fails.
	 */
	private static void loadAllGuildSettings() {
		if (allGuildSettings != null)
			return;
		
		try {
			allGuildSettings = DAOFactory.getGuildSettingsDAO().findAll();
			if (allGuildSettings.isEmpty())
				throw new Exception();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(4);
		}
	}

	/**
	 * When the bot joins a new server, it inserts a new entry of guild settings
	 * in database if it doesn't exist already. If the server is new, it will also
	 * send a welcome message in the bot announcements default channel
	 * 
	 * @param event - contains information about the guild joined
	 */
	@EventSubscriber
	public void onGuildCreated(GuildCreateEvent event) {
		while (allGuildSettings == null) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}

		Optional<GuildSettings> optGS = allGuildSettings.stream()
				.filter(gs -> gs != null && gs.getGuild() != null && event.getGuild().getLongID() == gs.getGuild().getLongID()).findAny();

		if (!optGS.isPresent()) {
			GuildSettings gs = new GuildSettings(event.getGuild());
			DAOFactory.getGuildSettingsDAO().insert(gs); // Database insertion of the guild
			allGuildSettings.add(gs);
			String joinMsg = "New guild joined : " + event.getGuild().getName()
					+ " (" + event.getGuild().getLongID() + ")";
			AppTools.sendDebugPMToSuperadmin(":white_check_mark: " + joinMsg);
			System.out.println(joinMsg);
		}

		System.out.println("Receiving guild: " + event.getGuild().getName() + " (" + event.getGuild().getLongID() + ")");
	}
	
	/**
	 * When the bot leaves a server, the associated guild settings entry is deleted
	 * from database.
	 * 
	 * @param event - contains info about the guild left
	 */
	@EventSubscriber
	public void onGuildLeft(GuildLeaveEvent event) {
		GuildSettingsDAO gsdao = new GuildSettingsDAO();
		GuildSettings gs = gsdao.find(event.getGuild().getLongID());
		if (gs != null)
			gsdao.delete(gs);
		
		String leaveMsg = "Guild left : " + event.getGuild().getName()
				+ " (" + event.getGuild().getLongID() + ")";
		AppTools.sendDebugPMToSuperadmin(":negative_squared_cross_mark: " + leaveMsg);
		System.out.println(leaveMsg);
	}
}
