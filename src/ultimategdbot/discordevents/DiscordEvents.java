package ultimategdbot.discordevents;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.impl.events.guild.GuildLeaveEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import ultimategdbot.app.Main;
import ultimategdbot.net.database.dao.GuildSettingsDAO;
import ultimategdbot.net.database.entities.GuildSettings;
import ultimategdbot.util.AppTools;

/**
 * Various Discord events are handled here
 * 
 * @author Alex1304
 *
 */
public class DiscordEvents {

	/**
	 * When the bot joins a new server, it inserts a new entry of guild settings
	 * in database if it doesn't exist already. If the server is new, it will also
	 * send a welcome message in the bot announcements default channel
	 * 
	 * @param event - contains information about the guild joined
	 */
	@EventSubscriber
	public void onGuildCreated(GuildCreateEvent event) {
		GuildSettingsDAO gsdao = new GuildSettingsDAO();
		GuildSettings gs = gsdao.find(event.getGuild().getLongID());

		if (gs == null) {
			gs = new GuildSettings(event.getGuild());
			sendWelcomeMessage(event.getGuild());
			gsdao.insert(gs); // Database insertion of the guild
			AppTools.sendDebugPMToSuperadmin(":white_check_mark: New guild joined : " + event.getGuild().getName()
					+ " (" + event.getGuild().getLongID() + ")");
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
		GuildSettingsDAO gsdao = new GuildSettingsDAO();
		GuildSettings gs = gsdao.find(event.getGuild().getLongID());
		if (gs != null)
			gsdao.delete(gs);

		AppTools.sendDebugPMToSuperadmin(":negative_squared_cross_mark: Guild left : " + event.getGuild().getName()
				+ " (" + event.getGuild().getLongID() + ")");
	}

	/**
	 * When the bot joins a new guild, it will send a "Thanks for the invite"
	 * message which contains instructions for the admins to setup the bot.
     * 
     * If the bot is unable to find a channel it has the perms to write in, the message won't be sent at all.
     * 
     * @param guild - the newly joined guild where it's supposed to send the welcome message.
	 */
	public void sendWelcomeMessage(IGuild guild) {
		IChannel channel = AppTools.findDefaultBotChannelForGuild(guild);

		if (channel != null) {
			AppTools.sendMessage(channel, "Hello! Thanks for inviting me :smile:\n"
							+ "My name is UltimateGDBot and I've been developped by "
							+ AppTools.formatDiscordUsername(Main.DISCORD_ENV.getSuperadmin()) + "!\n"
							+ "I got tons of useful commands for Geometry Dash players, type `"
							+ Main.CMD_PREFIX + "help` to get a list of them.\n"
							+ "If you are an Administrator of this server, run the command `"
							+ Main.CMD_PREFIX + "setup` to make me fully operational on this server!");
		}
	}
}
