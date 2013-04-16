package eu.europeana.portal2.web.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;

import org.junit.Test;

/**
 * Testing QueryUtil class
 * 
 * @author peter.kiraly@kb.nl
 */
public class QueryUtilTest {

	/**
	 * Testing whether String.replace() replaces all instances
	 */
	@Test
	public void testReplace() {
		String input = "19\"8\"7";
		String output = input.replace("\"", "\\\"");
		assertEquals("19\\\"8\\\"7", output);
	}
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

	/**
	 * Testing QueryUtil.escapeQuote
	 */
	@Test
	public void testEscapeQuote() {
		String input = "Music Library of Greece \"Lilian Voudouri\" - Friends of Music Society";
		String output = QueryUtil.escapeQuote(input);
		assertNotSame(output, input);
		assertEquals("Music Library of Greece \\\"Lilian Voudouri\\\" - Friends of Music Society", output);
	}
	
	/**
	 * Testing QueryUtil.escapeValue
	 */
	@Test
	public void testValue() {
		String input = "[1987]";
		String output = QueryUtil.escapeValue(input);
		assertNotSame(output, input);
		assertEquals("\\[1987\\]", output);

		input = "Music Library of Greece \"Lilian Voudouri\" - Friends of Music Society";
		output = QueryUtil.escapeValue(input);
		assertNotSame(output, input);
		assertEquals("Music Library of Greece \\\"Lilian Voudouri\\\" - Friends of Music Society", output);
	}
}
