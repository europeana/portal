package eu.europeana;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class EuropeanaTest {

	protected WebDriver driver;
	protected boolean realData = false;
	
	@BeforeMethod
	public void openBrowser() {
		driver = Config.openBrowser(this.realData);
	}

	@AfterMethod
	public void closeBrowser() throws IOException {
		Config.closeBrowser(driver);
	}
}
