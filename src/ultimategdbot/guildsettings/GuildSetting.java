package ultimategdbot.guildsettings;

import sx.blah.discord.handle.obj.IGuild;
import ultimategdbot.exceptions.InvalidValueException;

/**
 * Represents one setting in the guild settings. It associates a value and is
 * attached to one guild.
 * 
 * @author Alex1304
 *
 * @param <T>
 *            - The type of the value
 */
public abstract class GuildSetting<T> {
	
	protected String info;
	protected IGuild guild;
	protected T value;

	public GuildSetting(IGuild guild, String info, T value) {
		this.info = info;

		if (guild == null)
			return;
		
		this.guild = guild;
		this.value = (value != null) ? value : defaultValue();
	}
	
	/**
	 * Gets the setting info
	 * 
	 * @return the setting info as String
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * Gets the guild where this setting is applying
	 * 
	 * @return a IGuild instance
	 */
	public IGuild getGuild() {
		return guild;
	}

	/**
	 * Gets the value of this setting
	 * 
	 * @return the value of this setting
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Sets a new value to this setting
	 * 
	 * @param value
	 *            - the new value to assign
	 */
	public void setValue(T value) {
		this.value = value;
	}
	
	/**
	 * Sets a new value that has been converted from a String
	 * 
	 * @param valueStr
	 *            - the String representing the new value to assign
	 * @throws InvalidValueException
	 *             if the parameter coln't be parsed into a valid value.
	 */
	public void setParsedValue(String valueStr) throws InvalidValueException {
		setValue(parseValue(valueStr));
	}
	
	/**
	 * Defines the default value that this setting should take if nothing is specified.
	 * 
	 * @return the default value
	 */
	public T defaultValue() {
		return null;
	}
	
	/**
	 * Replaces the current value of this setting by the default value.
	 */
	public void resetValue() {
		setValue(defaultValue());
	}
	
	/**
	 * This method will construct the value from the given String, and the
	 * result will be returned
	 * 
	 * @param valueStr
	 *            - the String to parse as value
	 * @throws InvalidValueException
	 *             if the method failed to parse the value as a valid one
	 * @return the parsed value
	 */
	public abstract T parseValue(String valueStr) throws InvalidValueException;

	/**
	 * the toString() of this class is actually the name of this setting field.
	 * It will be shown to users when viewing the guild settings, so it's important
	 * to set a clear and short name. By default, the name of the setting will
	 * correspond to the class name but in lowercase and with underscores between
	 * each word. Also, the "Setting" word is removed.
	 */
	@Override
	public String toString() {
		String s = this.getClass().getSimpleName().replaceAll("Setting", "");
		String res = "";
		int i = 0;
		
		for (char c : s.toCharArray()) {
			if (i > 0 && (c + "").matches("[A-Z]"))
				res += "_";
			res += c;
			i++;
		}
		
		return res.toLowerCase();
	}
	
	/**
	 * Returns the String representation of the value. If the value is null,
	 * this method will return the String "N/A".
	 * 
	 * @return the toString() of the value, or "N/A" if the value is null.
	 */
	public String valueToString() {
		return value != null ? value.toString() : "N/A";
	}
}
