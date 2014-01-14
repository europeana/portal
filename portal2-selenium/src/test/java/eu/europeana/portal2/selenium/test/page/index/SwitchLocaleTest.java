package eu.europeana.portal2.selenium.test.page.index;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import eu.europeana.portal2.selenium.page.IndexPage;
import eu.europeana.portal2.selenium.test.PortalConfig;
import eu.europeana.portal2.selenium.test.abstracts.TestSetup;

public class SwitchLocaleTest extends TestSetup {

	private IndexPage page;
	
	@Before
	@Override
	public void setupPage() {
		super.setupPage(); // create webDriver instance
		page = IndexPage.openPage(webDriver);
	}
	

	@Test
	public void testLocaleMenu() {
		assertEquals("Not having right amount of locales", PortalConfig.INDEX_COUNT_LOCALES, page.countLocaleItems());
	}
	

}
