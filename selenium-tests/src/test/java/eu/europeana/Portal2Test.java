package eu.europeana;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Portal2Test {

	//private final String SERVER = "http://localhost:8081";
	//private final String SERVER = "http://test.portal2.eanadev.org";
	
	//private final String baseUrl = SERVER + "/portal/";
	//private final String searchUrl = baseUrl + "search.html?";
	//private final String RIGHTS = "RIGHTS";
	//private final String RIGHTS_PREFIX = RIGHTS + ":";
	//private final String RIGHTS_REGEX = RIGHTS_PREFIX + "(\".*\"|.*\\*)$";
	
	private static WebElement	sWebElement;	// a static WebElement
	private static String		sString;		// a static String
	private static int			sInt;			// a static int

	private WebDriver driver;
	private ScreenshotHelper screenshotHelper;

	public Portal2Test() {
	}

	@BeforeMethod
	public void openBrowser() {
		driver = new FirefoxDriver();
		driver.get(Config.baseUrl);
		screenshotHelper = new ScreenshotHelper(driver);
	}

	@AfterMethod
	public void saveScreenshotAndCloseBrowser() throws IOException {
		screenshotHelper.saveScreenshot("screenshot.png");
		driver.quit();
	}

}
