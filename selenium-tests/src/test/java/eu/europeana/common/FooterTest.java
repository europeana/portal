package eu.europeana.common;

import org.openqa.selenium.By;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import org.testng.annotations.Test;

import eu.europeana.EuropeanaTest;


public class FooterTest extends EuropeanaTest{

	public FooterTest(){}
	
	// newsletter popup test
	
	@Test(description = "Check newsletter popup opens.")
	public void testNewsletterPopup() throws Exception{
		WebElement buttonOpen  = driver.findElement(By.cssSelector("#newsletter-trigger"));
		buttonOpen.click();
		
		try{
			(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver d) {
					return d.findElement(By.cssSelector(".close")).isDisplayed();
				}
			});			
		}
		catch(TimeoutException e){
			Assert.assertTrue(false, "Newsletter popup did not open");
		}
		
		Assert.assertTrue( driver.findElement(By.cssSelector(".iframe-wrap .close")).isDisplayed(), "Close button should be visible");

		WebElement buttonClose = driver.findElement(By.cssSelector(".iframe-wrap .close"));
		buttonClose.click();
		
		Assert.assertFalse( driver.findElement(By.cssSelector(".iframe-wrap .close")).isDisplayed(), "Popup should not be visible" );
	}
}
