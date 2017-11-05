package ultimategdbot.events.observer.impl;

import java.util.ArrayList;
import java.util.List;

import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;
import ultimategdbot.app.Main;
import ultimategdbot.events.GDEvent;
import ultimategdbot.events.LastAwardedDeletedGDEvent;
import ultimategdbot.events.LastAwardedStateChangedGDEvent;
import ultimategdbot.events.NewAwardedGDEvent;
import ultimategdbot.events.observable.impl.LoopRequestNewAwardedLevels;
import ultimategdbot.events.observer.Observer;
import ultimategdbot.net.database.dao.GDLevelDAO;
import ultimategdbot.net.database.dao.GuildSettingsDAO;
import ultimategdbot.net.database.entities.GuildSettings;
import ultimategdbot.net.geometrydash.GDLevel;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.GDUtils;

public class AwardedLevelsObserver implements Observer<LoopRequestNewAwardedLevels> {

	private List<IMessage> messageOfLastRecordedLevelForEachGuild = new ArrayList<>();

	@Override
	public void update(GDEvent event) {
		if (event instanceof NewAwardedGDEvent)
			notifyNewRatesToAllSubscribers((NewAwardedGDEvent) event);
		if (event instanceof LastAwardedStateChangedGDEvent)
			updateEmbedForLastRecordedLevel((LastAwardedStateChangedGDEvent) event);
		if (event instanceof LastAwardedDeletedGDEvent)
			notifyDeletedRateToAllSubscribers((LastAwardedDeletedGDEvent) event);

	}
	
	private void notifyDeletedRateToAllSubscribers(LastAwardedDeletedGDEvent event) {
		notifySubscribers("A level just got un-rated from Geometry Dash...", event.getLevel(),
				unratedLevelEmbed(event.getLevel()));
		new GDLevelDAO().delete(event.getLevel());
	}

	public void updateEmbedForLastRecordedLevel(LastAwardedStateChangedGDEvent event) {
		GDLevel level = event.getLevel();
		
		for (IMessage message : messageOfLastRecordedLevelForEachGuild) {
			if (message != null)
				try {
					RequestBuffer.request(() -> message.edit(message.getContent(), newAwardedLevelEmbed(level)));
				} catch (DiscordException e) {
					e.printStackTrace();
				}
		}
	}

	public void notifyNewRatesToAllSubscribers(NewAwardedGDEvent event) {
		event.getLevelList().forEach(level -> {
			notifySubscribers("A new level has just been rated on Geometry Dash!!!", level, newAwardedLevelEmbed(level));
			new GDLevelDAO().insert(level);
		});
	}
	
	private void notifySubscribers(String message, GDLevel level, EmbedObject levelEmbed) {
		List<GuildSettings> gsList = new GuildSettingsDAO().findAll();
		messageOfLastRecordedLevelForEachGuild.clear();

		for (GuildSettings gs : gsList) {
			IGuild guild = Main.client.getGuildByID(gs.getGuildId());

			if (guild != null) {
				IChannel channelGDEventSub = guild.getChannelByID(gs.getGdeventSubscriberChannelId());
				IRole roleGDEventSub = guild.getRoleByID(gs.getGdeventSubscriberRoleId());
				IMessage lastMessage = null;
				if (channelGDEventSub != null) {
					try {
						// This 1 millisecond break will guarantee that messages won't be sent at the
						// same time, which can cause issues in the AppTools#sendMessage() method.
						Thread.sleep(1);
					} catch (InterruptedException e) {}
					lastMessage = AppTools.sendMessage(channelGDEventSub,
							(roleGDEventSub != null ? roleGDEventSub.mention() + " " : "")
									+ message,
							levelEmbed);
				}
				messageOfLastRecordedLevelForEachGuild.add(lastMessage);
			} else {
				System.err.println("[INFO] Guild deleted");
				new GuildSettingsDAO().delete(gs);
			}
		}
	}
	
	public EmbedObject newAwardedLevelEmbed(GDLevel level) {
		return GDUtils.buildEmbedForGDLevel("New rated level!", "https://i.imgur.com/asoMj1W.png", level);
	}
	
	public EmbedObject unratedLevelEmbed(GDLevel level) {
		return GDUtils.buildEmbedForGDLevel("Level unrated...", "https://i.imgur.com/fPECXUz.png", level);
	}
}
