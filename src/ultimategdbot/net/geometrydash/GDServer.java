package ultimategdbot.net.geometrydash;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GDServer {

	private static final String gdservURLPrefix = "http://www.boomlings.com/database/";

	public GDServer() {

	}

	private String sendRequest(String webpage, String urlParams) {
		try {
			HttpURLConnection con;
			con = (HttpURLConnection) new URL(gdservURLPrefix + webpage).openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);

			// Sending the request to the server
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParams);
			wr.flush();
			wr.close();

			// Fetching response
			String result = "";
			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				result += line + "\n";
			}

			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public String fetchNewAwardedLevels() {
		return sendRequest("getGJLevels21.php",
				"gameVersion=21&binaryVersion=33&gdw=0&type=11&str=&diff=-&len=-&page=0&total=0"
				+ "&uncompleted=0&onlyCompleted=0&featured=0&original=0&twoPlayer=0&coins=0&epic=0"
				+ "&secret=Wmfd2893gb7");
	}
}
