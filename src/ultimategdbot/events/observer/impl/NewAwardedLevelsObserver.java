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
			IChannel channelAwardedSub = guild.getChannelByID(gs.getGdeventAwardedSubscriberChannelId());
			IRole roleAwardedSub = guild.getRoleByID(gs.getGdeventAwardedSubscriberRoleId());
			for (Object level : args)
				AppTools.sendMessage(channelAwardedSub, roleAwardedSub.mention() + " NEW RATE!\n```" + level.toString() + "```");
		}
	}
//"NEW RATE!\n```" + level.toString() + "```"
}
