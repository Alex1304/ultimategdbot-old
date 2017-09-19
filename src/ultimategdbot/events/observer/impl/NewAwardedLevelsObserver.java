package ultimategdbot.events.observer.impl;

import java.util.ArrayList;
import java.util.List;

import sx.blah.discord.handle.obj.IPrivateChannel;
import sx.blah.discord.util.RequestBuffer;
import ultimategdbot.app.Main;
import ultimategdbot.events.observable.impl.LoopRequestNewAwardedLevels;
import ultimategdbot.events.observer.Observer;

public class NewAwardedLevelsObserver implements Observer<LoopRequestNewAwardedLevels> {

	@Override
	public void update(Object... args) {
		
		List<Long> subscribers = new ArrayList<>();
		subscribers.add(Main.superadminID);
		subscribers.add(198242185869656064L); // Squall
		
		for (long sub : subscribers) {
			IPrivateChannel pm = Main.client.getOrCreatePMChannel(Main.client.getUserByID(sub));
			for (Object level : args) {
				RequestBuffer.request(() -> {
					pm.sendMessage("NEW RATE!\n```" + level.toString() + "```");
				});
			}
		}
	}

}
