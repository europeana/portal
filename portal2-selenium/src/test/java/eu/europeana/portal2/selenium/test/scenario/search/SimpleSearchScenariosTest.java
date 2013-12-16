package eu.europeana.portal2.selenium.test.scenario.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import eu.europeana.portal2.selenium.page.IndexPage;
import eu.europeana.portal2.selenium.page.SearchPage;
import eu.europeana.portal2.selenium.test.abstracts.TestSetup;

public class SimpleSearchScenariosTest extends TestSetup {

	@Test
	public void searchForParisTest() {
		// Search for paris
		IndexPage indexPage = IndexPage.openPage(webDriver);
		indexPage.setSearchQuery("paris");
		indexPage.clickSearch();

		// check the amount of search results
		SearchPage searchPage = SearchPage.checkPage(webDriver);
		assertNotNull("Search page not loaded", searchPage);
		assertEquals("Not displaying 24 results", 24, searchPage.countSearchResults());
		assertTrue("Check search results page title",
				searchPage.getPageTitle().toLowerCase().startsWith("paris - europeana - search results"));

		assertTrue("Check there are at least 24 results",
				StringUtils.startsWith(searchPage.getPaginationString(), "Results 1-24"));
	}

}
