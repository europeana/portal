package eu.europeana.portal2.selenium.model.search;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import eu.europeana.portal2.selenium.model.abstracts.ElementUtils;

public class Facet extends ElementUtils {

	private WebElement title;
	private List<FacetItem> items = new ArrayList<FacetItem>();

	private String label;

	public Facet(WebDriver webdriver, WebElement e) {
		super(webdriver, e);
	}

	public void click() {
		final boolean isDisplaying = isListVisible();
		getTitle().click();
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
	
	private WebElement getTitle() {
		if (title == null) {
			title = findOneByCss(webElement, "h3 a");
		}
		return title;
	}
	
	public String getLabel() {
		if (label == null) {
			label = getTitle().getText();
		}
		return label;
	}

	public List<FacetItem> getItems() {
		if (items.isEmpty()) {
			for (WebElement e : findByCss(webElement, "ul li")) { 
				items.add(new FacetItem(driver, e));
			}
		}
		return items;
	}

}
