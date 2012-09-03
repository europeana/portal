package eu.europeana.portal2.web.controllers;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import eu.europeana.portal2.web.controllers.utils.RSSFeedParser;
import eu.europeana.portal2.web.controllers.utils.RSSImage;
import eu.europeana.portal2.web.presentation.model.data.submodel.FeedEntry;

/**
 * Testing the blog parsing features
 * 
 * @author peter.kiraly@kb.nl
 */
public class BlogTest {
	
	// TODO: use it later, to test something else
	// private List<FeedEntry> pinterestEntries;
	// private Calendar pinterestAge;

	private String blogFeedUrl = "http://blog.europeana.eu/feed/";
	private Integer blogItemLimit = 3;
	private RSSFeedParser parser;

	@Before
	public void runBeforeEveryTests() {
		parser = new RSSFeedParser(blogFeedUrl, blogItemLimit.intValue());
	}

	/**
	 * Tests the image extraction
	 */
	@Test
	public void testImageExtraction() {
		
		List<FeedEntry> newEntries = parser.readFeed();
		if ((newEntries != null) && (newEntries.size() > 0)) {
			for (FeedEntry entry : newEntries) {
				assertTrue(entry.getImages().size() > 0);
				for (RSSImage image : entry.getImages()) {
					assertNotNull(image.getSrc());
					assertTrue(image.getSrc().length() > 0);
				}
			}
		}
	}

	/**
	 * Tests the getPlainDescription function of FeedEntry object 
	 */
	@Test
	public void testPlainDescription() {
		List<FeedEntry> newEntries = parser.readFeed();
		if ((newEntries != null) && (newEntries.size() > 0)) {
			for (FeedEntry entry : newEntries) {
				assertTrue(entry.getPlainDescription().indexOf("<") == -1);
				assertTrue(entry.getPlainDescription().indexOf(">") == -1);
			}
		}
	}
}
