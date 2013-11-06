package eu.europeana.portal2.selenium.model.search;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class FacetItem {

	private WebElement webElement;
	private WebElement checkbox;
	private WebElement anchor;

	final public String label;
	final public String id;
	final public String link;

	public FacetItem(WebElement w) {
		webElement = w;
		anchor = webElement.findElement(By.cssSelector("h4 a"));
		label = anchor.getText().length() > 0 ? anchor.getText() : anchor.findElement(By.cssSelector("label"))
				.getText();
		checkbox = webElement.findElement(By.name("input"));
		id = webElement.getAttribute("id");
		link = anchor == null ? null : anchor.getAttribute("href");
	}

	public void click() {
		webElement.click();
	}

	public void clickCheckbox() {
		checkbox.click();
	}

	public boolean hasLink() {
		return anchor != null;
	}

	public String getRel() {
		return anchor == null ? null : anchor.getAttribute("rel");
	}

}
