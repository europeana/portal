package eu.europeana.portal2.web.util;

import static org.junit.Assert.*;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class MixedTest {

	String UNION_FACETS_FORMAT = "'{'!ex={0}'}'{0}";

	// @Test
	public void test() {
		assertEquals("{!ex=COUNTRY}COUNTRY", MessageFormat.format(UNION_FACETS_FORMAT, "COUNTRY"));
	}

	@Test
	public void testClearSeeAlso() {
		assertEquals("Rembrandt Harmensz. van Rĳn", clearSeeAlso("Rembrandt Harmensz. van Rĳn 1606-1669 [picture] (1606-1669)"));
	}

	private String clearSeeAlso(String value) {
		Pattern[] patterns = new Pattern[]{
			Pattern.compile("\\s*\\(.*?\\)\\s*$"),
			Pattern.compile("\\s*\\[.*?\\]\\s*$"),
			Pattern.compile("\\s*\\d+-\\d+\\s*$")
		};
		String empty = "";

		Matcher m;
		for (Pattern pattern : patterns) {
			m = pattern.matcher(value);
			while (m.find()) {
				value = m.replaceFirst(empty);
				m = pattern.matcher(value);
			}
		}

		return value;
	}
}
