package eu.europeana.portal2.web.model.seealso;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import eu.europeana.corelib.solr.utils.SolrUtils;

public class SeeAlsoCollectorTest {

	private String[][] testData = new String[][] {
		new String[] {"title", "Das Alphabet in Glozel", "0"},
		new String[] {"what", "still image", "1"},
		new String[] {"DATA_PROVIDER", "Bibliotheque de l'Alliance israelite universelle", "2"},
		new String[] {"PROVIDER", "Judaica Europeana", "3"},
	};

	private SeeAlsoCollector seeAlsoCollector;

	@Before
	public void setup() {
		seeAlsoCollector = new SeeAlsoCollector();
		for (String[] raw : testData) {
			SeeAlsoSuggestion suggestion = new SeeAlsoSuggestion(raw[0], raw[1], Integer.parseInt(raw[2]));
			suggestion.makeEscapedQuery(SolrUtils.escapeQuery(suggestion.getQuery()));
			seeAlsoCollector.add(suggestion);
		}
	}

	/**
	 * Testing getFields() method
	 */
	@Test
	public void testGetFields() {
		assertNotNull(seeAlsoCollector.getFields());
		assertEquals(4, seeAlsoCollector.getFields().size());
		assertTrue(seeAlsoCollector.getFields().contains("title"));
		assertTrue(seeAlsoCollector.getFields().contains("what"));
		assertTrue(seeAlsoCollector.getFields().contains("DATA_PROVIDER"));
		assertTrue(seeAlsoCollector.getFields().contains("PROVIDER"));
		assertFalse(seeAlsoCollector.getFields().contains("fake"));
	}

	/**
	 * Testing the get() method
	 */
	@Test
	public void testGet() {
		List<SeeAlsoSuggestion> suggestions;
		SeeAlsoSuggestion suggestion;

		suggestions = seeAlsoCollector.get("title");
		assertNotNull(suggestions);
		assertEquals(1, suggestions.size());
		suggestion = suggestions.get(0);
		assertEquals(seeAlsoCollector.findById(0), suggestion);
		testFirst(suggestion);

		suggestions = seeAlsoCollector.get("what");
		assertNotNull(suggestions);
		assertEquals(1, suggestions.size());
		suggestion = suggestions.get(0);
		assertEquals(seeAlsoCollector.findById(1), suggestion);
		testSecond(suggestion);

		suggestions = seeAlsoCollector.get("DATA_PROVIDER");
		assertNotNull(suggestions);
		assertEquals(1, suggestions.size());
		suggestion = suggestions.get(0);
		assertEquals(seeAlsoCollector.findById(2), suggestion);
		testThird(suggestion);

		suggestions = seeAlsoCollector.get("PROVIDER");
		assertNotNull(suggestions);
		assertEquals(1, suggestions.size());
		suggestion = suggestions.get(0);
		assertEquals(seeAlsoCollector.findById(3), suggestion);
		testForth(suggestion);

		suggestions = seeAlsoCollector.get("fake");
		assertNull(suggestions);
	}

	/**
	 * Testing the findById() method
	 */
	@Test
	public void testFindById() {
		SeeAlsoSuggestion suggestion;

		suggestion = seeAlsoCollector.findById(0);
		assertNotNull(suggestion);
		testFirst(suggestion);

		suggestion = seeAlsoCollector.findById(1);
		assertNotNull(suggestion);
		testSecond(suggestion);

		suggestion = seeAlsoCollector.findById(2);
		assertNotNull(suggestion);
		testThird(suggestion);

		suggestion = seeAlsoCollector.findById(3);
		assertNotNull(suggestion);
		testForth(suggestion);

		suggestion = seeAlsoCollector.findById(5);
		assertNull(suggestion);

		suggestion = seeAlsoCollector.findById(-1);
		assertNull(suggestion);
	}

	/**
	 * Testing getQueries() method.
	 */
	@Test
	public void testGetQueries() {
		List<String> queries = seeAlsoCollector.getQueries();
		assertNotNull(queries);
		assertEquals(4, queries.size());
		assertEquals("{!id=0}title:\"Das Alphabet in Glozel\"", queries.get(0));
		assertEquals("{!id=1}what:\"still image\"", queries.get(1));
		assertEquals("{!id=2}DATA_PROVIDER:\"Bibliotheque de l'Alliance israelite universelle\"", queries.get(2));
		assertEquals("{!id=3}PROVIDER:\"Judaica Europeana\"", queries.get(3));

		queries = seeAlsoCollector.getQueries(false);
		assertNotNull(queries);
		assertEquals(4, queries.size());
		assertEquals("title:\"Das Alphabet in Glozel\"", queries.get(0));
		assertEquals("what:\"still image\"", queries.get(1));
		assertEquals("DATA_PROVIDER:\"Bibliotheque de l'Alliance israelite universelle\"", queries.get(2));
		assertEquals("PROVIDER:\"Judaica Europeana\"", queries.get(3));
	}

	/**
	 * Testing the first object
	 * @param suggestion
	 */
	private void testFirst(SeeAlsoSuggestion suggestion) {
		assertNotNull(suggestion);
		assertEquals(0, suggestion.getId());
		assertEquals("title", suggestion.getMetaField());
		assertEquals("Das Alphabet in Glozel", suggestion.getLabel());
		assertEquals("Das Alphabet in Glozel", suggestion.getQuery());
		assertEquals("title:\"Das Alphabet in Glozel\"", suggestion.getEscapedQuery());
		assertEquals("{!id=0}title:\"Das Alphabet in Glozel\"", suggestion.getTaggedEscapedQuery());
	}

	/**
	 * Testing the second object
	 * @param suggestion
	 */
	private void testSecond(SeeAlsoSuggestion suggestion) {
		assertNotNull(suggestion);
		assertEquals(1, suggestion.getId());
		assertEquals("what", suggestion.getMetaField());
		assertEquals("still image", suggestion.getLabel());
		assertEquals("still image", suggestion.getQuery());
		assertEquals("what:\"still image\"", suggestion.getEscapedQuery());
		assertEquals("{!id=1}what:\"still image\"", suggestion.getTaggedEscapedQuery());
	}

	/**
	 * Testing the third object
	 * @param suggestion
	 */
	private void testThird(SeeAlsoSuggestion suggestion) {
		assertNotNull(suggestion);
		assertEquals(2, suggestion.getId());
		assertEquals("DATA_PROVIDER", suggestion.getMetaField());
		assertEquals("Bibliotheque de l'Alliance israelite universelle", suggestion.getLabel());
		assertEquals("Bibliotheque de l'Alliance israelite universelle", suggestion.getQuery());
		assertEquals("DATA_PROVIDER:\"Bibliotheque de l'Alliance israelite universelle\"", suggestion.getEscapedQuery());
		assertEquals("{!id=2}DATA_PROVIDER:\"Bibliotheque de l'Alliance israelite universelle\"", suggestion.getTaggedEscapedQuery());
	}

	/**
	 * Testing the forth object
	 * @param suggestion
	 */
	private void testForth(SeeAlsoSuggestion suggestion) {
		assertNotNull(suggestion);
		assertEquals(3, suggestion.getId());
		assertEquals("PROVIDER", suggestion.getMetaField());
		assertEquals("Judaica Europeana", suggestion.getLabel());
		assertEquals("Judaica Europeana", suggestion.getQuery());
		assertEquals("PROVIDER:\"Judaica Europeana\"", suggestion.getEscapedQuery());
		assertEquals("{!id=3}PROVIDER:\"Judaica Europeana\"", suggestion.getTaggedEscapedQuery());
	}

}
