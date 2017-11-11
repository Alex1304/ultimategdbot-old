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

public class DiscordEvents {

	@EventSubscriber
	public void onGuildCreated(GuildCreateEvent event) {
		GuildSettingsDAO gsdao = new GuildSettingsDAO();
		GuildSettings gs = gsdao.find(event.getGuild().getLongID());

		if (gs == null) {
			gs = new GuildSettings(event.getGuild().getLongID(), 0, 0);
			sendWelcomeMessage(event.getGuild());
			gsdao.insert(gs); // Database insertion of the guild
		}
	}

	@EventSubscriber
	public void onGuildLeft(GuildLeaveEvent event) {
		GuildSettingsDAO gsdao = new GuildSettingsDAO();
		GuildSettings gs = gsdao.find(event.getGuild().getLongID());
		gsdao.delete(gs);
		System.out.println("Guild left");
	}

	/**
	 * When the bot joins a new guild, it will send a "Thanks for the invite"
	 * message which contains instructions for the admins to setup the bot.
     * 
     * 
     * If the bot is unable to find a channel it has the perms to write in, the message won't be sent at all.
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
