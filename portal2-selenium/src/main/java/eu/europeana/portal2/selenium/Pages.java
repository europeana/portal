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

			//url = prefs.node("server").get("url", "http://preview.europeana.eu/");
			url = prefs.node("server").get("url", "http://test.portal2.eanadev.org");
			appSuffix = prefs.node("server").get("application", "/portal");
		} catch (Exception e) {
			url = "http://test.portal2.eanadev.org";
			//url = "http://preview.europeana.eu";
			appSuffix = "/portal";
		}

	}

	public final static String INDEX  = url + appSuffix;
	public final static String SEARCH = INDEX + "/search.html";
	public final static String OBJECT = INDEX + "/record/09102/_SMS_MM_M777.html";

}
