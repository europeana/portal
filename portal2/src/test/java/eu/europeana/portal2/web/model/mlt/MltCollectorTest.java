package eu.europeana.portal2.web.model.mlt;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import eu.europeana.corelib.solr.utils.SolrUtils;

public class MltCollectorTest {

	MltCollector collector;

	/**
	 * Setup the collector
	 */
	@Before
	public void setup() {
		collector = new MltCollector();
		MltSuggestion suggestion = new MltSuggestion("what", "Natural history", 0);
		suggestion.makeEscapedQuery(SolrUtils.escapeQuery(suggestion.getQuery()));
		collector.add(suggestion);
		suggestion = new MltSuggestion("DATA_PROVIDER", "University of California Libraries (archive.org)", 1, false);
		suggestion.makeEscapedQuery(SolrUtils.escapeQuery(suggestion.getQuery()));
		collector.add(suggestion);
	}

	/**
	 * Tests getQueries(false)
	 */
	@Test
	public void testQueriesFalse() {
		List<String> queries = collector.getQueries(false);
		assertNotNull(queries);
		assertEquals(2, queries.size());
		assertEquals("what:\"Natural history\"", queries.get(0));
		assertEquals("DATA_PROVIDER:\"University of California Libraries \\(archive.org\\)\"", queries.get(1));
	}

	/**
	 * Tests getQueries(true)
	 */
	@Test
	public void testQueriesTrue() {
		List<String> queries = collector.getQueries(true);
		assertNotNull(queries);
		assertEquals(2, queries.size());
		assertEquals("{!id=0}what:\"Natural history\"", queries.get(0));
		assertEquals("{!id=1}DATA_PROVIDER:\"University of California Libraries \\(archive.org\\)\"", queries.get(1));
	}

	/**
	 * Tests getQueries()
	 */
	@Test
	public void testQueries() {
		List<String> queries = collector.getQueries(true);
		assertNotNull(queries);
		assertEquals(2, queries.size());
		assertEquals("{!id=0}what:\"Natural history\"", queries.get(0));
		assertEquals("{!id=1}DATA_PROVIDER:\"University of California Libraries \\(archive.org\\)\"", queries.get(1));
	}

	/**
	 * Tests get(fieldname) method
	 */
	@Test
	public void testGet() {
		List<MltSuggestion> suggestions = collector.get("what");
		assertNotNull(suggestions);
		assertEquals(1, suggestions.size());
		assertEquals("what:\"Natural history\"", suggestions.get(0).getEscapedQuery());
		assertEquals("{!id=0}what:\"Natural history\"", suggestions.get(0).getTaggedEscapedQuery());
	}

	/**
	 * Tests findById(id) method
	 */
	@Test
	public void testFindById() {
		MltSuggestion suggestion = collector.findById(0);
		assertNotNull(suggestion);
		assertEquals("what:\"Natural history\"", suggestion.getEscapedQuery());
		assertEquals("{!id=0}what:\"Natural history\"", suggestion.getTaggedEscapedQuery());
	}
}
