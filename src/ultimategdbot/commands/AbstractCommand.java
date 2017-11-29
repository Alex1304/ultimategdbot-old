package ultimategdbot.commands;

import java.util.List;
import java.util.Map;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import ultimategdbot.exceptions.CommandFailedException;

/**
 * Basic implementation of the Command interface. Simple commands could simply extend this class?
 * 
 * @author Alex1304
 *
 */
public abstract class AbstractCommand implements Command {
	
	/**
	 * Map of sub-commands, associated with their alias
	 */
	private Map<String, Command> subCommandMap;
	
	/**
	 * Initializes the map of sub-commands.
	 */
	public AbstractCommand() {
		this.subCommandMap = initSubCommandMap();
	}
	
	/**
	 * Initializes the private field subCommandMap.
	 * @return the initialized Map that will be stored into the private field.
	 */
	protected abstract Map<String, Command> initSubCommandMap();

	@Override
	public Map<String, Command> getSubCommandMap() {
		return this.subCommandMap;
	}
	
	@Override
	public boolean triggerSubCommand(String cmdName, MessageReceivedEvent event, List<String> args) throws CommandFailedException {
		if (subCommandMap.containsKey(cmdName)) {
			subCommandMap.get(cmdName).runCommand(event, args);
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Setter for subCommandMap
	 * 
	 * @param subCommandMap
	 */
	public void setSubCommandMap(Map<String, Command> subCommandMap) {
		if (subCommandMap == null)
			this.subCommandMap = subCommandMap;
	}

}
