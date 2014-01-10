package eu.europeana.portal2.selenium.page;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;

import eu.europeana.portal2.selenium.Pages;
import eu.europeana.portal2.selenium.page.abstracts.Portal2Page;

public class IndexPage extends Portal2Page {

	public static IndexPage openPage(WebDriver driver) {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get(Pages.INDEX);
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
