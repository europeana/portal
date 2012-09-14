package eu.europeana.portal2.web.util;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonFormatter {

	public static String format(String rawJsonString) throws JSONException {
		return new JSONObject(rawJsonString).toString(2).replace(" ", "&nbsp;").replace("\n", "<br/>\n");
	}
}
