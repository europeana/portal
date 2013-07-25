package eu.europeana;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

// mvn surefire-report:report


public class BasicSearchTest extends EuropeanaTest{

	public BasicSearchTest() {}


	/***
	 * Search for 'paris'
	 */
	private void searchForParis() {
		WebElement searchText = driver.findElement(By.id("query-input"));
		searchText.sendKeys("paris");
		searchText.submit();

		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return d.getTitle().toLowerCase()
						.startsWith("paris - europeana - search results");
			}
		});
	}

	// @Test(description = "Search 'Paris', check page title and that there are at least 12 results.")
	public void runBasicSearch() throws IndexOutOfBoundsException {

		Assert.assertEquals(driver.getTitle(), "Europeana - Homepage", "Check page title is ok");

		searchForParis();

		String cssSelectorResults = "#search-results div ul[class='navigation-pagination'] li:nth-child(1)";

		WebElement resultsElement = driver.findElement(By.cssSelector(cssSelectorResults));

		Assert.assertEquals(resultsElement.getText().substring(0, 14), "Results 1 - 12", "Check there are at least 12 results");
	}

	// @Test(description = "Search 'Paris', make a refinement - check the breadcrumb text is as expected.")
	public void runRefinedSearch() {

		Assert.assertEquals(driver.getTitle(), "Europeana - Homepage", "Check page title is ok");

		searchForParis();

		String cssSelectorSubmit = "#refine-search-form fieldset input[type='submit']";
		String cssSelectorBreadcrumbList = "#breadcrumb li";

		WebElement refineLink	= driver.findElement(By.id("refine-search"));
		WebElement refineText	= driver.findElement(By.id("rq"));
		WebElement refineSubmit	= driver.findElement(By.cssSelector(cssSelectorSubmit));

		Assert.assertFalse(refineSubmit.isDisplayed(), "Check refinement submit button is not visible");

		refineLink.click();

		Assert.assertTrue(refineSubmit.isDisplayed(), "Check refinement submit button is visible");

		refineText.sendKeys("painting");
		refineText.submit();

		new WebDriverWait(driver, 10).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				String cssSelectorSubmit = "#query-search fieldset input[value='Search']";
				return d.findElement(By.cssSelector(cssSelectorSubmit)).isDisplayed();
			}
		});

		List<WebElement> breadcrumbs = driver.findElements(By.cssSelector(cssSelectorBreadcrumbList));

		Assert.assertEquals(breadcrumbs.size(), 3, "Check there are 3 breadcrumbs");

		String breadcrumbInner1 = breadcrumbs.get(0).findElement(By.cssSelector("span")).getText();
		String breadcrumbInner2 = breadcrumbs.get(1).findElement(By.cssSelector("a")).getText();
		String breadcrumbInner3 = breadcrumbs.get(2).findElement(By.cssSelector("b")).getText();

		Assert.assertEquals(breadcrumbInner1, "Matches for:",	"Check 1st breadcrumb text");
		Assert.assertEquals(breadcrumbInner2, "paris",			"Check 2nd breadcrumb text");
		Assert.assertEquals(breadcrumbInner3, "text:painting",	"Check 3rd breadcrumb text");
	}

	// @Test(description = "Search 'Paris', check that timeline and map options are available.")
	public void testGridTimelineMapOptionsPresent() {

		Assert.assertEquals(driver.getTitle(), "Europeana - Homepage", "Check page title is ok");

		searchForParis();

		String cssSelectorTimelineLink	= "ul[class='navigation-icons'] li a[class='timeline']";
		String cssSelectorMapLink		= "ul[class='navigation-icons'] li a[class='mapview']";

		WebElement timelineLink		= driver.findElement(By.cssSelector(cssSelectorTimelineLink));
		WebElement mapLink			= driver.findElement(By.cssSelector(cssSelectorMapLink));

		Assert.assertNotNull(timelineLink);
		Assert.assertNotNull(mapLink);

	}

	// @Test(description = "Search 'Paris', check that facets appear and can be opened and closed.")
	public void testFacets() {

		Assert.assertEquals(driver.getTitle(), "Europeana - Homepage", "Check page title is ok");

		searchForParis();

		WebElement facetList = driver.findElement(By.id("filter-search"));

		// media type facets
		final WebElement elImage = facetList.findElement(By.cssSelector("li ul li h4 a[title='IMAGE']"));
		Assert.assertNotNull(elImage);
		Assert.assertNotNull(facetList.findElement(By.cssSelector("li ul li h4 a[title='TEXT']")));
		Assert.assertNotNull(facetList.findElement(By.cssSelector("li ul li h4 a[title='VIDEO']")));
		Assert.assertNotNull(facetList.findElement(By.cssSelector("li ul li h4 a[title='SOUND']")));

		// language facets
		final WebElement elFrench = facetList.findElement(By.cssSelector("li ul li h4 a[title='fr']"));
		Assert.assertNotNull(elFrench);
		Assert.assertNotNull(facetList.findElement(By.cssSelector("li ul li h4 a[title='de']")));
		Assert.assertNotNull(facetList.findElement(By.cssSelector("li ul li h4 a[title='mul']")));
		Assert.assertNotNull(facetList.findElement(By.cssSelector("li ul li h4 a[title='es']")));
		Assert.assertNotNull(facetList.findElement(By.cssSelector("li ul li h4 a[title='en']")));
		Assert.assertNotNull(facetList.findElement(By.cssSelector("li ul li h4 a[title='nl']")));
		Assert.assertNotNull(facetList.findElement(By.cssSelector("li ul li h4 a[title='yi']")));
		Assert.assertNotNull(facetList.findElement(By.cssSelector("li ul li h4 a[title='fi']")));
		Assert.assertNotNull(facetList.findElement(By.cssSelector("li ul li h4 a[title='no']")));
		Assert.assertNotNull(facetList.findElement(By.cssSelector("li ul li h4 a[title='pt']")));

		// country facets
		final WebElement elFrance = facetList.findElement(By.cssSelector("li ul li h4 a[title='france']"));
		Assert.assertNotNull(elFrance);
		Assert.assertNotNull(facetList.findElement(By.cssSelector("li ul li h4 a[title='germany']")));
		Assert.assertNotNull(facetList.findElement(By.cssSelector("li ul li h4 a[title='spain']")));
		Assert.assertNotNull(facetList.findElement(By.cssSelector("li ul li h4 a[title='belgium']")));
		Assert.assertNotNull(facetList.findElement(By.cssSelector("li ul li h4 a[title='europe']")));
		Assert.assertNotNull(facetList.findElement(By.cssSelector("li ul li h4 a[title='switzerland']")));
		Assert.assertNotNull(facetList.findElement(By.cssSelector("li ul li h4 a[title='netherlands']")));

		// copyright facets
		final WebElement elFreeAccess = facetList.findElement(By.cssSelector("li ul li h4 a[title='Free Access - Rights Reserved']"));
		Assert.assertNotNull(elFreeAccess);
		Assert.assertNotNull(facetList.findElement(By.cssSelector("li ul li h4 a[title='Public Domain marked']")));
		Assert.assertNotNull(facetList.findElement(By.cssSelector("li ul li h4 a[title='CC0']")));
		Assert.assertNotNull(facetList.findElement(By.cssSelector("li ul li h4 a[title='Paid Access - Rights Reserved']")));

		// show / hide
		List<WebElement> showHideButtons = facetList.findElements(By.cssSelector("li h3 a "));

		Assert.assertEquals(showHideButtons.size(), 7, "Check there are 7 facets");

		// by default only the media type facet should be visible - all others
		// should be hidden until toggled by a click on the open-facet link
		for (WebElement showHideButton : showHideButtons) {
			if (showHideButton.getText().equalsIgnoreCase("By Media Type")) {
				Assert.assertTrue(elImage.isDisplayed());
				showHideButton.click();
				new WebDriverWait(driver, 100)
						.until(new ExpectedCondition<Boolean>() {
							public Boolean apply(WebDriver d) {
								return !elImage.isDisplayed();
							}
						});
				Assert.assertFalse(elImage.isDisplayed());
			}

			if (showHideButton.getText().equalsIgnoreCase("By Country")) {
				Assert.assertFalse(elFrance.isDisplayed());
				showHideButton.click();
				new WebDriverWait(driver, 100)
						.until(new ExpectedCondition<Boolean>() {
							public Boolean apply(WebDriver d) {
								return elFrance.isDisplayed();
							}
						});
				Assert.assertTrue(elFrance.isDisplayed());
			}

			if (showHideButton.getText().equalsIgnoreCase("By Language")) {
				Assert.assertFalse(elFrench.isDisplayed());
				showHideButton.click();
				new WebDriverWait(driver, 100)
						.until(new ExpectedCondition<Boolean>() {
							public Boolean apply(WebDriver d) {
								return elFrench.isDisplayed();
							}
						});
				Assert.assertTrue(elFrench.isDisplayed());
			}

			if (showHideButton.getText().equalsIgnoreCase("By Copyright")) {
				Assert.assertFalse(elFreeAccess.isDisplayed());
				showHideButton.click();
				new WebDriverWait(driver, 100)
						.until(new ExpectedCondition<Boolean>() {
							public Boolean apply(WebDriver d) {
								return elFreeAccess.isDisplayed();
							}
						});
				Assert.assertTrue(elFreeAccess.isDisplayed());
			}
		}
	}

	private class ScreenshotHelper {
		public void saveScreenshot(String screenshotFileName) throws IOException {
			File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screenshot, new File(screenshotFileName));
			// JOptionPane.showMessageDialog(new JPanel(), "Saved screenshot to " + new File(screenshotFileName).getAbsolutePath() );
		}
	}
	
}




