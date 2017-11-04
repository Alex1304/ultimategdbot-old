package ultimategdbot.util;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ultimategdbot.exceptions.InvalidCharacterException;

public abstract class GDMessageBodyEncoder {
	
	private static Map<Character, byte[]> charEncodingMap = initMap();
	
	private static Map<Character, byte[]> initMap() {
		Map<Character, byte[]> map = new HashMap<>();
		
		map.put('A', Base64.getUrlDecoder().decode("cHVzdHA="));
		map.put('B', Base64.getUrlDecoder().decode("c3Zwd3M="));
		map.put('C', Base64.getUrlDecoder().decode("cndxdnI="));
		map.put('D', Base64.getUrlDecoder().decode("dXB2cXU="));
		map.put('E', Base64.getUrlDecoder().decode("dHF3cHQ="));
		map.put('F', Base64.getUrlDecoder().decode("d3J0c3c="));
		map.put('G', Base64.getUrlDecoder().decode("dnN1cnY="));
		map.put('H', Base64.getUrlDecoder().decode("eXx6fXk="));
		map.put('I', Base64.getUrlDecoder().decode("eH17fHg="));
		map.put('J', Base64.getUrlDecoder().decode("e354f3s="));
		map.put('K', Base64.getUrlDecoder().decode("en95fno="));
		map.put('L', Base64.getUrlDecoder().decode("fXh-eX0="));
		map.put('M', Base64.getUrlDecoder().decode("fHl_eHw="));
		map.put('N', Base64.getUrlDecoder().decode("f3p8e38="));
		map.put('O', Base64.getUrlDecoder().decode("fnt9en4="));
		map.put('P', Base64.getUrlDecoder().decode("YWRiZWE="));
		map.put('Q', Base64.getUrlDecoder().decode("YGVjZGA="));
		map.put('R', Base64.getUrlDecoder().decode("Y2ZgZ2M="));
		map.put('S', Base64.getUrlDecoder().decode("YmdhZmI="));
		map.put('T', Base64.getUrlDecoder().decode("ZWBmYWU="));
		map.put('U', Base64.getUrlDecoder().decode("ZGFnYGQ="));
		map.put('V', Base64.getUrlDecoder().decode("Z2JkY2c="));
		map.put('W', Base64.getUrlDecoder().decode("ZmNlYmY="));
		map.put('X', Base64.getUrlDecoder().decode("aWxqbWk="));
		map.put('Y', Base64.getUrlDecoder().decode("aG1rbGg="));
		map.put('Z', Base64.getUrlDecoder().decode("a25ob2s="));
		map.put('0', Base64.getUrlDecoder().decode("AQQCBQE="));
		map.put('1', Base64.getUrlDecoder().decode("AAUDBAA="));
		map.put('2', Base64.getUrlDecoder().decode("AwYABwM="));
		map.put('3', Base64.getUrlDecoder().decode("AgcBBgI="));
		map.put('4', Base64.getUrlDecoder().decode("BQAGAQU="));
		map.put('5', Base64.getUrlDecoder().decode("BAEHAAQ="));
		map.put('6', Base64.getUrlDecoder().decode("BwIEAwc="));
		map.put('7', Base64.getUrlDecoder().decode("BgMFAgY="));
		map.put('8', Base64.getUrlDecoder().decode("CQwKDQk="));
		map.put('9', Base64.getUrlDecoder().decode("CA0LDAg="));
		map.put('.', Base64.getUrlDecoder().decode("HxocGx8="));
		map.put(',', Base64.getUrlDecoder().decode("HRgeGR0="));
		map.put('/', Base64.getUrlDecoder().decode("HhsdGh4="));
		map.put('?', Base64.getUrlDecoder().decode("DgsNCg4="));
		map.put('!', Base64.getUrlDecoder().decode("EBUTFBA="));
		map.put('~', Base64.getUrlDecoder().decode("T0pMS08="));
		map.put('-', Base64.getUrlDecoder().decode("HBkfGBw="));
		map.put('#', Base64.getUrlDecoder().decode("EhcRFhI="));
		map.put('@', Base64.getUrlDecoder().decode("cXRydXE="));
		map.put('(', Base64.getUrlDecoder().decode("GRwaHRk="));
		map.put(')', Base64.getUrlDecoder().decode("GB0bHBg="));
		map.put('^', Base64.getUrlDecoder().decode("b2psa28="));
		map.put('\\', Base64.getUrlDecoder().decode("bWhuaW0="));
		map.put('*', Base64.getUrlDecoder().decode("Gx4YHxs="));
		map.put('%', Base64.getUrlDecoder().decode("FBEXEBQ="));
		map.put(':', Base64.getUrlDecoder().decode("Cw4IDws="));
		map.put('&', Base64.getUrlDecoder().decode("FxIUExc="));
		map.put('$', Base64.getUrlDecoder().decode("FRAWERU="));
		map.put('[', Base64.getUrlDecoder().decode("am9pbmo="));
		map.put(']', Base64.getUrlDecoder().decode("bGlvaGw="));
		map.put('{', Base64.getUrlDecoder().decode("Sk9JTko="));
		map.put('}', Base64.getUrlDecoder().decode("TElPSEw="));
		map.put('<', Base64.getUrlDecoder().decode("DQgOCQ0="));
		map.put('>', Base64.getUrlDecoder().decode("DwoMCw8="));
		map.put(';', Base64.getUrlDecoder().decode("Cg8JDgo="));
		map.put('\'', Base64.getUrlDecoder().decode("FhMVEhY="));
		map.put('|', Base64.getUrlDecoder().decode("TUhOSU0="));
		map.put('_', Base64.getUrlDecoder().decode("bmttam4="));
		map.put('=', Base64.getUrlDecoder().decode("DAkPCAw="));
		map.put('"', Base64.getUrlDecoder().decode("ExYQFxM="));
		map.put('+', Base64.getUrlDecoder().decode("Gh8ZHho="));
		map.put(' ', Base64.getUrlDecoder().decode("ERQSFRE="));
		initLowercaseChars(map);
		
		return map;
	}
	
	private static void initLowercaseChars(Map<Character, byte[]> map) {
		for (char c = 'a' ; c <= 'z' ; c++) {
			byte[] bytes = map.get((char) (c - 0x20)).clone();
			for (int i = 0 ; i < bytes.length ; i++)
				bytes[i] -= 0x20;
			map.put(c, bytes);
		}
	}
	
	public static String encode(String s) throws InvalidCharacterException {
		byte[] resultByte = new byte[s.length()];
		
		for (int i = 0 ; i < s.length() ; i++) {
			char currChar = s.charAt(i);
			if (!charEncodingMap.containsKey(currChar))
				throw new InvalidCharacterException();
			
			resultByte[i] = charEncodingMap.get(currChar)[i%5];
		}
		
		return new String(Base64.getUrlEncoder().encode(resultByte));
	}
	
	public static String decode(String s) {
		String base64decoded = new String(Base64.getUrlDecoder().decode(s));
		
		char[] result = new char[base64decoded.length()];
		
		for (int i = 0 ; i < base64decoded.length() ; i++) {
			char currChar = base64decoded.charAt(i);
			char decodedChar = 0;
			for (Entry<Character, byte[]> e : charEncodingMap.entrySet()) {
				if (e.getValue()[i%5] == currChar) {
					decodedChar = e.getKey();
					break;
				}
			}
			if (decodedChar == 0)
				throw new IllegalArgumentException("Input is not a valid encoded GD message !");
			
			result[i] = decodedChar;
		}
		
		return new String(result);
	}
}
