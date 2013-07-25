package eu.europeana.search;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import eu.europeana.Config;
import eu.europeana.EuropeanaTest;
import eu.europeana.Utils;


public class SearchTest extends EuropeanaTest {
	
	private final int ROWS = 24;

	public SearchTest(){}
	
	
	@Test(description = "Search 'Paris', check page title and that there are at least 24 results.")
	public void runBasicSearchTest() throws IndexOutOfBoundsException, Exception {

		List<String> queries = Arrays.asList(new String[]{"query=paris"});
		
		testTitle("Europeana - Homepage");
		Utils.searchFor(driver, "paris");
		testTitle("paris - Europeana - Search results");
		
		runTests(queries);
	}
	
	
	@Test(description = "Search 'Paris and milton', check page title, check that there are at least 24 results and check the expected facets are showing.")
	public void runSearchWithRefinement() throws IndexOutOfBoundsException, Exception {

		List<String> queries = Arrays.asList(new String[]{"query=paris", "qf=milton"});
		
		testTitle("Europeana - Homepage");
		
		
		Utils.searchFor(driver, "paris");
		
		(new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return d.findElement(By.id("newKeyword")).isDisplayed();
			}
		});

		WebElement searchText = driver.findElement(By.id("newKeyword"));
		searchText.sendKeys("milton");
		searchText.submit();

		(new WebDriverWait(driver, 15)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return d.getTitle().toLowerCase().endsWith(" - europeana - search results") && d.findElement(By.cssSelector("div#search-results")).isDisplayed();
			}
		});
		
		
		testTitle("paris - Europeana - Search results");
		
		runTests(queries);
	}
	
	
	
	private void runTests(List<String> queries) throws Exception {
		
		List<Pattern> patterns = Utils.transformPatterns(queries);
		patterns.add(Utils.createPattern("rows=" + ROWS));

		testNavigationBar("div.nav-top", queries, patterns);
		testNavigationBar("div.nav-bottom", queries, patterns);
		testSearchResults(queries, patterns);
		
	}

	private void testTitle(String exptectedTitle) {
		Assert.assertEquals(driver.getTitle(), exptectedTitle, "Check page title is ok");
	}
	
	private void testSearchResults(List<String> queries, List<Pattern> patterns) throws Exception {
		System.out.println("testSearchResults");
		List<String> dataTypes = Arrays.asList(new String[]{"text", "image", "video", "sound", "_3d"});
		
		List<WebElement> els = driver.findElements(By.cssSelector("div#search-results div#items div.li"));
		Assert.assertEquals(els.size(), ROWS, "24 results");
		
		for (WebElement el : els) {
			WebElement a = el.findElement(By.cssSelector("a"));
			Assert.assertNotNull(a, "There is a link inside");
			String href = a.getAttribute("href");
			for (Pattern pattern : patterns) {
				Assert.assertTrue(pattern.matcher(href).find(), "Next result set");
			}


			WebElement ellipsis = el.findElement(By.cssSelector("a div.ellipsis-inner"));
			Assert.assertNotNull(ellipsis, "there is an ellipsis-inner");

			Assert.assertNotNull(el.findElement(By.cssSelector("div.thumb-frame")), "There is an image container.");

			WebElement imgLink = el.findElement(By.cssSelector("div.thumb-frame a"));
			Assert.assertEquals(get20chars(a.getAttribute("title")), get20chars(ellipsis.getText()), "Title and elipsis should be the same");
			Assert.assertEquals(imgLink.getAttribute("href"), a.getAttribute("href"), "The two links should point the same object.");
			Assert.assertEquals(imgLink.getAttribute("title"), a.getAttribute("title"), "The two links should point the same object.");
			

			Assert.assertNotNull(imgLink, "There is an image link.");
			Assert.assertEquals(imgLink.getAttribute("rel"), "nofollow", "The image links should have no follow attrib.");

			WebElement img = el.findElement(By.cssSelector("div.thumb-frame a img"));
			Assert.assertNotNull(img, "There is an image.");
			Assert.assertNotNull(img.getAttribute("src"), "There should be a @src.");
			Assert.assertTrue(!StringUtils.isBlank(img.getAttribute("src")), "@src should not be empty");
			Assert.assertNotNull(img.getAttribute("class"), "There should be a @src.");
			Assert.assertEquals(img.getAttribute("class"), "thumbnail", "@class=thumbnail instead of " + img.getAttribute("class"));
			Assert.assertNotNull(img.getAttribute("data-type"), "There should be a @data-type.");
			Assert.assertTrue(dataTypes.contains(img.getAttribute("data-type")), "@data-type=image|text|video|sound|_3d instead of " + img.getAttribute("data-type"));
		}

		// check the first element's concrete values
		WebElement el = driver.findElement(By.cssSelector("div#search-results div#items div.li:nth-child(1)"));
		Assert.assertNotNull(el, "There is a first result");
		
		System.out.println("/testSearchResults");
	}
	
	private void testNavigationBar(String divName, List<String> queries, List<Pattern> patterns) {
		System.out.println("testNavigationBar:" + divName);
		
		WebElement el;

		el = driver.findElement(By.cssSelector(divName  + " div.count span span.of-bracket"));
		
		Assert.assertTrue(   Integer.parseInt(el.getText().trim().replaceAll(",", "") ) > 24, "Check there are at least 24 results");

		el = driver.findElement(By.cssSelector(divName  + " ul.result-pagination li.nav-next a"));
		Assert.assertTrue(el.getAttribute("href").startsWith(Config.searchUrl), "Next result set");
		String href = el.getAttribute("href");
		
		for (Pattern pattern : patterns) {
			Assert.assertTrue(pattern.matcher(href).find(), "Next result set");
		}

		el = driver.findElement(By.cssSelector(divName  + " div.eu-menu span.menu-label"));
		Assert.assertEquals(el.getText().trim(), "24", "'24 elements per page' options should be selected");

		el = driver.findElement(By.cssSelector(divName  + " input#start-page"));
		Assert.assertEquals(el.getAttribute("value"), "1", "First page");

		el = driver.findElement(By.cssSelector(divName  + " form.jump-to-page"));
		Assert.assertNotNull(el, "Result number");
		// Assert.assertEquals(el.getText().trim(), "of 51804", "Result number");
		System.out.println("/testNavigationBar:" + divName);
	}


	
	private String get20chars(String text) {
		text = text.trim();
		if (text.length() > 17) {
			text = text.substring(0, 17);
		}
		return text;
	}

	
}
