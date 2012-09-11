package eu.europeana.portal2.web.controllers;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import eu.europeana.portal2.web.controllers.utils.RSSFeedParser;
import eu.europeana.portal2.web.controllers.utils.RSSImage;
import eu.europeana.portal2.web.presentation.model.data.submodel.FeedEntry;

/**
 * Tests the Pinterest feed related functions.
 * 
 * @author peter.kiraly@kb.nl
 */
public class PinterestTest {
	
	// TODO: use it later, to test something else
	// private List<FeedEntry> pinterestEntries;
	// private Calendar pinterestAge;

	private String pintFeedUrl = "http://pinterest.com/europeana/feed.rss";
	private Integer pintItemLimit = 20;
	private RSSFeedParser parser;

	@Before
	public void runBeforeEveryTests() {
		parser = new RSSFeedParser(pintFeedUrl, pintItemLimit.intValue(), new String[0], new int[0]);
	}

	/**
	 * Tests the image extraction
	 */
	@Test
	public void testImageExtraction() {
		
		List<FeedEntry> newEntries = parser.readFeed();
		if ((newEntries != null) && (newEntries.size() > 0)) {
			for (FeedEntry entry : newEntries) {
				for (RSSImage image : entry.getImages()) {
					assertNotNull(image.getSrc());
					assertTrue(image.getSrc().length() > 0);
					// System.out.println(image.getTitle());
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
