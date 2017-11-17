package ultimategdbot.util;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

/**
 * Manages a Discord message that is used to show progress of a long process.
 * 
 * @author Alex1304
 *
 */
public class ProgressMessage {
	
	private IMessage progressMessage = null;
	private IChannel workingChannel;
	
	public ProgressMessage(IChannel workingChannel) {
		this.workingChannel = workingChannel;
	}
	
	public IMessage getProgressMessage() {
		return progressMessage;
	}
	
	public IChannel getWorkingChannel() {
		return workingChannel;
	}
	
	 /**
	  * Edits the progress message with the updated content. If the edit fails, then it will send
	  * a new message in the working channel. If it keeps failing at sending messages, false 
	  * will be returned.
	  * 
	  * @param content - updated content of the progress message
	  * @return false if it fails to send the updated message, true otherwise.
	  */
	public boolean updateProgress(String content) {
		if (progressMessage == null || progressMessage.isDeleted()
				|| AppTools.editMessage(progressMessage, content) == null) {
			if (progressMessage != null && !progressMessage.isDeleted())
				progressMessage.delete();
			progressMessage = AppTools.sendMessage(workingChannel, content);
		}
		
		return progressMessage != null;
	}
	
	/**
	 * Upon new update, the progress message will be sent as a new message instead of editing the existing one
	 */
	public void release() {
		progressMessage = null;
	}
}
