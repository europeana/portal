package eu.europeana.portal2.selenium.test.abstracts;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.saucelabs.selenium.client.factory.SeleniumFactory;

public abstract class TestSetup {

	protected WebDriver webDriver;

	@Rule
	public TestName testName = new TestName();

	public WebDriver setupDriver() {
		WebDriver driver = null;
		if (StringUtils.isNotBlank(System.getenv("SELENIUM_BROWSER"))) {
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability("name", this.getClass().getSimpleName() + " . " + testName.getMethodName());
			capabilities.setCapability("record-video", false);
			driver = SeleniumFactory.createWebDriver(capabilities);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		} else {
			driver = new FirefoxDriver();
		}
		return driver;
	}

	@Before
	public void setupPage() {
		webDriver = setupDriver();
		String sessionId = ((RemoteWebDriver) webDriver).getSessionId().toString();
		System.out.println("SauceOnDemandSessionID=" + sessionId);
	}

	@After
	public void closePage() {
		webDriver.quit();
	}

}
