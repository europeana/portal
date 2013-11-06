package eu.europeana.portal2.selenium.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PatternUtils {

	public static String normaliseWhitespace(String input) {
		return input != null ? input.replaceAll("\\s+", " ").trim() : null;
	}

	public static Pattern createPattern(String query) {
		String s = "";
		for (char c : query.toCharArray()) {
			if (c == '*') {
				s += "\\*";
			} else {
				s += c;
			}
		}
		return Pattern.compile("(&|\\?)" + s + "(&|$)");
	}

	public static List<Pattern> transformPatterns(List<String> queries) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		for (String query : queries) {
			patterns.add(createPattern(query));
		}
		return patterns;
	}

	public static String encodeFix(String text) {
		return text.replace("%3B", ";").replace("%5C", "\\").replace("%2F", "/").replace("%7C", "|")
				.replace("%21", "!");
	}

}
