package eu.europeana.portal2.selenium.test.page.index;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SwitchLocaleTest {
	private WebDriver webDriver;
	
	@Before
	public void setupPage() {
		webDriver = new FirefoxDriver();
	}
	
	@After
	public void closePage() {
		webDriver.quit();
	}
	
	
}
