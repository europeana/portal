package eu.europeana.portal2.selenium.model.search;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import eu.europeana.portal2.selenium.page.abstracts.PageUtils;

public class FacetItem extends PageUtils {

	private WebElement webElement;
	private WebElement checkbox;
	private WebElement anchor;

	public String label;
	final public String id;
	final public String link;

	public FacetItem(WebDriver driver, WebElement w) {
		super(driver);
		webElement = w;
		anchor = findOneByCss(webElement, "h4 a"); 
		setLabel(anchor.getText().length() > 0 ? anchor.getText() : findOneByCss(anchor,"label")
				.getText());
		checkbox = findOneByCss(webElement, "h4 input");
		id = webElement.getAttribute("id");
		link = anchor == null ? null : anchor.getAttribute("href");
	}
	
	private void setLabel(String text) {
		label = StringUtils.trim(StringUtils.substringBeforeLast(text, "("));
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
