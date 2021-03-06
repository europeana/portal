package eu.europeana.portal2.selenium.page;

import org.openqa.selenium.WebDriver;

import eu.europeana.portal2.selenium.Pages;
import eu.europeana.portal2.selenium.page.abstracts.Portal2Page;

public class IndexPage extends Portal2Page {

	public static IndexPage openPage(WebDriver driver) {
		driver.get(Pages.INDEX);
		IndexPage page = new IndexPage(driver);
		return page;
	}

	private IndexPage(WebDriver driver) {
		super(driver);
		waitFor(new WaitCondition() {
			@Override
			public boolean condition() {
				return findByCss("#section-blog .six").size() > 0;
			}
		});
	}

	public int countBlogItems() {
		return findByCss("#section-blog .six").size();
	}

	public int countLocaleItems() {
		return findByClass("lang").size();
	}

}
