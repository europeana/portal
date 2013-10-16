package eu.europeana.portal2.selenium.utils;

import java.io.File;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class PageUtils {
	
	protected WebDriver driver;
	
	public PageUtils(WebDriver driver) {
		this.driver = driver;
	}
	
	// PROTECTED METHODS FOR USE IN PAGE OBJECTS ONLY
	
	protected WebElement findById(String id) {
		return driver.findElement(By.id(id));
	}
	
	protected List<WebElement> findByClass(String className) {
		return driver.findElements(By.className(className));
	}
	
	protected int countByClass(String className) {
		List<WebElement> list = findByClass(className);
		if (list != null) {
			return list.size();
		}
		return 0;
	}

	protected WebElement findOneByCss(String query) {
		return driver.findElement(By.cssSelector(query));
	}
	
	protected List<WebElement> findByCss(String query) {
		return driver.findElements(By.cssSelector(query));
	}
	
	public void takeScreenshot(File file) {
		// TODO
	}

}
