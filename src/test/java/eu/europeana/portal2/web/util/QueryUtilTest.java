package eu.europeana.portal2.web.util;

import static org.junit.Assert.*;

import java.util.regex.Matcher;

import org.junit.Test;

/**
 * Testing QueryUtil class
 * 
 * @author peter.kiraly@kb.nl
 */
public class QueryUtilTest {

	/**
	 * Testing QueryUtil.SQUARE_BRACKET_PATTERN
	 */
	@Test
	public void testPattern() {
		String input = "[1987]";
		Matcher matcher = QueryUtil.SQUARE_BRACKET_PATTERN.matcher(input);
		assertTrue(matcher.find());
		assertEquals("1987", matcher.group(1));
	}

	/**
	 * Testing QueryUtil.escapeSquareBrackets
	 */
	@Test
	public void testEscapeSquareBrackets() {
		String input = "[1987]";
		String output = QueryUtil.escapeSquareBrackets(input);
		assertNotSame(output, input);
		assertEquals("\\[1987\\]", output);
	}
}
