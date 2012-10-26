package eu.europeana.portal2.web.presentation;

import java.util.ArrayList;
import java.util.List;

public class ThemeChecker {

	public static final String DEFAULT_THEME = "default";

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

		registeredThemes.add("portal2-html");
		registeredThemes.add("portal2-html-css");
		registeredThemes.add("portal2-html-css-js");
	}

	public static String check(String theme) {
		return check(theme, DEFAULT_THEME);
	}

	public static String check(String theme, String defaultTheme) {
		if (registeredThemes.contains(theme)) {
			return theme;
		}
		
		if (defaultTheme != null) {
			return defaultTheme;
		} else {
			return DEFAULT_THEME;
		}
	}
}
