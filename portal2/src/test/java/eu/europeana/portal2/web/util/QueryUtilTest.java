package eu.europeana.portal2.web.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

/**
 * Testing QueryUtil class
 * 
 * @author peter.kiraly@kb.nl
 */
public class QueryUtilTest {

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

	/**
	 * Testing QueryUtil.removeTruncation
	 */
	@Test
	public void testRemoveTruncation() {
		String input = "http://creativecommons.org/publicdomain/mark/1.0/*";
		String output = QueryUtil.removeTruncation(input);
		assertNotSame(output, input);
		assertEquals("http://creativecommons.org/publicdomain/mark/1.0/", output);
	}

	/**
	 * Testing QueryUtil.createPhraseValue
	 */
	@Test
	public void testCreatePhraseValue() {
		String input = "Bulgariana/Ontotext";
		String output = QueryUtil.createPhraseValue("PROVIDER", input);
		assertNotSame(output, input);
		assertEquals("%22Bulgariana%2FOntotext%22", output);

		input = "Bulgariana / Ontotext";
		output = QueryUtil.createPhraseValue("PROVIDER", input);
		assertNotSame(output, input);
		assertEquals("%22Bulgariana+%2F+Ontotext%22", output);
	}
}
