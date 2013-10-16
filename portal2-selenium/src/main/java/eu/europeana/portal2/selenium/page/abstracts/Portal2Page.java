package eu.europeana.portal2.selenium.page.abstracts;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import eu.europeana.portal2.selenium.utils.PageUtils;

public abstract class Portal2Page extends PageUtils {
	
	public static final String ID_SEARCH_MENU_DIV = "search-menu";
	public static final String ID_SEARCH_INPUT_TEXT = "query-input";
	
	public Portal2Page(WebDriver driver) {
		super(driver);
	}
	
	// click actions
	
	public void clickSearch() {
		getSearchInput().submit();
	}
	
	public void clickRefine() {
		findById("refine-search").click();
	}
	
	// setters
	
	public void setSearchQuery(String value) {
		getSearchInput().sendKeys(value);
	}
	
	// getters
	
	public String getPageTitle() {
		return driver.getTitle();
	}
	
	public String getPageUrl() {
		return driver.getCurrentUrl();
	}
	
	// internal
	
	protected WebElement getSearchInput() {
		return findById(ID_SEARCH_INPUT_TEXT);
	}
	

}
