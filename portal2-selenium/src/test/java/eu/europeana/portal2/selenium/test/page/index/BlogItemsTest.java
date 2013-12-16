package eu.europeana.portal2.selenium.test.page.index;

import org.junit.Test;

import eu.europeana.portal2.selenium.page.IndexPage;
import eu.europeana.portal2.selenium.test.abstracts.TestSetup;

public class BlogItemsTest extends TestSetup {

	private IndexPage page;

	@Test
	public void countBlogItemsTest() {
		page.countBlogItems();
	}

}
