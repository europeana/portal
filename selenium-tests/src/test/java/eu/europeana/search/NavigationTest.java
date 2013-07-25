package eu.europeana.search;

import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import eu.europeana.EuropeanaTest;
import eu.europeana.Utils;


import org.testng.Assert;

/**
 * 
 * @author Andy MacLean
 *
 * Tests the navigation components on the search page:
 * 
 * assert next link works across result set
 * assert previous link works across result set
 * assert start-page text field can be used to jump to all pages
 * assert start-page text field cannot be used to jump to inexistent page 
 * 
 * TODO first link works across result set
 * TODO last link works across result set
 *
 */

public class NavigationTest extends EuropeanaTest{

	String prevResText		= "";
	int resPerPage			= 24;
	int totalResults		= 0;
	By selectorNavNext		= By.cssSelector("div.nav-top li.nav-next a");
	By selectorNavPrev		= By.cssSelector("div.nav-top li.nav-prev a");
	By selectorNavFirst		= By.cssSelector("div.nav-top li.nav-first a");
	By selectorNavLast		= By.cssSelector("div.nav-top li.nav-last a");
	By selectorResultPg		= By.cssSelector("div.nav-top .count span"); 
	By selectorStartPage	= By.cssSelector("div.nav-top #start-page"); 

	By selectorResSizeLabel	= By.cssSelector("div.nav-top .eu-menu li.item a.menu-label"); 
	By selectorResSizeOpen	= By.cssSelector("div.nav-top .eu-menu .open-menu"); 
	By selectorResSize12	= By.cssSelector("div.nav-top .eu-menu li.item:nth-child(1) a"); 
	By selectorResSize24	= By.cssSelector("div.nav-top .eu-menu li.item:nth-child(2) a"); 
	By selectorResSize48	= By.cssSelector("div.nav-top .eu-menu li.item:nth-child(3) a"); 
	By selectorResSize96	= By.cssSelector("div.nav-top .eu-menu li.item:nth-child(4) a"); 
	By selectorResThumbs	= By.cssSelector(".thumb-frame");
	By selectorRes1stTitle	= By.cssSelector("#search-results #items .li:nth-child(1) .ellipsis-inner"); 
	
	
	
