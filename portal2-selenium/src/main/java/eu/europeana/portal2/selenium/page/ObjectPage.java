package eu.europeana.portal2.selenium.page;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import eu.europeana.portal2.selenium.Pages;
import eu.europeana.portal2.selenium.page.abstracts.Portal2Page;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import eu.europeana.portal2.selenium.model.object.Node;

public class ObjectPage extends Portal2Page {


	// constructors

	public static ObjectPage openPage(WebDriver driver, String paramsString) {
		driver.get(Pages.OBJECT + "?" + paramsString);
		ObjectPage page = new ObjectPage(driver);
		return page;
	}


	private ObjectPage(WebDriver driver) {
		super(driver);
		

	    (new WebDriverWait(driver, 10))
	    .until(new ExpectedCondition() {

			@Override
			public Object apply(Object o) {
				return ((WebDriver)o).findElement(By.cssSelector("#root")) != null;
//				JavascriptExecutor javascriptExecutor = (JavascriptExecutor)arg;
				//return (Boolean)javascriptExecutor.executeScript("return document.getElementById('root').innerHTML.indexOf('Cultural') > 0");
	//			return (Boolean)javascriptExecutor.executeScript("return document.getElementById('root')");
			}			
        });

		
		
/*	    
	    boolean isListPopulated = (new WebDriverWait(webDriver, 1000))
        .until(new ExpectedCondition() {
            public Boolean apply(WebDriver d) {
                JavascriptExecutor javascriptExecutor = (JavascriptExecutor) webDriver;
                return (Boolean)javascriptExecutor.executeScript("return document.getElementById('ajaxResults').innerHTML.indexOf('ALEX') > 0");
            }
        });
*/
	    /*
		waitFor(new WaitCondition() {
			@Override
			public boolean condition() {
				
				ExpectedCondition<T>
				find
				return driver.findElement(By.cssSelector("#root"))  != null;
			}
		});
		*/
	}

	// counters
	/*
	public int countSearchResults() {
		return countByCss("div#items div.li");
	}

	public int countBreadCrumbs() {
		return countByCss("#breadcrumb li");
	}
	*/

	// getters
	/*
	public String getPaginationString() {
		return PatternUtils.normaliseWhitespace(findOneByCss(CSS_SELECTOR_RESULT_COUNT).getText());
	}
	*/
	/*
	 */
	public List<Node> getNodes() {
		List<WebElement> elements = driver.findElements(By.cssSelector(".jstree-node"));
		List<Node> results = new ArrayList<Node>();

		for (WebElement e : elements) {
			results.add(new Node(driver, e));
		}

		return results;
	}
}
