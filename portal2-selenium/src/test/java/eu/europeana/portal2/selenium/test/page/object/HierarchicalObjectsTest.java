package eu.europeana.portal2.selenium.test.page.object;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import eu.europeana.portal2.selenium.page.ObjectPage;
import eu.europeana.portal2.selenium.test.abstracts.TestSetup;

public class HierarchicalObjectsTest extends TestSetup {
	
	private ObjectPage page;
	
	@Before
	@Override
	public void setupPage() {
		enableScreenshots = false;
		super.setupPage(); // create webDriver instance
		page = ObjectPage.openPage(webDriver, "ho=true");		
	}
	
	// @Test
	public void openNodeTest() {
		
		new WebDriverWait(webDriver, 15).until(new ExpectedCondition() {
			@Override
			public Object apply(Object o){
				WebDriver webDriver = (WebDriver)o;

				String sCountPrev   = webDriver.findElement(By.cssSelector(".hierarchy-prev .count")).getText();
				String sCountNext   = webDriver.findElement(By.cssSelector(".hierarchy-next .count")).getText();
				
				return new Boolean( sCountPrev.length() > 0 && sCountNext.length() > 0);
			}
		});
		
		WebElement root		= webDriver.findElement(By.cssSelector("#root"));
		WebElement prevLink	= webDriver.findElement(By.cssSelector(".hierarchy-prev a"));
		WebElement nextLink	= webDriver.findElement(By.cssSelector(".hierarchy-next a"));

		String sCountNext   = webDriver.findElement(By.cssSelector(".hierarchy-next .count")).getText();
		sCountNext          = sCountNext.replace("(", "");
		sCountNext          = sCountNext.replace(" items)", "");
		int countNext		= Integer.parseInt(sCountNext);

		
		String sCountPrev   = webDriver.findElement(By.cssSelector(".hierarchy-prev .count")).getText();
		sCountPrev          = sCountPrev.replace("(", "");
		sCountPrev          = sCountPrev.replace(" items)", "");
		int countPrev		= Integer.parseInt(sCountPrev);

		assertNotNull("Root element should exist",	root);
		assertNotNull("Previous Link should exist",	prevLink);
		assertNotNull("Next Link should exist",		nextLink);
		
		prevLink.click();
		
	
		try{
			Thread.sleep(3000);
		}
		catch(InterruptedException e){
			System.err.println(e.getStackTrace());
		}
		
		sCountPrev   		= webDriver.findElement(By.cssSelector(".hierarchy-prev .count")).getText();
		sCountNext  	 	= webDriver.findElement(By.cssSelector(".hierarchy-next .count")).getText();
		sCountPrev          = sCountPrev.replace("(", "");
		sCountPrev          = sCountPrev.replace(" items)", "");
		sCountNext          = sCountNext.replace("(", "");
		sCountNext          = sCountNext.replace(" items)", "");

		int countNextNew	= Integer.parseInt(sCountNext);
		int countPrevNew	= Integer.parseInt(sCountPrev);

		assertTrue("Next count should have been updated",     countNextNew != countNext );
		assertTrue("Previous count should have been updated", countPrevNew != countPrev );
		
	}
}
