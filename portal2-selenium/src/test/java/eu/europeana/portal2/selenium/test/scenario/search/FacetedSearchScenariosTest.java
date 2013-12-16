package eu.europeana.portal2.selenium.test.scenario.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import eu.europeana.portal2.selenium.Pages;
import eu.europeana.portal2.selenium.model.search.Facet;
import eu.europeana.portal2.selenium.model.search.FacetItem;
import eu.europeana.portal2.selenium.page.IndexPage;
import eu.europeana.portal2.selenium.page.SearchPage;
import eu.europeana.portal2.selenium.test.abstracts.TestSetup;
import eu.europeana.portal2.selenium.utils.PatternUtils;

public class FacetedSearchScenariosTest extends TestSetup {

	private final String RIGHTS = "RIGHTS";
	private final String RIGHTS_PREFIX = RIGHTS + ":";
	private final String RIGHTS_REGEX = RIGHTS_PREFIX + "(\".*\"|.*\\*)$";
	private final int ROWS = 24;
	
	@Test
	public void searchForParisTest() {
		System.out.println("testFacets");

		List<String> queries = Arrays.asList(new String[] { "query=*:*" });
		List<Pattern> patterns = PatternUtils.transformPatterns(queries);
		patterns.add(PatternUtils.createPattern("rows=" + ROWS));

		IndexPage indexPage = IndexPage.openPage(webDriver);
		indexPage.setSearchQuery("*:*");
		indexPage.clickSearch();

		final int FACET_TYPE = 2;
		final int FACET_LANGUAGE = 3;
		final int FACET_YEAR = 4;
		final int FACET_COUNTRY = 5;
		final int FACET_COPYRIGHT = 6;
		final int FACET_PROVIDER = 7;
		final int FACET_DATA_PROVIDER = 8;

		Map<Integer, List<String>> labels = new HashMap<Integer, List<String>>() {
			private static final long serialVersionUID = 1L;
			{
				put(FACET_TYPE, Arrays.asList(new String[] { "By media type", "TYPE" }));
				put(FACET_LANGUAGE, Arrays.asList(new String[] { "By language of description", "LANGUAGE" }));
				put(FACET_YEAR, Arrays.asList(new String[] { "By year", "YEAR" }));
				put(FACET_COUNTRY, Arrays.asList(new String[] { "By providing country", "COUNTRY" }));
				put(FACET_COPYRIGHT, Arrays.asList(new String[] { "By copyright", RIGHTS }));
				put(FACET_PROVIDER, Arrays.asList(new String[] { "By provider", "PROVIDER" }));
				put(FACET_DATA_PROVIDER, Arrays.asList(new String[] { "By data provider", "DATA_PROVIDER" }));
			}
		};

		SearchPage searchPage = SearchPage.checkPage(webDriver);

		List<Facet> facetLists = searchPage.getFacetLists();

		int i = 0;
		StringBuffer verificationErrors = new StringBuffer();

		for (Facet facet : facetLists) {

			i++;
			if (i == 1 || i == FACET_DATA_PROVIDER || i == 9) { // skip add keyword, (hidden) data provider and ugc
																// filter
				continue;
			}

			String facetLabel = labels.get(i).get(0);
			String type = labels.get(i).get(1);

			facet.click();

			assertTrue("click should open/close facet [" + i + "], visibility was " + facet.isListVisible()
					+ " instead of " + (i != FACET_TYPE), facet.isListVisible() == (i != FACET_TYPE));
			assertEquals(String.format("Facet label #%d should be %s", i, facetLabel), facet.label, facetLabel);

			for (FacetItem item : facet.getItems()) {

				// if ( item.id.equals("rights-info")) {
				// continue;
				// }

				// check rel attribute correct

				assertTrue("link is present.", item.hasLink());
				assertNotNull("@rel is mandatory.", item.getRel());
				assertEquals("@rel should be \"nofollow\"", "nofollow", item.getRel());

				// check tooltips present
				assertNotNull("Link should have title.", StringUtils.trimToNull(item.label));

				// check links include search
				assertNotNull("@href expected on facet item", item.link);

				/*
				 * WebWindow ww = new WebWindow(driver, link);
				 * 
				 * (new WebDriverWait(driver, 15)).until(new ExpectedCondition<Boolean>() { public Boolean
				 * apply(WebDriver d) { return d.getTitle().toLowerCase().endsWith(" - europeana - search results") &&
				 * d.findElement(By.cssSelector("div#search-results")).isDisplayed(); } });
				 * 
				 * WebElement results = driver.findElement(By.cssSelector("div#search-results"));
				 * 
				 * try{ Assert.assertFalse( results.getText().indexOf("Invalid query") > -1, ""); } catch(AssertionError
				 * e){ verificationErrors.append("Link should be a valid query (" + facetLabel + " / " + linkText +
				 * ") [" + link + "]\n"); }
				 * 
				 * ww.close();
				 */

				assertTrue("Link contains search URL: " + Pages.SEARCH, item.link.contains(Pages.SEARCH));

				for (Pattern pattern : patterns) {
					try {
						assertTrue("Link should contain " + pattern + " but it is " + item.link,
								pattern.matcher(item.link).find());
					} catch (AssertionError e) {
					}
				}

				assertTrue("Link contains rows", item.link.contains("&rows=" + ROWS));
				assertTrue("Link contains qf parameter", item.link.contains("&qf="));

				if (type.equals(RIGHTS)) {

					// rights facet checking

					String tempLink = item.link;
					while (tempLink.indexOf("&qf=" + RIGHTS_PREFIX) > -1) {
						int start = tempLink.indexOf("&qf=" + RIGHTS_PREFIX) + 4;
						int end = tempLink.indexOf("&", start);
						String qf = tempLink.substring(start, end).replace("%22", "\"");
						tempLink = tempLink.substring(end);
						assertTrue("Rights prefix have to start with " + RIGHTS_PREFIX, qf.startsWith(RIGHTS_PREFIX));
						assertTrue("Rights prefix have to match " + RIGHTS_REGEX, qf.matches(RIGHTS_REGEX));
					}
				} else {

					// non-rights facet checking
					String linkTitle = item.label;
					if (linkTitle.contains(" ")) {
						linkTitle = '"' + linkTitle + '"';
					}

					String encoded = "";
					try {
						encoded = PatternUtils.encodeFix(URLEncoder.encode(linkTitle, "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}

					assertTrue(
							String.format("Link contains FACET:VALUE as %s:%s but get %s", type, encoded, item.link),
							PatternUtils.encodeFix(item.link).contains("&qf=" + type + ":" + encoded)

					);
				}
			}

		}

		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			throw new AssertionError(verificationErrorString);
		}

		System.out.println("/testFacets");
	}

}
