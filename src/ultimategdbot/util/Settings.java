package ultimategdbot.util;

import ultimategdbot.app.Main;

public enum Settings {
	GDEVENTS_ANNOUNCEMENTS_CHANNEL("Specifies in which channel I am supposed to send messages "
			+ "when a new event on GD happens. You can provide the channel either by giving its name, "
			+ "its ID or its tag. Example: `" + Main.CMD_PREFIX + "setup edit gdevents_announcements_channel "
			+ "#bot_announcements`"),
	GDEVENTS_SUBSCRIBER_ROLE("Specifies which role I am supposed to tag when a new event on GD happens. "
			+ "If this setting is not provided, I will send the message but without mentionning any role. "
			+ "You don't need to give any permissions for the chosen role, but it must be mentionnable "
			+ "and I must have the permission to manage this role, or else the `" + Main.CMD_PREFIX + "gdevents` "
			+ "command won't work! You can provide the role either by giving its name, its ID or by mentionning "
			+ "it directly. Example: `" + Main.CMD_PREFIX + "setup edit gdevents_subscriber_role @EventSubscribers`");

	private String info;

	private Settings(String info) {
		this.info = info;
	}

	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}

	public String getInfo() {
		return info;
	}
}
