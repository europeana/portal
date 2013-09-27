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
	
	public void setSearchQuery(String value) {
		getSearchInput().sendKeys(value);
	}
	
	public void doSearchSubmit() {
		getSearchInput().submit();
	}
	
	protected WebElement getSearchInput() {
		return findById(ID_SEARCH_INPUT_TEXT);
	}
	

}
