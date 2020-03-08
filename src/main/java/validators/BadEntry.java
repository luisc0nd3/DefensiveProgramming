package validators;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

public class BadEntry {
	private final static Logger LOGGER = Logger.getLogger(BadEntry.class.getName());

	public static void main(String[] args) {

		String input = "{\r\n" + 
				"  \"data\": {\r\n" + 
				"    \"customer_uuid\": \"ba2bed16-4088-40c1-9730-7824175051a4\",\r\n" + 
				"    \"general\": {\r\n" + 
				"      \"name\": \"luis arturo\",\r\n" + 
				"      \"lname\": \"conde\",\r\n" + 
				"      \"slname\": \"hernandez\",\r\n" + 
				"      \"bdate\": \"2020-03-08T00:40:32+00:00\"\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"}";

		boolean result = myValidator(input);
		LOGGER.log(Level.INFO, "The input is valid:" + result);
	}

	private static boolean myValidator(String input) {

		// lo primero a validar es que no se un null
		if (input == null) {
			LOGGER.log(Level.SEVERE, "The entry cannot be null.");
			return false;
		}

		// lo segundo a validar es que sea un String
		if (!(input instanceof String)) {
			LOGGER.log(Level.SEVERE, "The entry is not a string.");
			return false;
		}

		// lo tercero a validar es que no sea un string vacio o con espacios en blanco
		if (input.replace(" ", "").isEmpty()) {
			LOGGER.log(Level.SEVERE, "The entry cannot be an empty string.");
			return false;
		}

		// lo cuarto a validar es el formato
		try {
			JSONObject inputJson = new JSONObject(input);
			inputJson = inputJson.getJSONObject("data");

			LOGGER.log(Level.INFO, inputJson.toString());

			if (!inputJson.has("customer_uuid") || !(inputJson.get("customer_uuid") instanceof String)) {
				LOGGER.log(Level.SEVERE, "The customer_uuid property has an incorrect format.");
				return false;
			}

			if (!inputJson.has("general") || !(inputJson.get("general") instanceof JSONObject)) {
				LOGGER.log(Level.SEVERE, "The general property has an incorrect format.");
				return false;
			}

			JSONObject generalJson = inputJson.getJSONObject("general");

			if (!generalJson.has("name") || !(generalJson.get("name") instanceof String)
					|| generalJson.getString("name").replace(" ", "").isEmpty()) {
				LOGGER.log(Level.SEVERE, "The general.name property has an incorrect format.");
				return false;
			}

			if (!generalJson.has("lname") || !(generalJson.get("lname") instanceof String)
					|| generalJson.getString("lname").replace(" ", "").isEmpty()) {
				LOGGER.log(Level.SEVERE, "The general.lname property has an incorrect format.");
				return false;
			}

			if (!generalJson.has("slname") || !(generalJson.get("slname") instanceof String)
					|| generalJson.getString("slname").replace(" ", "").isEmpty()) {
				LOGGER.log(Level.SEVERE, "The general.slname property has an incorrect format.");
				return false;
			}

			if (!generalJson.has("bdate") || !(generalJson.get("bdate") instanceof String
					|| generalJson.getString("bdate").replace(" ", "").isEmpty())) {
				LOGGER.log(Level.SEVERE, "The general.bdate property has an incorrect format.");
				return false;
			}

			if (!utcValidator(generalJson.getString("bdate"))) {
				LOGGER.log(Level.SEVERE, "The general.bdate property has an incorrect format.");
				return false;
			}

		} catch (JSONException e) {
			LOGGER.log(Level.SEVERE, "The entry is not a valid JSON.");
			return false;
		}

		return true;
	}

	private static boolean utcValidator(String bdate) {

		Pattern patternIso8601 = Pattern.compile(
				"^(?:[1-9]\\d{3}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1\\d|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[1-9]\\d(?:0[48]|[2468][048]|[13579][26])|(?:[2468][048]|[13579][26])00)-02-29)T(?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d(?:Z|[+-][01]\\d:[0-5]\\d)$");

		try {
			Matcher matN = patternIso8601.matcher(bdate);

			if (!matN.find()) {
				return false;
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE,
					"Exception within method utcValidator with the following message: " + e.getMessage());
			return false;
		}

		return true;
	}

}