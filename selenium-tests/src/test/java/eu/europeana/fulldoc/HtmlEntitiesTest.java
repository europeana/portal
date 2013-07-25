package eu.europeana.fulldoc;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import eu.europeana.EuropeanaTest;
import eu.europeana.Utils;
import eu.europeana.WebWindow;

public class HtmlEntitiesTest extends EuropeanaTest{
	

	public HtmlEntitiesTest(){}
	
	
	@Test(description = "Make sure there are no escaped  non-ascii characters on the page.")
	public void testForHtmlEntities() throws Exception{
		Utils.searchFor(driver, "Ty&#353;ler");	// TODO - search will not contain escaped entities when the solr bug is fixed
		
		StringBuffer verificationErrors = new StringBuffer();
		boolean doContinue = true;
		int resultPageNum = 1;
		while(doContinue){

			System.err.println("Page " + resultPageNum);
			
			try{
				List<WebElement> thumbLinks = driver.findElements(By.cssSelector("div#search-results div#items div.li div.thumb-frame a"));
			
				for(WebElement thumbLink :thumbLinks){
	
					System.err.println("  - " + thumbLink.getAttribute("href"));
					WebWindow	ww = null;
					String		msg = "";
					try{
						
						ww = new WebWindow(driver, thumbLink.getAttribute("href") );
						
						// wait for separate object page to laod
						
						(new WebDriverWait(driver, 15)).until(new ExpectedCondition<Boolean>() {
							public Boolean apply(WebDriver d) {
								return d.findElement(By.cssSelector("div#item-details")).isDisplayed();
							}
						});
						
						// make sure that there are no &[esc]; elements on the page
						
						String txt			= driver.findElement(By.cssSelector("div#item-details")).getText();
						Pattern pattern		= Pattern.compile("&(.*?);", Pattern.CASE_INSENSITIVE);
					    Matcher matcher		= pattern.matcher(txt);
					    
					    while (matcher.find()){
					    	msg = "Escaped entity (" + matcher.group() + ") present in item data: " +  driver.getCurrentUrl();
					    	Assert.assertTrue(false, "");
					    }
						
					}
					catch(AssertionError e){
						verificationErrors.append(msg + "\n");
					}
					catch(ElementNotVisibleException e){
						verificationErrors.append(msg + "\n");
					}
					catch(Exception e){
						verificationErrors.append(msg + "\n");
					}
					finally{
						ww.close();					
					}		
				}
			}
			catch(Exception e){
		        String verificationErrorString = verificationErrors.toString();
				if (!"".equals(verificationErrorString)) {
		        	throw new AssertionError(verificationErrorString);
		        }
				else{
					throw e;
					
				}
			}

			WebElement nextPageLink = null;
			try{
				nextPageLink = driver.findElement(By.cssSelector("div.nav-top li.nav-next a"));
			}
			catch(NoSuchElementException e){
				doContinue = false;
			}
			if(nextPageLink != null){
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
					verificationErrors.append("Error getting next page: " + nexPageUrl);
		        	throw new AssertionError(verificationErrors.toString());
				}				
			}
		}
		
        String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
        	throw new AssertionError(verificationErrorString);
        }

	}
	
}
