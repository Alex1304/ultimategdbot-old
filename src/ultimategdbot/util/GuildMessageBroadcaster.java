package ultimategdbot.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import ultimategdbot.net.database.entities.GuildSettings;

/**
 * Allows to broadcast a message to a collection of Discord guilds. It can send
 * the same message across all guilds, or send a message customized for each
 * guild. Anyway, this is done with a multi-thread architecture for the best
 * performances.
 * 
 * @author Alex1304
 *
 */
public class GuildMessageBroadcaster {
	
	public static final int DEFAULT_THREAD_COUNT = 8;
	
	private Collection<GuildSettings> guilds;
	private Function<GuildSettings, IChannel> targetChannel;
	private Function<GuildSettings, String> messageToSend;
	private Function<GuildSettings, EmbedObject> embedToSend;
	private List<IMessage> result;

	/**
	 * Creates a new object with a collection of guild settings, and predefined
	 * message and embed. Use this constructor to send the same message
	 * uniformly to all channels.
	 *
	 * @param guilds
	 *            - collection of guilds the message needs to be broadcasted to
	 * @param targetChannel
	 *            - specific channel in the guild the message needs to be broadcasted to
	 * @param message
	 *            - the message content. Cannot be null.
	 * @param embed
	 *            - the message embed. Passing null there just won't broadcast
	 *            an embed.
	 */
	public GuildMessageBroadcaster(Collection<GuildSettings> guilds, 
			Function<GuildSettings, IChannel> targetChannel, String message, EmbedObject embed) {
		this.guilds = guilds;
		this.targetChannel = targetChannel;
		this.messageToSend = gs -> message;
		this.embedToSend = gs -> embed;
	}
	
	/**
	 * Creates a new object with a collection of guild settings, and with a
	 * message and embed customized for each guild.
	 * 
	 * Use this constructor to send a message that can vary in function of the
	 * recipient guild.
	 *
	 * @param guilds
	 *            - collection of guilds the message needs to be broadcasted to
	 * @param targetChannel
	 *            - specific channel in the guild the message needs to be broadcasted to
	 * @param messageToSend
	 *            - the message content in function of the guild.
	 * @param embedToSend
	 *            - the message embed in function of the guild.
	 */
	public GuildMessageBroadcaster(Collection<GuildSettings> guilds, 
			Function<GuildSettings, IChannel> targetChannel, Function<GuildSettings, String> messageToSend,
			Function<GuildSettings, EmbedObject> embedToSend) {
		this.guilds = guilds;
		this.targetChannel = targetChannel;
		this.messageToSend = messageToSend;
		this.embedToSend = embedToSend;
	}

	/**
	 * Starts broadcasting the messages to the guilds specified on object
	 * instanciation.
	 * 
	 * @param threadCount
	 *            - the number of threads to use for this process
	 * 
	 * @throws IllegalArgumentException
	 *             if thread count is zero or negative
	 */
	@SuppressWarnings("unchecked")
	public void broadcast(int threadCount) {
		if (threadCount < 1)
			throw new IllegalArgumentException("Thread count cannot be zero or negative !");
		
		this.result = new ArrayList<>();
		Collection<GuildSettings>[] subCollections = (Collection<GuildSettings>[]) new Collection[threadCount];
		int i = 0;
		
		for (GuildSettings gs : guilds) {
			int subCollectionIndex = i % threadCount;
			
			if (subCollections[subCollectionIndex] == null)
				subCollections[subCollectionIndex] = new ArrayList<>();
			
			subCollections[subCollectionIndex].add(gs);
			i++;
		}
		
		// The use of Math.min() is to avoid any error when guilds.size() < threadCount
		// So if threadCount == 8 and guilds.size() == 3, then only 3 threads would be created.
		Thread[] threads = new Thread[Math.min(guilds.size(), threadCount)];
		
		for (int it = 0 ; it < threads.length ; it++) {
			final int itFinal = it; // Exporting to final so it can be used in lambda expressions
			
			threads[it] = new Thread(() -> {
				for (GuildSettings gs : subCollections[itFinal]) {
					IChannel channel = targetChannel.apply(gs);
					String message = messageToSend.apply(gs);
					
					if (channel != null && message != null)
						result.add(AppTools.sendMessage(channel, message, embedToSend.apply(gs)));
				}
			});
			
			threads[it].start();
		}
		
		for (Thread t : threads)
			try {
				t.join();
			} catch (InterruptedException e) {
			}
	}
	
	/**
	 * Starts broadcasting the messages to the guilds specified on object
	 * instanciation. It will use a default number of threads to perform this
	 * operation, as defined by
	 * {@link GuildMessageBroadcaster#DEFAULT_THREAD_COUNT}
	 */
	public void broadcast() {
		broadcast(DEFAULT_THREAD_COUNT);
	}

	/**
	 * @return the list of messages successfully sent after broadcasting, or
	 *         null if the broadcast() method has never been called
	 */
	public List<IMessage> getResult() {
		return result;
	}

}
