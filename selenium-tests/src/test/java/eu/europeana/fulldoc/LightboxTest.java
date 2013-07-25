package eu.europeana.fulldoc;

import java.util.List;
import java.util.Vector;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import eu.europeana.EuropeanaTest;
import eu.europeana.Utils;
import eu.europeana.WebWindow;


public class LightboxTest extends EuropeanaTest{

	public LightboxTest(){
		this.realData = true;
	}
	
	@Test(description = "Test Lightbox / External content links.")
	public void runTestLightbox() throws IndexOutOfBoundsException, Exception {

		String lightboxTriggerSelector = "div.lb-trigger span.label";
		
		Utils.searchFor(driver, "provider_aggregation_edm_isShownAt:*.jpg");
		
		Vector<String> verificationErrors = new Vector<String>();
		
		boolean doContinue = true;
		int resultPageNum = 1;
		while(doContinue){

			System.err.println("Page " + resultPageNum);
			
			List<WebElement> thumbLinks = driver.findElements(By.cssSelector("div#search-results div#items div.li div.thumb-frame a"));
		
			
			for(WebElement thumbLink :thumbLinks){
				
				System.err.println("  - " + thumbLink.getAttribute("href"));
				
				WebWindow ww = new WebWindow(driver, thumbLink.getAttribute("href") );
				
				// wait for separate object page to laod
				
				(new WebDriverWait(driver, 15)).until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return d.findElement(By.cssSelector("div#item-details")).isDisplayed();
					}
				});
	
				// wait for lightbox test to run 
				
				Utils.setString(lightboxTriggerSelector);
				
				(new WebDriverWait(driver, 15)).until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return driver.findElement(By.cssSelector(Utils.getString())) != null;
					}
				});
	
				WebElement trigger = driver.findElement(By.cssSelector(lightboxTriggerSelector));
	
				try{
					Assert.assertTrue(trigger != null, "");
					
					trigger.click();
					
					// close modal (analytics tracking on test)
					driver.switchTo().alert().accept();
						
					(new WebDriverWait(driver, 15)).until(new ExpectedCondition<Boolean>() {
						public Boolean apply(WebDriver d) {
							return d.findElement(By.cssSelector("div.overlaid-content")) != null && d.findElement(By.cssSelector("div.overlaid-content")).isDisplayed() ;
						}
					});

				}
				catch(AssertionError e){
					System.err.println("Error 1: " + driver.getCurrentUrl());
					verificationErrors.add("Lightbox didn't initialise: " + driver.getCurrentUrl() + "\n");
				}
				catch(ElementNotVisibleException e){
					System.err.println("Error 2: " + driver.getCurrentUrl());
					verificationErrors.add("Lightbox didn't initialise: " + driver.getCurrentUrl() + "\n");
				}
				catch(NoAlertPresentException e){
					System.err.println("Error 3: " + driver.getCurrentUrl());
					verificationErrors.add("Lightbox didn't initialise: " + driver.getCurrentUrl() + "\n");
				}
				catch(TimeoutException e){
					System.err.println("Error 4: " + driver.getCurrentUrl());
					verificationErrors.add("Lightbox didn't initialise: " + driver.getCurrentUrl() + "\n");
				}
				catch(Exception e){
					System.err.println("Error 5: " + driver.getCurrentUrl());
					verificationErrors.add("Lightbox didn't initialise: " + driver.getCurrentUrl() + "\n");
				}
				finally{
					ww.close();					
				}		
			}
			

			WebElement nextPageLink = driver.findElement(By.cssSelector("div.nav-top li.nav-next a"));
			if(nextPageLink == null){
				doContinue = false;
			}
			else{
				String nexPageUrl = nextPageLink.getAttribute("href");
				try{
					resultPageNum++;
					Utils.setInt(resultPageNum);
					nextPageLink.click();
					(new WebDriverWait(driver, 20)).until(new ExpectedCondition<Boolean>() {
						public Boolean apply(WebDriver d) {
							return d.getTitle().toLowerCase().endsWith(" - europeana - search results") 
									&& d.findElement(By.cssSelector("div.nav-top input#start-page")).isDisplayed()
									&& (Integer.parseInt( d.findElement(By.cssSelector("div.nav-top input#start-page")).getAttribute("value") )
									 == Utils.getInt());
						}
					});
					
				}
				catch(Exception e){
					verificationErrors.add("Error getting next page: " + nexPageUrl);
		        	throw new AssertionError(verificationErrors.toString());
				}
			}
		}
		
		// end pages loop
        String verificationErrorString = "";
        for(String error: verificationErrors){
        	verificationErrorString += "\n" + error + "\n";
        }
        
        if (!"".equals(verificationErrorString)) {
        	System.err.println("Error Way Out [" + verificationErrors.size()  + "]: " + driver.getCurrentUrl());
        	throw new AssertionError(verificationErrorString);
        }

	
	}
	
}
