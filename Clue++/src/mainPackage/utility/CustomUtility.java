package mainPackage.utility;

import java.util.Map;

public class CustomUtility {

	public static String revereseMapLookup(String value, Map<String, String> map) {
		for (String key : map.keySet()) {
			if (map.get(key).equals(value)) {
				return key;
			}
		}
		return null;
	}

}
