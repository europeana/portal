package eu.europeana.portal2.selenium.test.abstracts;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.saucelabs.selenium.client.factory.SeleniumFactory;

public abstract class TestSetup {
	
	protected WebDriver webDriver;

	public WebDriver setupDriver() {
	    WebDriver driver = null;
	    if (StringUtils.isNotBlank(System.getenv("SELENIUM_BROWSER"))) {
	    	driver = SeleniumFactory.createWebDriver();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	    } else {
	    	driver = new FirefoxDriver();
	    }
	    return driver;
	}
	

	@Before
	public void setupPage() {
		webDriver = setupDriver();
	}

	@After
	public void closePage() {
		webDriver.quit();
	}


}
