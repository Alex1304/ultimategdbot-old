package ultimategdbot.guildsettings;

import java.util.HashMap;
import java.util.Map;

public abstract class GuildSettingNames {

	private static final Map<String, Class<? extends GuildSetting<?>>> SETTING_NAMES = new HashMap<>();

	static {
		SETTING_NAMES.put(new ChannelAwardedLevelsSetting(null, null).toString(), ChannelAwardedLevelsSetting.class);
		SETTING_NAMES.put(new ChannelGdModeratorsSetting(null, null).toString(), ChannelGdModeratorsSetting.class);
		SETTING_NAMES.put(new ChannelBotAnnouncementsSetting(null, null).toString(), ChannelBotAnnouncementsSetting.class);
		SETTING_NAMES.put(new RoleAwardedLevelsSetting(null, null).toString(), RoleAwardedLevelsSetting.class);
		SETTING_NAMES.put(new RoleGdModeratorsSetting(null, null).toString(), RoleGdModeratorsSetting.class);
		SETTING_NAMES.put(new TagEveryoneOnBotAnnouncementSetting(null, null).toString(), TagEveryoneOnBotAnnouncementSetting.class);
	}
	
	public static Class<? extends GuildSetting<?>> get(String name) {
		return SETTING_NAMES.get(name);
	}
}
