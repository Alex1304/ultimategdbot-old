package ultimategdbot.util;

import java.util.EnumSet;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.PermissionUtils;
import ultimategdbot.app.AppParams;
import ultimategdbot.app.Main;

public enum BotRoles {
	SUPERADMIN((user, channel) -> user.getLongID() == AppParams.SUPERADMIN_ID),
	MODERATOR((user, channel) -> user.getRolesForGuild(Main.DISCORD_ENV.getOfficialDevGuild())
			.contains(Main.DISCORD_ENV.getModeratorsRole())),
	SERVER_ADMIN((user, channel) -> PermissionUtils.hasPermissions(channel, user, Permissions.ADMINISTRATOR)),
	BETA_TESTER((user, channel) -> user.getRolesForGuild(Main.DISCORD_ENV.getOfficialDevGuild())
			.contains(Main.DISCORD_ENV.getBetaTestersRole())),
	USER((user, channel) -> true);
	
	static {
		SUPERADMIN.setExtendedRoles(EnumSet.of(MODERATOR, SERVER_ADMIN, BETA_TESTER, USER));
		MODERATOR.setExtendedRoles(EnumSet.of(SERVER_ADMIN, BETA_TESTER, USER));
		SERVER_ADMIN.setExtendedRoles(EnumSet.of(USER));
		BETA_TESTER.setExtendedRoles(EnumSet.of(USER));
		USER.setExtendedRoles(EnumSet.noneOf(BotRoles.class));
	}
	
	private PredicateUserChannel conditionForUserToBeGranted;
	
	private BotRoles(PredicateUserChannel conditionForUserToBeGranted) {
		this.conditionForUserToBeGranted = conditionForUserToBeGranted;
	}
	
	private EnumSet<BotRoles> extendedRoles;

	public EnumSet<BotRoles> getExtendedRoles() {
		return extendedRoles;
	}

	public void setExtendedRoles(EnumSet<BotRoles> extendedRoles) {
		this.extendedRoles = extendedRoles;
	}
	
	public static EnumSet<BotRoles> botRolesForUserInChannel(IUser user, IChannel channel) {
		EnumSet<BotRoles> botRoles = EnumSet.noneOf(BotRoles.class);
		
		for (BotRoles role : values())
			if (!botRoles.contains(role) && role.conditionForUserToBeGranted.test(user, channel)) {
				botRoles.add(role);
				botRoles.addAll(role.getExtendedRoles());
			}
		
		return botRoles;
	}
	
	public static boolean isGranted(IUser user, IChannel channel, BotRoles role) {
		return botRolesForUserInChannel(user, channel).contains(role);
	}
	
	public static boolean isGrantedAll(IUser user, IChannel channel, EnumSet<BotRoles> setOfRoles) {
		for (BotRoles role : setOfRoles)
			if (!isGranted(user, channel, role))
				return false;
		return true;
	}
	
	public static BotRoles getHighestBotRoleInSet(EnumSet<BotRoles> set) {
		if (set.isEmpty())
			return null;
		
		int lowestOrdinal = values().length;
		
		for (BotRoles br : set)
			if (br.ordinal() < lowestOrdinal)
				lowestOrdinal = br.ordinal();
		
		return values()[lowestOrdinal];
	}

	public PredicateUserChannel getConditionForUserToBeGranted() {
		return conditionForUserToBeGranted;
	}
	
	interface PredicateUserChannel {
		boolean test(IUser user, IChannel channel);
	}
}
