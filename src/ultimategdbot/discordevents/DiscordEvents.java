package ultimategdbot.discordevents;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.impl.events.guild.GuildLeaveEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import ultimategdbot.app.AppTools;
import ultimategdbot.app.Main;
import ultimategdbot.net.database.dao.GuildSettingsDAO;
import ultimategdbot.net.database.entities.GuildSettings;

public class DiscordEvents {

	@EventSubscriber
	public void onGuildCreated(GuildCreateEvent event) {
		GuildSettingsDAO gsdao = new GuildSettingsDAO();
		GuildSettings gs = gsdao.find(event.getGuild().getLongID());

		if (gs == null) {
			gs = new GuildSettings(event.getGuild().getLongID(), 0, 0);
			sendWelcomeMessage(event.getGuild());
			gsdao.insert(gs);
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
	 */
	public void sendWelcomeMessage(IGuild guild) {
		// First channel which the bot can send messages in
		IChannel firstChannel = null;
		// Channel named "bot_commands" or "bot-commands"
		IChannel botCommandsChannel = null;
		// Channel named "general"
		IChannel generalChannel = null;
		// The bot's highest role in the guild
		IRole botHighestRole = null;
		try {
			botHighestRole = guild.getRolesForUser(Main.client.getOurUser()).get(0);
		} catch (IndexOutOfBoundsException e) {
			botHighestRole = guild.getEveryoneRole();
		}

		for (IChannel channel : guild.getChannels()) {
			System.out.println(channel.getName());
			if (channel.getModifiedPermissions(botHighestRole).contains(Permissions.SEND_MESSAGES)) {
				if (botCommandsChannel == null && channel.getName().matches("bot[-_]commands"))
					botCommandsChannel = channel;
				else if (generalChannel == null && channel.getName().equals("general"))
					generalChannel = channel;
				else if (firstChannel == null)
					firstChannel = channel;
			}
		}
		IChannel channel = botCommandsChannel != null ? botCommandsChannel
				: generalChannel != null ? generalChannel : firstChannel;

		if (channel != null) {
			IUser superadmin = Main.client.fetchUser(Main.superadminID);
			String SAName = superadmin.getName() + "#" + superadmin.getDiscriminator();
			AppTools.sendMessage(channel,
					"Hello! Thanks for inviting me :smile:\n" + "My name is UltimateGDBot and I've been developped by "
							+ SAName + "!\n"
							+ "I got tons of useful commands for Geometry Dash players, type `g!help` for"
							+ "more info.\n" + "If you are an Administrator of this server, run the command `g!setup`"
							+ " to gain access to the entirety of my features!");
		}
	}
}
