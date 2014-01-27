package eu.europeana.portal2.web.model.mlt;

import static org.junit.Assert.*;

import org.junit.Test;

import eu.europeana.corelib.solr.utils.SolrUtils;

/**
 * Testing MltSuggestion class
 * @author Peter.Kiraly@europeana.eu
 */
public class MltSuggestionTest {

	/**
	 * Tests basic use case
	 */
	@Test
	public void testBasics() {
		MltSuggestion suggestion = new MltSuggestion("what", "Natural history", 7);
		assertEquals("what", suggestion.getMetaField());
		assertEquals("Natural history", suggestion.getLabel());
		assertEquals("Natural history", suggestion.getQuery());
		assertEquals(7, suggestion.getId());
		assertNull(suggestion.getEscapedQuery());
		assertNull(suggestion.getTaggedEscapedQuery());

		suggestion.makeEscapedQuery(SolrUtils.escapeQuery(suggestion.getQuery()));

		assertNotNull(suggestion.getEscapedQuery());
		assertNotNull(suggestion.getTaggedEscapedQuery());
		assertEquals("what:\"Natural history\"", suggestion.getEscapedQuery());
		assertEquals("{!id=7}what:\"Natural history\"", suggestion.getTaggedEscapedQuery());

	}

	/**
	 * Tests construction with the forth parameter set to false (keeping query as it is)
	 */
	@Test
	public void testClearFalse() {
		MltSuggestion suggestion = new MltSuggestion("DATA_PROVIDER", "University of California Libraries (archive.org)", 9, false);
		assertEquals("DATA_PROVIDER", suggestion.getMetaField());
		assertEquals("University of California Libraries (archive.org)", suggestion.getLabel());
		assertEquals("University of California Libraries (archive.org)", suggestion.getQuery());
		assertEquals(9, suggestion.getId());
		assertNull(suggestion.getEscapedQuery());
		assertNull(suggestion.getTaggedEscapedQuery());

		suggestion.makeEscapedQuery(SolrUtils.escapeQuery(suggestion.getQuery()));

		assertNotNull(suggestion.getEscapedQuery());
		assertNotNull(suggestion.getTaggedEscapedQuery());
		assertEquals("DATA_PROVIDER:\"University of California Libraries \\(archive.org\\)\"", suggestion.getEscapedQuery());
		assertEquals("{!id=9}DATA_PROVIDER:\"University of California Libraries \\(archive.org\\)\"", suggestion.getTaggedEscapedQuery());
	}

	/**
	 * Tests construction with the forth parameter set to true (doing query clearance)
	 */
	@Test
	public void testClearTrue() {
		MltSuggestion suggestion = new MltSuggestion("DATA_PROVIDER", "University of California Libraries (archive.org)", 9, true);
		assertEquals("DATA_PROVIDER", suggestion.getMetaField());
		assertEquals("University of California Libraries (archive.org)", suggestion.getLabel());
		assertEquals("University of California Libraries", suggestion.getQuery());
		assertEquals(9, suggestion.getId());
		assertNull(suggestion.getEscapedQuery());
		assertNull(suggestion.getTaggedEscapedQuery());

		suggestion.makeEscapedQuery(SolrUtils.escapeQuery(suggestion.getQuery()));

		assertNotNull(suggestion.getEscapedQuery());
		assertNotNull(suggestion.getTaggedEscapedQuery());
		assertEquals("DATA_PROVIDER:\"University of California Libraries\"", suggestion.getEscapedQuery());
		assertEquals("{!id=9}DATA_PROVIDER:\"University of California Libraries\"", suggestion.getTaggedEscapedQuery());
	}
}

