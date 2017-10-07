package ultimategdbot.events.observer.impl;

import java.util.List;

import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.util.RequestBuffer;
import ultimategdbot.app.Main;
import ultimategdbot.events.GDEvent;
import ultimategdbot.events.LastAwardedStateChangedGDEvent;
import ultimategdbot.events.NewAwardedGDEvent;
import ultimategdbot.events.observable.impl.LoopRequestNewAwardedLevels;
import ultimategdbot.events.observer.Observer;
import ultimategdbot.net.database.dao.GuildSettingsDAO;
import ultimategdbot.net.database.entities.GuildSettings;
import ultimategdbot.net.geometrydash.GDLevel;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.GDUtils;

public class NewAwardedLevelsObserver implements Observer<LoopRequestNewAwardedLevels> {

	private IMessage messageOfLastRecordedLevel;

	@Override
	public void update(GDEvent event) {
		if (event instanceof NewAwardedGDEvent)
			notifyNewRatesToAllSubscribers((NewAwardedGDEvent) event);
		if (event instanceof LastAwardedStateChangedGDEvent)
			updateEmbedForLastRecordedLevel((LastAwardedStateChangedGDEvent) event);

	}
	
	public void updateEmbedForLastRecordedLevel(LastAwardedStateChangedGDEvent event) {
		if (messageOfLastRecordedLevel == null)
			return;
		
		GDLevel level = event.getLevel();
		RequestBuffer.request(() -> {
			messageOfLastRecordedLevel.edit(messageOfLastRecordedLevel.getContent(), newAwardedLevelEmbed(level));
		});
	}

	public void notifyNewRatesToAllSubscribers(NewAwardedGDEvent event) {
		List<GuildSettings> gsList = new GuildSettingsDAO().findAll();

		for (GuildSettings gs : gsList) {
			IGuild guild = Main.client.getGuildByID(gs.getGuildId());

			if (guild != null) {
				IChannel channelGDEventSub = guild.getChannelByID(gs.getGdeventSubscriberChannelId());
				IRole roleGDEventSub = guild.getRoleByID(gs.getGdeventSubscriberRoleId());
				for (GDLevel level : event.getNewAwardedLevels()) {
					if (channelGDEventSub != null)
						messageOfLastRecordedLevel = AppTools.sendMessage(channelGDEventSub,
								(roleGDEventSub != null ? roleGDEventSub.mention() + " " : "")
										+ "A new level has just been rated on Geometry Dash!!!",
								newAwardedLevelEmbed(level));
				}
			} else {
				System.err.println("[INFO] Guild deleted");
				new GuildSettingsDAO().delete(gs);
			}
		}
	}
	
	public EmbedObject newAwardedLevelEmbed(GDLevel level) {
		return GDUtils.buildEmbedForGDLevel("New rated level!", "https://i.imgur.com/asoMj1W.png", level);
	}
}
