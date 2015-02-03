package eu.europeana.portal2.web.util.rss;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import eu.europeana.portal2.services.ResponsiveImageService;
import eu.europeana.portal2.web.presentation.model.data.submodel.FeedEntry;

/**
 * Tests the Pinterest feed related functions.
 * 
 * @author peter.kiraly@kb.nl
 */
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/portal2-test.xml"})
public class PinterestTest {
	
	@Resource
	private ResponsiveImageService responsiveImageService;
	
	// TODO: use it later, to test something else
	// private List<FeedEntry> pinterestEntries;
	// private Calendar pinterestAge;

	private String pintFeedUrl = "http://pinterest.com/europeana/feed.rss";
	private Integer pintItemLimit = 20;
	private RSSFeedParser parser;

	@Before
	public void runBeforeEveryTests() {
		parser = new RSSFeedParser(pintFeedUrl, pintItemLimit.intValue());
	}

	/**
	 * Tests the image extraction
	 */
	@Test
	public void testImageExtraction() {
		boolean error = false;
		try{
			List<FeedEntry> newEntries = parser.readFeed(responsiveImageService);
			if(newEntries == null){
				error = true;				
			}
			else if (newEntries.size() > 0) {
				for (FeedEntry entry : newEntries) {
					for (RSSImage image : entry.getImages()) {
						assertNotNull(image.getSrc());
						assertTrue(image.getSrc().length() > 0);
					}
				}
			}			
		}
		catch(Exception e){
			error = true;
		}
		assertTrue("Feed should be read without Exception being thrown", !error);
	}

	/**
	 * Tests the getPlainDescription function of FeedEntry object 
	 */
	@Test
	public void testPlainDescription() {
		List<FeedEntry> newEntries = parser.readFeed(responsiveImageService);
		if ((newEntries != null) && (newEntries.size() > 0)) {
			for (FeedEntry entry : newEntries) {
				assertTrue(entry.getPlainDescription().indexOf("<") == -1);
				assertTrue(entry.getPlainDescription().indexOf(">") == -1);
			}
		}
	}
}