	@Test(description = "Search *:* and check that the fist and last page links behave as expected")
	public void testFirstAndLastPageLinks(){
		
		Utils.searchFor(driver, "*:*");
		
		WebElement navLast	= driver.findElement(selectorNavLast);
		WebElement results	= driver.findElement(selectorResultPg);
		
		prevResText 	= results.getText();
		totalResults	= getTotalResultCount();
		int noPages		= totalResults / 24 + ( totalResults % 24 > 0 ? 1 : 0 );

		// jump to last page
		
		navLast.click();

		(new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return	d.findElement(selectorResultPg).isDisplayed() && (! d.findElement(selectorResultPg).getText().equals(prevResText));
		}});

		
		// Assert next and last links are hidden, and that the 
		
		Assert.assertEquals(	driver.findElement(selectorStartPage).getAttribute("value"), "" + noPages,	"Last page has the number we expect");
		Assert.assertFalse(		Utils.isElementPresent(driver, selectorNavLast),							"Last page link should not be present");
		Assert.assertFalse(		Utils.isElementPresent(driver, selectorNavNext),							"Next page link should not be present");
		Assert.assertTrue(		driver.findElement(selectorNavFirst).isDisplayed(),							"First page link should show");
		Assert.assertTrue(		driver.findElement(selectorNavPrev).isDisplayed(),							"Previous page link should show");

		// jump to 1st page
		
		prevResText	= driver.findElement(selectorResultPg).getText();
		driver.findElement(selectorNavFirst).click();

		(new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return	d.findElement(selectorResultPg).isDisplayed() && (! d.findElement(selectorResultPg).getText().equals(prevResText));
		}});

		
		Assert.assertEquals(	driver.findElement(selectorStartPage).getAttribute("value"), "1", "Last page has the number we expect");
		Assert.assertTrue(		driver.findElement(selectorNavLast).isDisplayed(),	"Last page link should show");
		Assert.assertTrue(		driver.findElement(selectorNavNext).isDisplayed(),	"Next page link should show");
		Assert.assertFalse(		Utils.isElementPresent(driver, selectorNavFirst),	"First page link should not be present");
		Assert.assertFalse(		Utils.isElementPresent(driver, selectorNavPrev),	"Previous page link should not be present");

	}
	
	
	@Test(description = "Search *:* and check that the next page link behaves as expected")
	public void testNextPageLink(){
		
		Utils.searchFor(driver, "*:*");
		
		WebElement navNext;
		
		prevResText		= "";
		totalResults	= getTotalResultCount();
		int noPages		= totalResults / 24 + ( totalResults % 24 > 0 ? 1 : 0 );
		
		// navigate up the pages to the last

		for(int i=0; i<noPages; i++){
			testResultText(i+1);
			if(i+1<noPages){
				navNext	= driver.findElement(selectorNavNext);
				Assert.assertNotNull(navNext, "find nav next link");
				navNext.click();
				(new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return	d.findElement(selectorResultPg).isDisplayed() && (! d.findElement(selectorResultPg).getText().equals(prevResText));
				}});			
			}
		}
	}
	
	
	@Test(description = "Search *:* and check that the prev page link behaves as expected")
	public void testPrevPageLink(){
		
		Utils.searchFor(driver, "*:*");
		
		WebElement navPrev;
		WebElement navlast = driver.findElement(selectorNavLast);
		
		// jump to last page
		
		prevResText		= "";
		navlast.click();
		(new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return	d.findElement(selectorResultPg).isDisplayed() && (! d.findElement(selectorResultPg).getText().equals(prevResText));
		}});

		// now count down the pages
		
		prevResText		= "";
		totalResults	= getTotalResultCount();
		int noPages		= totalResults / 24 + ( totalResults % 24 > 0 ? 1 : 0 );
		navPrev			= driver.findElement(selectorNavPrev);
		
		for(int i=noPages; i>0; i--){
			testResultText(i);
			if(i-1>0){
				navPrev	= driver.findElement(selectorNavPrev);
				Assert.assertNotNull(navPrev, "find nav prev link");
				navPrev.click();
				(new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return	d.findElement(selectorResultPg).isDisplayed() && (! d.findElement(selectorResultPg).getText().equals(prevResText));
				}});			
			}
		}
	}

	@Test(description = "Search *:* and test that all pages are reachable via the start-page text field")
	public void testTextJump(){
		
		Utils.searchFor(driver, "*:*");
		
		WebElement startPage;
		
		prevResText		= "";
		totalResults	= getTotalResultCount();
		int noPages	= totalResults / 24 + ( totalResults % 24 > 0 ? 1 : 0 );
		
		// navigate up the pages to the last

		for(int i=0; i<noPages; i++){
			testResultText(i+1);
			if(i+1<noPages){
				startPage = driver.findElement(selectorStartPage);
				String nextPageKeys = "" + (Integer.parseInt(startPage.getAttribute("value") ) + 1);
				Assert.assertNotNull(startPage, "find start-page input");
				startPage.clear();
				startPage.sendKeys(nextPageKeys); 
				startPage.submit();
				
				(new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return	d.findElement(selectorResultPg).isDisplayed() && (! d.findElement(selectorResultPg).getText().equals(prevResText));
				}});			
			}
		}
	}
	
	@Test(description = "Search *:* and test that it's not possible to submit a higher page than the total number of pages in the start-page text field")
	public void testMaxPageLimit(){
		
		Utils.searchFor(driver, "*:*");
		
		WebElement startPage = driver.findElement(selectorStartPage);
		
		totalResults		= getTotalResultCount();
		final int noPages	= totalResults / 24 + ( totalResults % 24 > 0 ? 1 : 0 );

		Assert.assertNotNull(startPage, "find start-page input");
		startPage.clear();
		startPage.sendKeys("" + (noPages * 2));
		startPage.submit();
				
		(new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return	d.findElement(selectorResultPg).isDisplayed() 
						&&
						( d.findElement(selectorStartPage).getAttribute("value").equals("" + noPages));
		}});			
	}
	

	@Test(description = "Search *:* and check that the number of displayed results corresponds to the selected value in the result size menu")
	public void testResultSizes(){
		
		Utils.searchFor(driver, "*:*");
		
		
		Assert.assertEquals(driver.findElements(selectorResThumbs).size(), 24, "Expect 24 results (default)");
		WebElement results			= null;
		WebElement resultSizeOption	= null;
		
		HashMap <Integer, By> options = new HashMap<Integer, By>();
		options.put(12, selectorResSize12);
		options.put(24, selectorResSize24);
		options.put(48, selectorResSize48);
		options.put(96, selectorResSize96);
		
		Iterator<Integer> itOptions = options.keySet().iterator();
		Integer expectedResSize = 0;
		
		while(itOptions.hasNext()){
			expectedResSize = itOptions.next();
			
			By menuOptionSelector = options.get(expectedResSize);
			
			driver.findElement(selectorResSizeOpen).click();
			
			resultSizeOption	= driver.findElement(menuOptionSelector); 
			results				= driver.findElement(selectorResultPg);
			prevResText 		= results.getText();

			resultSizeOption.click();
		
			(new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver d) {
					return	d.findElement(selectorResultPg).isDisplayed() && (! d.findElement(selectorResultPg).getText().equals(prevResText));
			}});

			Assert.assertEquals(driver.findElements(selectorResThumbs).size(), expectedResSize.intValue() , "Expect " + expectedResSize + " results"); 
			
			results		= driver.findElement(selectorResultPg);
			prevResText = results.getText();
		} 
	}
	
	
	

	@Test(description = "Search *:*, go to the next page of results and that the 1st item displayed remains constant through changes to the result size menu")
	public void testResultSizeMaintains1stItem(){
		
		// load and navigate to 2nd page
		
		Utils.searchFor(driver, "*:*");
		
		WebElement results	= driver.findElement(selectorResultPg);
		WebElement navNext	= driver.findElement(selectorNavNext);
		prevResText 		= results.getText();
		totalResults		= getTotalResultCount();

		navNext.click();
		(new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return	d.findElements(By.cssSelector(".ellipsis-inner") ).size() == 24;
		}});			

		// get the title of the 1st result in the grid
		
		selectorRes1stTitle	= By.cssSelector("#search-results #items .li:first-child a div.ellipsis div.ellipsis-inner");
		String title1st		= driver.findElement(selectorRes1stTitle).getText();
		prevResText			= "";
		
		Assert.assertEquals(driver.findElements(selectorResThumbs).size(), 24, "Expect 24 results (default)");
		WebElement resultSizeOption	= null;
		
		HashMap <Integer, By> options = new HashMap<Integer, By>();
		options.put(12, selectorResSize12);
		options.put(24, selectorResSize24);
		options.put(48, selectorResSize48);
		options.put(96, selectorResSize96);
		
		Iterator<Integer> itOptions = options.keySet().iterator();
		Integer expectedResSize = 0;
		
		while(itOptions.hasNext()){
			expectedResSize = itOptions.next();
			
			driver.findElement(selectorResSizeOpen).click();
			
			resultSizeOption	= driver.findElement(options.get(expectedResSize)); 
			results				= driver.findElement(selectorResultPg);
			prevResText 		= results.getText();

			resultSizeOption.click();
		
			(new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver d) {
					return	d.findElement(selectorResultPg).isDisplayed() && (! d.findElement(selectorResultPg).getText().equals(prevResText));
			}});

			Assert.assertEquals( driver.findElement(selectorRes1stTitle).getText(), title1st, "Expect 1st item to remain the same when result size changed");
			results		= driver.findElement(selectorResultPg);
			prevResText = results.getText();
		} 
	}
	
	
	
	
	
	// Helper functions
	
	private int getTotalResultCount(){
		String sTotal	= driver.findElement(selectorResultPg).getText();
		sTotal			= sTotal.substring(sTotal.indexOf("of ") + 3, sTotal.length());
		sTotal			= sTotal.replaceAll(",", "");
		return Integer.parseInt(sTotal);
	}

	private void testResultText(int pageNum){
		WebElement results	= driver.findElement(selectorResultPg);
		String expected = "Results  " + (((pageNum -1) * resPerPage) + 1) + "-" + ((pageNum * resPerPage) > totalResults ? totalResults : (pageNum * resPerPage)) + " of " + totalResults;
		
		//System.err.println("E=" + expected);
		//System.err.println("T=" + results.getText());
		
		Assert.assertTrue(results.getText().equals(expected), "Check nav text equals " + expected );
		prevResText = results.getText();
	}
	
}
