package ultimategdbot.discordevents;

import static ultimategdbot.app.Main.CMD_PREFIX;
import static ultimategdbot.app.Main.DISCORD_ENV;
import static ultimategdbot.app.Main.GD_EVENT_DISPATCHER;
import static ultimategdbot.app.Main.registerThreads;

import java.util.List;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.impl.events.guild.GuildLeaveEvent;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.StatusType;
import sx.blah.discord.util.RequestBuffer;
import ultimategdbot.commands.DiscordCommandHandler;
import ultimategdbot.gdevents.listeners.GDEventListeners;
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
public class DiscordEvents implements SQLQueryExecutor<Long>{

	public List<Long> guildIDs;
	
	public DiscordEvents() {
		this.guildIDs = executeQuery("SELECT guild_id FROM guild_settings", r -> r.getLong(1));
	}
	
	/**
	 * Executes when the client is ready
	 * 
	 * @param event
	 */
	@EventSubscriber
	public void onClientReady(ReadyEvent event) {
		System.out.println("All guilds are loaded, we can now fetch hierarchy info (dev server, mod/beta-testers role, etc)...");

		if (!DISCORD_ENV.fetchSuperadmin()) {
			System.err.println("Unable to fetch Superadmin with given ID");
			System.exit(1);
		}
		
		if (!DISCORD_ENV.init()) {
			System.err.println("Unable to load roles necessary for the bot to work. "
					+ "Please make sure you have provided the correct hierarchy info through application args");
			System.exit(1);
		}
		
		System.out.println("Hierarchy info successfully fetched!");
		RequestBuffer.request(() ->
			DISCORD_ENV.getClient().changePresence(StatusType.ONLINE, ActivityType.PLAYING, "Geometry Dash | " + CMD_PREFIX + "help")
		);
		
		// Adding the command handler is the last thing to do, for performance reasons.
		DISCORD_ENV.getClient().getDispatcher().registerListener(new DiscordCommandHandler());
		
		// Registering Geometry Dash events
		GD_EVENT_DISPATCHER.addAllListeners(GDEventListeners.getListeners());
		registerThreads();
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
		if (DISCORD_ENV.getSuperadmin() != null && !guildIDs.contains(event.getGuild().getLongID())) {
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
