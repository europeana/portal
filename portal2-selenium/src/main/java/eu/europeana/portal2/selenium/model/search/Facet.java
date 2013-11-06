package eu.europeana.portal2.selenium.model.search;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Facet {

	final private WebDriver webDriver;
	final private WebElement webElement;
	private WebElement title;
	private List<FacetItem> items = new ArrayList<FacetItem>();

	final public String label;

	public Facet(WebDriver webdriver, WebElement e) {
		webDriver = webdriver;
		webElement = e;
		title = e.findElement(By.cssSelector("h3 a"));
		label = title.getText();
	}

	public void click() {
		final boolean isDisplaying = isListVisible();
		title.click();
		(new WebDriverWait(webDriver, 10)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return isListVisible() != isDisplaying;
			}
		});
	}

	public boolean isListVisible() {
		return webElement.findElement(By.cssSelector("ul")).isDisplayed();
	}

	public List<FacetItem> getItems() {
		if (items.isEmpty()) {
			for (WebElement e : webElement.findElements(By.cssSelector("ul li"))) {
				items.add(new FacetItem(e));
			}
		}
		return items;
	}

}
