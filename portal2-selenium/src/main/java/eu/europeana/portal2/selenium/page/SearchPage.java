package eu.europeana.portal2.selenium.page;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;

import eu.europeana.portal2.selenium.page.abstracts.Portal2Page;

public class SearchPage extends Portal2Page {

	public static final String PAGE = IndexPage.PAGE + "search.html"; 
	
	public static SearchPage openPage(WebDriver driver, String paramsString) {
		driver.get(PAGE + "?" + paramsString);
		SearchPage page = new SearchPage(driver);
		return page;
	}
	
	public static SearchPage checkPage(WebDriver driver) {
		if (StringUtils.startsWithIgnoreCase(driver.getCurrentUrl(), PAGE)) {
			SearchPage page = new SearchPage(driver);
			return page;
		}
		return null;
	}
	
	
	private SearchPage(WebDriver driver) {
		super(driver);
	}
	
}
