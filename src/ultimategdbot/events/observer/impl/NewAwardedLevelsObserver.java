package ultimategdbot.events.observer.impl;

import java.util.List;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import ultimategdbot.app.AppTools;
import ultimategdbot.app.Main;
import ultimategdbot.events.observable.impl.LoopRequestNewAwardedLevels;
import ultimategdbot.events.observer.Observer;
import ultimategdbot.net.database.dao.GuildSettingsDAO;
import ultimategdbot.net.database.entities.GuildSettings;

public class NewAwardedLevelsObserver implements Observer<LoopRequestNewAwardedLevels> {

	@Override
	public void update(Object... args) {
		List<GuildSettings> gsList = new GuildSettingsDAO().findAll();
		
		for (GuildSettings gs : gsList) {
			IGuild guild = Main.client.getGuildByID(gs.getGuildId());
			IChannel channelGDEventSub = guild.getChannelByID(gs.getGdeventSubscriberChannelId());
			IRole roleGEventSub = guild.getRoleByID(gs.getGdeventSubscriberRoleId());
			for (Object level : args) {
				if (roleGEventSub != null && channelGDEventSub != null)
					AppTools.sendMessage(channelGDEventSub, roleGEventSub.mention() +
							" A new level has just been rated on Geometry Dash!!!\n```" + level.toString() + "```");
				else
					if (guild != null)
						System.err.println("Unable to send notification message upon new GD event in guild " + guild.getName()
							+ ": Channel or/and Role info are not correctly provided");
					else
						new GuildSettingsDAO().delete(gs);
			}
		}
	}
//"NEW RATE!\n```" + level.toString() + "```"
}
