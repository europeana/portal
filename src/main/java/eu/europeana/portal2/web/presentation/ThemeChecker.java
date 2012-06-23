package eu.europeana.portal2.web.presentation;

import java.util.ArrayList;
import java.util.List;

public class ThemeChecker {

	private static final String DEFAULT = "default";

	private static List<String> registeredThemes = new ArrayList<String>();
	
	/**
	 * The registry of themes. If you want to accept a new theme, please register here.
	 */
	static {
		registeredThemes.add("boilerplate");
		registeredThemes.add("bootstrap");
		registeredThemes.add("default");
		registeredThemes.add("diy");
		registeredThemes.add("foundation");
		registeredThemes.add("portal1");
		registeredThemes.add("vanilla");
		registeredThemes.add("devel");
	}

	public static String check(String theme) {
		if (registeredThemes.contains(theme)) {
			return theme;
		}
		return DEFAULT;
	}
}
