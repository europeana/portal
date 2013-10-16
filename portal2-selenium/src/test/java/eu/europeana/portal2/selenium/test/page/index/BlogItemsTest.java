package eu.europeana.portal2.selenium.test.page.index;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import eu.europeana.portal2.selenium.page.IndexPage;

public class BlogItemsTest {
	
	private WebDriver webDriver;
	
	private IndexPage page;
	
	@Before
	public void setupPage() {
		webDriver = new FirefoxDriver();
		page = IndexPage.openPage(webDriver);
	}
	
	@After
	public void closePage() {
		webDriver.quit();
	}
	
	@Test
	public void countBlogItemsTest() {
		page.countBlogItems();
	}

}
