package ultimategdbot.guildsettings;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains a map that allows to retrieve a setting class by providing its name.
 * @author alexandre
 *
 */
public abstract class GuildSettingNames {

	private static final Map<String, Class<? extends GuildSetting<?>>> SETTING_NAMES = new HashMap<>();

	static {
		SETTING_NAMES.put(new ChannelAwardedLevelsSetting(null, null).toString(), ChannelAwardedLevelsSetting.class);
		SETTING_NAMES.put(new ChannelGdModeratorsSetting(null, null).toString(), ChannelGdModeratorsSetting.class);
		SETTING_NAMES.put(new ChannelBotAnnouncementsSetting(null, null).toString(), ChannelBotAnnouncementsSetting.class);
		SETTING_NAMES.put(new RoleAwardedLevelsSetting(null, null).toString(), RoleAwardedLevelsSetting.class);
		SETTING_NAMES.put(new RoleGdModeratorsSetting(null, null).toString(), RoleGdModeratorsSetting.class);
		SETTING_NAMES.put(new TagEveryoneOnBotAnnouncementSetting(null, null).toString(), TagEveryoneOnBotAnnouncementSetting.class);
		SETTING_NAMES.put(new ChannelTimelyLevelsSetting(null, null).toString(), ChannelTimelyLevelsSetting.class);
		SETTING_NAMES.put(new RoleTimelyLevelsSetting(null, null).toString(), RoleTimelyLevelsSetting.class);
	}
	
	/**
	 * Finds the Class object of the guild setting in the map using the given
	 * name.
	 * 
	 * @param name
	 *            - the name of the setting class to look for
	 * @return the Class object associated with the given name, or null if the
	 *         name doesn't refer to any class.
	 */
	public static Class<? extends GuildSetting<?>> get(String name) {
		return SETTING_NAMES.get(name);
	}
}
