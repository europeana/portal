package eu.europeana.portal2.selenium.model.abstracts;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import eu.europeana.portal2.selenium.page.abstracts.PageUtils;

public abstract class ElementUtils extends PageUtils {

	final protected WebElement webElement;
	
	public ElementUtils(WebDriver webdriver, WebElement e) {
		super(webdriver);
		webElement = e;
	}
	
	public boolean isVisible() {
		return webElement.isDisplayed();
	}
	
}
