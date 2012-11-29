package eu.europeana.portal2.web.util;

import static org.junit.Assert.*;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class MixedTest {

	String UNION_FACETS_FORMAT = "'{'!ex={0}'}'{0}";

	@Test
	public void testQueryRegex() {
		String link = "http://test.portal2.eanadev.org/portal/search.html?query=paris&qf=RIGHTS:%22http://creativecommons.org/publicdomain/mark/1.0/%22&rows=24";
		String regex = "\\?" + "query=paris" + "(&|$)";
		// String regex = "query"; // + "(&|$)";
		match("foo", "foofoofoo");
		match("query", link);
		match(regex, link);
		/*
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(link);
		*/
		assertTrue(Pattern.compile(regex).matcher(link).find());
	}

	private void match(String regex, String input) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);

		boolean found = false;
		while (matcher.find()) {
			System.out.println(String.format("I found the text \"%s\" starting at index %d and ending at index %d.%n",
					matcher.group(),
					matcher.start(),
					matcher.end()));
			found = true;
		}
		if(!found){
			System.out.println("No match found.");
		}
	}

	@Test
	public void test() {
		assertEquals("{!ex=COUNTRY}COUNTRY", MessageFormat.format(UNION_FACETS_FORMAT, "COUNTRY"));
	}

	@Test
	public void testClearSeeAlso() {
		assertEquals("Rembrandt Harmenszoon van Rijn", clearSeeAlso("Rembrandt Harmenszoon van Rijn 1606-1669 [picture] (1606-1669)"));
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
