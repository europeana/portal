package eu.europeana.portal2.selenium.page;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import eu.europeana.portal2.selenium.Pages;
import eu.europeana.portal2.selenium.model.search.Facet;
import eu.europeana.portal2.selenium.page.abstracts.Portal2Page;
import eu.europeana.portal2.selenium.utils.PatternUtils;

public class SearchPage extends Portal2Page {

	public static final String CSS_SELECTOR_RESULT_COUNT = ".search-results-navigation .count";
	public static final String XPATH_SELECTOR_FACET_LIST = "//ul[@id=\"filter-search\"]/li";

	// constructors

	public static SearchPage openPage(WebDriver driver, String paramsString) {
		driver.get(Pages.SEARCH + "?" + paramsString);
		SearchPage page = new SearchPage(driver);
		return page;
	}

	public static SearchPage checkPage(WebDriver driver) {
		if (StringUtils.startsWithIgnoreCase(driver.getCurrentUrl(), Pages.SEARCH)) {
			SearchPage page = new SearchPage(driver);
			return page;
		}
		return null;
	}

	private SearchPage(WebDriver driver) {
		super(driver);
	}

	// counters

	public int countSearchResults() {
		return countByCss("div#items div.li");
	}

	public int countBreadCrumbs() {
		return countByCss("#breadcrumb li");
	}

	// getters

	public String getPaginationString() {
		return PatternUtils.normaliseWhitespace(findOneByCss(CSS_SELECTOR_RESULT_COUNT).getText());
	}

	public List<Facet> getFacetLists() {
		List<WebElement> elements = findByXPath(XPATH_SELECTOR_FACET_LIST);
		List<Facet> results = new ArrayList<Facet>();

		for (WebElement e : elements) {
			results.add(new Facet(driver, e));
		}

		return results;
	}

}
