package eu.europeana.portal2.web.util;

import static org.junit.Assert.*;

import java.text.MessageFormat;
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
		assertEquals("Rembrandt Harmensz. van Rĳn", clearSeeAlso("Rembrandt Harmensz. van Rĳn [picture] (1606-1669)"));
	}

	private String clearSeeAlso(String value) {
		System.out.println(value);
		System.out.println(value.matches("(.*?)\\s*$"));
		String[] patterns = new String[]{"(.*?)\\s*$", "\\[.*?\\])\\s*$"};
		
		for (String pattern : patterns) {
			System.out.println(pattern);
			// while (value.matches(pattern)) {
			value = value.replaceAll(pattern, "");
			value = value.replaceAll("\\s+$", "");
			System.out.println(value);
			// }
		}
		System.out.println(value);
		/*
		Pattern p = Pattern.compile("\\\\\\\\)");
		System.out.println(p.matcher(value).matches());


		System.out.println(value.matches("\\)"));
		System.out.println(value.matches(" \\([^\\(\\)]+\\)\\s*$"));
		*/
		return value;
	}
}
