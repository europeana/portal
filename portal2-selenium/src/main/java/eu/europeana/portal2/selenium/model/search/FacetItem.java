package eu.europeana.portal2.selenium.model.search;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import eu.europeana.portal2.selenium.page.abstracts.PageUtils;

public class FacetItem extends PageUtils {

	private WebElement webElement;
	private WebElement checkbox;
	private WebElement anchor;

	private String label;
	private String id;
	private String link;

	public FacetItem(WebDriver driver, WebElement w) {
		super(driver);
		webElement = w;
	}

	public String getLabel() {
		if (label == null) {
			String text = getAnchor().getText().length() > 0 ? getAnchor().getText() : findOneByCss(getAnchor(),"label")
					.getText();
			label = StringUtils.trim(StringUtils.substringBeforeLast(text, "("));
		}
		return label;
	}

	public void click() {
		webElement.click();
	}

	public void clickCheckbox() {
		if (checkbox == null) {
			checkbox = findOneByCss(webElement, "h4 input");
		}
		checkbox.click();
	}
	
	public String getLink() {
		if (link == null) {
			link = getAnchor() == null ? null : getAnchor().getAttribute("href");
		}
		return link;
	}
	
	public String getId() {
		if (id == null) {
			id = webElement.getAttribute("id");
		}
		return id;
	}

	public boolean hasLink() {
		return getAnchor() != null;
	}

	public String getRel() {
		return getAnchor() == null ? null : getAnchor().getAttribute("rel");
	}

	private WebElement getAnchor() {
		if (anchor == null) {
			anchor = findOneByCss(webElement, "h4 a"); 
		}
		return anchor;
	}
}
