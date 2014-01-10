package eu.europeana.portal2.selenium.model.search;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import eu.europeana.portal2.selenium.page.abstracts.PageUtils;

public class Facet extends PageUtils {

	final private WebElement webElement;
	private WebElement title;
	private List<FacetItem> items = new ArrayList<FacetItem>();

	final public String label;

	public Facet(WebDriver webdriver, WebElement e) {
		super(webdriver);
		webElement = e;
		title = findOneByCss(webElement, "h3 a");
		label = title.getText();
	}

	public void click() {
		final boolean isDisplaying = isListVisible();
		title.click();
		waitFor(new WaitCondition() {
			@Override
			public boolean condition() {
				return isListVisible() != isDisplaying;
			}
		});
	}

	public boolean isListVisible() {
		return findOneByCss(webElement, "ul").isDisplayed();
	}

	public List<FacetItem> getItems() {
		if (items.isEmpty()) {
			for (WebElement e : findByCss(webElement, "ul li")) { 
				items.add(new FacetItem(e));
			}
		}
		return items;
	}

}
