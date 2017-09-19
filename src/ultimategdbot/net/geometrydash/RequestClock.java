package ultimategdbot.net.geometrydash;

public class RequestClock implements Runnable {

	private static final int REQUEST_COOLDOWN_SECONDS = 10;
	
	@Override
	public void run() {
		while (true) {
			String awardedLevels = GDServer.fetchNewAwardedLevels();
			// TODO
			
			try {
				Thread.sleep(REQUEST_COOLDOWN_SECONDS * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
