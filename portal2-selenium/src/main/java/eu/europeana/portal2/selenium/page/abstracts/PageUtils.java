package eu.europeana.portal2.selenium.page.abstracts;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class PageUtils {

	protected WebDriver driver;

	public PageUtils(WebDriver driver) {
		this.driver = driver;
	}

	// PROTECTED METHODS FOR USE IN PAGE OBJECTS ONLY

	// FIND BY ID
	
	protected WebElement findById(String id) {
		return findById(driver, id);
	}

	protected WebElement findById(SearchContext context, String id) {
		return context.findElement(By.id(id));
	}

	// FIND BY CLASS
	
	protected List<WebElement> findByClass(String className) {
		return findByClass(driver, className);
	}

	protected List<WebElement> findByClass(SearchContext context, String className) {
		return context.findElements(By.className(className));
	}

	// FIND BY CSS QUERY
	
	protected WebElement findOneByCss(String query) {
		return findOneByCss(driver, query);
	}

	protected WebElement findOneByCss(SearchContext context, String query) {
		return context.findElement(By.cssSelector(query));
	}

	protected List<WebElement> findByCss(String query) {
		return findByCss(driver, query);
	}

	protected List<WebElement> findByCss(SearchContext context, String query) {
		return context.findElements(By.cssSelector(query));
	}

	// FIND BY XPATH
	
	protected List<WebElement> findByXPath(String xpath) {
		return findByXPath(driver, xpath);
	}
	
	protected List<WebElement> findByXPath(SearchContext context, String xpath) {
		return context.findElements(By.xpath(xpath));
	}

	// COUNT BY CLASS
	
	protected int countByClass(String className) {
		return countByClass(driver, className);
	}
	
	protected int countByClass(SearchContext context, String className) {
		List<WebElement> list = findByClass(context, className);
		if (list != null) {
			return list.size();
		}
		return 0;
	}

	// COUNT BY CSS QUERY

	protected int countByCss(String query) {
		return countByCss(driver, query);
	}
	
	protected int countByCss(SearchContext context, String query) {
		List<WebElement> list = findByCss(context, query);
		if (list != null) {
			return list.size();
		}
		return 0;
	}
	
	// WAIT LOOPS
	
	protected void waitFor(final WaitCondition condition) {
		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver d) {
				return Boolean.valueOf(condition.condition());
			}
		});
	}
	
	// INTERFACES
	
	protected interface WaitCondition {
		boolean condition();
	}

}
