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

import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import eu.europeana.portal2.selenium.page.MyEuropeanaPage;
import eu.europeana.portal2.selenium.page.SearchPage;
import eu.europeana.portal2.selenium.test.abstracts.TestSetup;

public class KeywordTranslationSavedTest extends TestSetup {
	
	private MyEuropeanaPage page;
	
	@Before
	@Override
	public void setupPage() {
		enableScreenshots = false;
		super.setupPage(); // create webDriver instance
	}

	@Test
	public void translateTermsAppliedLoggedIn() {

		page = MyEuropeanaPage.openPage(webDriver, "#login");
		page.login();

		try{
			// wait for redirect
			Thread.sleep(2000);
		}
		catch(Exception e){}
		
		page = MyEuropeanaPage.openPage(webDriver, "#language-settings");
		page.setLanguageOptions();
		page.setSearchQuery("paris");
		page.clickSearch();
		
		SearchPage searchPage = SearchPage.checkPage(webDriver);

		// check that translation have been applied

		assertTrue("Query translate should be applied for logged in user", searchPage.getCBQT().isSelected() );
	}
}

