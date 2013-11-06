package eu.europeana.portal2.selenium;

import java.util.prefs.Preferences;

import org.ini4j.Ini;
import org.ini4j.IniPreferences;

public class Pages {

	static String url;
	static String appSuffix;
	static {
		try {
			Ini ini = new Ini(Pages.class.getClassLoader().getResourceAsStream("conf.ini"));
			Preferences prefs = new IniPreferences(ini);

			url = prefs.node("server").get("url", "http://www.europeana.eu/");
			appSuffix = prefs.node("server").get("application", "/portal");
		} catch (Exception e) {
			url = "http://www.europeana.eu";
			appSuffix = "/portal";
		}

	}

	public final static String INDEX = url + appSuffix;
	public final static String SEARCH = INDEX + "/search.html";

}
