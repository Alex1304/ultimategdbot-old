package ultimategdbot.gdevents.notifiers;

import java.io.IOException;

import sx.blah.discord.api.internal.json.objects.EmbedObject;
import ultimategdbot.exceptions.RawDataMalformedException;
import ultimategdbot.guildsettings.ChannelTimelyLevelsSetting;
import ultimategdbot.guildsettings.RoleTimelyLevelsSetting;
import ultimategdbot.net.database.dao.impl.DAOFactory;
import ultimategdbot.net.geometrydash.GDLevel;
import ultimategdbot.net.geometrydash.GDUserFactory;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.Emoji;
import ultimategdbot.util.GDUtils;

/**
 * Notifier implementation for timely level events
 * 
 * @author Alex1304
 *
 */
public class TimelyLevelNotifier extends GDEventNotifier {
	
	private GDLevel level;
	private boolean daily;

	/**
	 * @param level - the level concerned by the event
	 * @param daily - true if Daily, false if Weekly
	 */
	public TimelyLevelNotifier(GDLevel level, boolean daily) {
		super(ChannelTimelyLevelsSetting.class, RoleTimelyLevelsSetting.class,
				DAOFactory.getGuildSettingsDAO().findAllWithChannelTimelyLevelsSetup());
		this.level = level;
		this.daily = daily;
	}

	@Override
	public String buildMessage() {
		return daily ? "There is a new Daily level on Geometry Dash !!!"
				: "There is a new Weekly demon on Geometry Dash !!!";
	}

	@Override
	public EmbedObject buildEmbed() {
		String id;
		try {
			id = "" + GDUtils.fetchCurrentTimelyID(daily);
		} catch (IOException e) {
			id = "-";
		}
		
		return daily ? GDUtils.buildEmbedForGDLevel("New Daily level! (#" + id + ")", "https://i.imgur.com/enpYuB8.png", level)
				: GDUtils.buildEmbedForGDLevel("New Weekly demon! (#" + id + ")", "https://i.imgur.com/kcsP5SN.png", level);
	}
	
	@Override
	public void onNotifyDone() {
		try {
			notifyConcernedUserIfLinked(GDUserFactory.buildGDUserFromNameOrDiscordTag(level.getCreator()),
					daily ? "Congratulations for getting a Daily Level!"
							: "Congratulations for getting a Weekly Demon!");
		} catch (RawDataMalformedException | IOException e) {
		}

		AppTools.sendDebugPMToSuperadmin(Emoji.SUCCESS + " Timely level event dispatched in "
				+ AppTools.formatMillis(System.currentTimeMillis() - startTimestamp)
				+ "\n" + (daily ? "Daily Level" : "Weekly Demon") + ": " + Emoji.PLAY + " " + level.toString());
	}

}
