package ultimategdbot.gdevents.notifiers;

import sx.blah.discord.api.internal.json.objects.EmbedObject;
import ultimategdbot.guildsettings.ChannelGdModeratorsSetting;
import ultimategdbot.guildsettings.RoleGdModeratorsSetting;
import ultimategdbot.net.database.dao.impl.DAOFactory;
import ultimategdbot.net.geometrydash.GDUser;
import ultimategdbot.util.AppTools;
import ultimategdbot.util.Emoji;
import ultimategdbot.util.GDUtils;

/**
 * Notifier implementation for GD mod events
 * 
 * @author Alex1304
 *
 */
public class GDModeratorNotifier extends GDEventNotifier {

	private GDUser user;
	private boolean newMod;
	
	/**
	 * @param user - the user modded / unmodded
	 * @param newMod - true if modded, false if unmodded
	 */
	public GDModeratorNotifier(GDUser user, boolean newMod) {
		super(ChannelGdModeratorsSetting.class, RoleGdModeratorsSetting.class,
				DAOFactory.getGuildSettingsDAO().findAllWithChannelGDModeratorsSetup());
		this.user = user;
		this.newMod = newMod;
	}

	@Override
	public String buildMessage() {
		return newMod ? "**" + user.getName() + "** just got Geometry Dash moderator !!!"
				: "**" + user.getName() + "** just lost Geometry Dash moderator...";
	}

	@Override
	public EmbedObject buildEmbed() {
		return newMod ? GDUtils.buildEmbedForGDUser("User modded!", "https://i.imgur.com/zY61GDD.png", user)
				: GDUtils.buildEmbedForGDUser("User unmodded...", "https://i.imgur.com/X53HV7d.png", user);
	}
	
	@Override
	public void onNotifyDone() {
		notifyConcernedUserIfLinked(user,
				newMod ? "Congratulations for getting the status of Geometry Dash moderator!"
						: "Oh snap! You lost your status of Geometry Dash moderator...");


		AppTools.sendDebugPMToSuperadmin(Emoji.SUCCESS + " GD Moderator event dispatched in "
				+ AppTools.formatMillis(System.currentTimeMillis() - startTimestamp)
				+ "\n" + (newMod ? "+" : "-") + " " + Emoji.MODERATOR + " " + user.getName());
	}

}
