package eu.europeana.portal2.selenium.page;

import org.openqa.selenium.WebDriver;

import eu.europeana.portal2.selenium.page.abstracts.Portal2Page;

public class IndexPage extends Portal2Page {
	
	public static final String PAGE = "http://www.europeana.eu/portal/"; 
	
	public static IndexPage openPage(WebDriver driver) {
		driver.get(PAGE);
		IndexPage page = new IndexPage(driver);
		return page;
	}
	
	private IndexPage(WebDriver driver) {
		super(driver);
	}
	
	public int countBlogItems() {
		// TODO
		return -1;
	}

}
