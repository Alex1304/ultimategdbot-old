package ultimategdbot.net.geometrydash;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;

import ultimategdbot.app.AppParams;
import ultimategdbot.app.Main;
import ultimategdbot.exceptions.InvalidCharacterException;
import ultimategdbot.util.GDMessageBodyEncoder;

/**
 * Connection with Geometry Dash official servers is managed here
 * Contains utility levels to interact with the game
 * 
 * @author Alex1304
 *
 */
public abstract class GDServer {

	private static final String GD_SERVER_URL_PREFIX = "http://www.boomlings.com/database/";
	private static final String SECRET = "Wmfd2893gb7";
	
	/**
	 * Submits a POST request to the specified page on the GD server and returns the result as a String
	 * 
	 * @param webpage relative URL to the page
	 * @param urlParams Content of the POST request
	 * @return server response as String
	 */
	public static String sendRequest(String webpage, String urlParams) throws IOException {
			HttpURLConnection con;
			con = (HttpURLConnection) new URL(GD_SERVER_URL_PREFIX + webpage).openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			
			// Sending the request to the server
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParams);
			wr.flush();
			wr.close();
			
			if (Main.isTestEnvironment())
				System.out.println(urlParams);

			// Fetching response
			String result = "";
			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				result += line + "\n";
			}

			return result;
	}
	
	/**
	 * Fetches the latest awarded levels
	 * @return server response as String
	 * @throws IOException 
	 */
	public static String fetchNewAwardedLevels() throws IOException {
		return sendRequest("getGJLevels21.php",
				"gameVersion=21&binaryVersion=33&gdw=0&type=11&str=&diff=-&len=-&page=0&total=0"
				+ "&uncompleted=0&onlyCompleted=0&featured=0&original=0&twoPlayer=0&coins=0&epic=0"
				+ "&secret=" + SECRET);
	}
	
	/**
	 * Fetches the latest uploaded levels
	 * @return server response as String
	 * @throws IOException 
	 */
	public static String fetchMostRecentLevels() throws IOException {
		return sendRequest("getGJLevels21.php",
				"gameVersion=21&binaryVersion=33&gdw=0&type=4&str=&diff=-&len=-&page=0&total=9999"
				+ "&uncompleted=0&onlyCompleted=0&featured=0&original=0&twoPlayer=0&coins=0&epic=0"
				+ "&secret=" + SECRET);
	}
	
	/**
	 * Fetches the first level corresponding to the given name
	 * @return server response as String
	 * @throws IOException 
	 */
	public static String fetchLevelByNameOrID(String levelNameOrID) throws IOException {
		return sendRequest("getGJLevels21.php",
				"gameVersion=21&binaryVersion=33&gdw=0&type=0&str=" + URLEncoder.encode(levelNameOrID, "UTF-8") + "&diff=-&len=-&page=0&total=0"
				+ "&uncompleted=0&onlyCompleted=0&featured=0&original=0&twoPlayer=0&coins=0&epic=0"
				+ "&secret=" + SECRET);
	}
	
	public static String sendMessageFromBotToGDUser(long recipientAccountID, String subject, String body) throws IOException, InvalidCharacterException {
		return sendRequest("uploadGJMessage20.php",
				"gameVersion=21&binaryVersion=33&gdw=0&accountID=" + AppParams.GD_ACCOUNT_ID + "&gjp="
				+ AppParams.GD_ACCOUNT_GJP + "&toAccountID=" + recipientAccountID + "&subject="
				+ new String(Base64.getUrlEncoder().encode(subject.getBytes())) + "&body="
				+ GDMessageBodyEncoder.encode(body) + "&secret=" + SECRET);
	}
	
	public static String fetchUsersByNameOrID(String userNameOrID) throws IOException {
		return sendRequest("getGJUsers20.php",
				"gameVersion=21&binaryVersion=33&gdw=0&type=0&str=" + URLEncoder.encode(userNameOrID, "UTF-8") + "&page=0&total=0"
				+ "&secret=" + SECRET);
	}
	
	public static String fetchUserProfile(long accountID) throws IOException {
		return sendRequest("getGJUserInfo20.php",
				"gameVersion=21&binaryVersion=33&gdw=0&accountID=" + AppParams.GD_ACCOUNT_ID + "&gjp=" + AppParams.GD_ACCOUNT_GJP
				+ "&targetAccountID=" + accountID + "&secret=" + SECRET);
	}
}
