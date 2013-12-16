package eu.europeana.portal2.selenium.test.abstracts;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public abstract class TestSetup {
	
	protected WebDriver webDriver;

	public WebDriver setupDriver() {
	    WebDriver driver = null;
	    if (StringUtils.isNotBlank(System.getenv("SELENIUM_BROWSER"))) {
			try {
				DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
			    desiredCapabilities.setBrowserName(System.getenv("SELENIUM_BROWSER"));
			    desiredCapabilities.setVersion(System.getenv("SELENIUM_VERSION"));
			    desiredCapabilities.setCapability(CapabilityType.PLATFORM, System.getenv("SELENIUM_PLATFORM"));
				driver = new RemoteWebDriver(
				            new URL("http://europeana:211c9cb8-27a8-404a-8533-2ddbd6ce40c6@ondemand.saucelabs.com:80/wd/hub"),
				            desiredCapabilities);
			} catch (MalformedURLException e) {
			}
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
