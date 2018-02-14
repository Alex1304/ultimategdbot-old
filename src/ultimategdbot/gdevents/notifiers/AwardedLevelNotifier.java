package ultimategdbot.gdevents.notifiers;

import java.io.IOException;

import sx.blah.discord.api.internal.json.objects.EmbedObject;
import ultimategdbot.exceptions.RawDataMalformedException;
import ultimategdbot.guildsettings.ChannelAwardedLevelsSetting;
import ultimategdbot.guildsettings.RoleAwardedLevelsSetting;
import ultimategdbot.net.database.dao.impl.DAOFactory;
import ultimategdbot.net.geometrydash.GDLevel;
import ultimategdbot.net.geometrydash.GDUserFactory;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.Emoji;
import ultimategdbot.util.GDUtils;

/**
 * Notifier implementation for new awarded level events
 * 
 * @author Alex1304
 *
 */
public class AwardedLevelNotifier extends GDEventNotifier {
	
	private boolean newRate;
	private GDLevel level;
	
	/**
	 * @param level - the level concerned by the event
	 * @param newRate
	 *            - true if it's for a new rated level notification, false if
	 *            it's for an unrated level.
	 */
	public AwardedLevelNotifier(GDLevel level, boolean newRate) {
		super(ChannelAwardedLevelsSetting.class, RoleAwardedLevelsSetting.class,
				DAOFactory.getGuildSettingsDAO().findAllWithChannelAwardedLevelsSetup());
		this.level = level;
		this.newRate = newRate;
	}

	@Override
	public String buildMessage() {
		return newRate ? "A new level has just been rated on Geometry Dash!!!"
				: "A level just got un-rated from Geometry Dash...";
	}

	@Override
	public EmbedObject buildEmbed() {
		return newRate ? GDUtils.buildEmbedForGDLevel("New rated level!", "https://i.imgur.com/asoMj1W.png", level)
				: GDUtils.buildEmbedForGDLevel("Level unrated...", "https://i.imgur.com/fPECXUz.png", level);
	}
	
	@Override
	public void onNotifyDone() {
		try {
			notifyConcernedUserIfLinked(GDUserFactory.buildGDUserFromNameOrDiscordTag(level.getCreator()),
					newRate ? "Congratulations for getting your level rated!"
							: "Oh snap! Your level has just been unrated...");
		} catch (RawDataMalformedException | IOException e) {
		}

		AppTools.sendDebugPMToSuperadmin(Emoji.SUCCESS + " Awarded level event dispatched in "
				+ AppTools.formatMillis(System.currentTimeMillis() - startTimestamp)
				+ "\n" + (newRate ? "+" : "-") + " " + Emoji.PLAY + " " + level.toString());
	}

}
