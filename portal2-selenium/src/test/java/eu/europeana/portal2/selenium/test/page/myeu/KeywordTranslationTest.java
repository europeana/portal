package eu.europeana.portal2.selenium.test.page.myeu;

/**
 * Coverage for this bug:
 * 
 * Query translation is not enabled automatically when you're not logged in 
 * 
 * https://www.assembla.com/spaces/europeana/tickets/1793-query-translation-is-not-enabled-automatically-when-you-re-not-logged-in#/activity/ticket:
 * 
 * */

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import eu.europeana.portal2.selenium.page.MyEuropeanaPage;
import eu.europeana.portal2.selenium.page.SearchPage;
import eu.europeana.portal2.selenium.test.abstracts.TestSetup;

public class KeywordTranslationTest extends TestSetup {
	
	private MyEuropeanaPage page;
	
	@Before
	@Override
	public void setupPage() {
		enableScreenshots = false;
		super.setupPage(); // create webDriver instance
	}

	@Test
	public void translateTermsApplied() {
		page = MyEuropeanaPage.openPage(webDriver, "#language-settings");
		page.setLanguageOptions();
		page.setSearchQuery("paris");
		page.clickSearch();
		
		SearchPage searchPage = SearchPage.checkPage(webDriver);

		// check that translation have been applied

		assertTrue("Query translate should be applied", searchPage.getCBQT().isSelected() );
	}

}


