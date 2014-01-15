package eu.europeana.portal2.selenium.test.page.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import eu.europeana.portal2.selenium.Pages;
import eu.europeana.portal2.selenium.model.search.Facet;
import eu.europeana.portal2.selenium.model.search.FacetItem;
import eu.europeana.portal2.selenium.page.SearchPage;
import eu.europeana.portal2.selenium.test.PortalConfig;
import eu.europeana.portal2.selenium.test.abstracts.TestSetup;
import eu.europeana.portal2.selenium.utils.PatternUtils;

public class FacetValidationTest extends TestSetup {
	
	final int FACET_ADDMORE = 0;
	final int FACET_TYPE = 1;
	final int FACET_LANGUAGE = 2;
	final int FACET_YEAR = 3;
	final int FACET_COUNTRY = 4;
	final int FACET_CANIUSE = 5;
	final int FACET_COPYRIGHT = 6;
	final int FACET_PROVIDER = 7;
	final int FACET_DATA_PROVIDER = 8;
	
	private final String LANGUAGE = "LANGUAGE";
	private final String RIGHTS = "RIGHTS";
	
	private final String RIGHTS_PREFIX = RIGHTS + ":";
	private final String RIGHTS_REGEX = RIGHTS_PREFIX + "(\".*\"|.*\\*)$";

	
	Map<Integer, List<String>> labels = new HashMap<Integer, List<String>>() {
		private static final long serialVersionUID = 1L;
		{
			put(FACET_TYPE, Arrays.asList(new String[] { "By media type", "TYPE" }));
			put(FACET_LANGUAGE, Arrays.asList(new String[] { "By language of description", LANGUAGE }));
			put(FACET_YEAR, Arrays.asList(new String[] { "By year", "YEAR" }));
			put(FACET_COUNTRY, Arrays.asList(new String[] { "By providing country", "COUNTRY" }));
			put(FACET_COPYRIGHT, Arrays.asList(new String[] { "By copyright", RIGHTS }));
			put(FACET_PROVIDER, Arrays.asList(new String[] { "By provider", "PROVIDER" }));
			put(FACET_DATA_PROVIDER, Arrays.asList(new String[] { "By data provider", "DATA_PROVIDER" }));
		}
	};
	
	private SearchPage page;
	
	@Before
	@Override
	public void setupPage() {
		super.setupPage(); // create webDriver instance
		page = SearchPage.openPage(webDriver, "query=*:*");
	}
	
	@Test
	public void typeFacetTest() {
		Facet facet = page.getFacetLists().get(FACET_TYPE);
		assertTrue("Facet TYPE should be opened by default", facet.isListVisible());
		checkFacet(facet, FACET_TYPE);
	}
	
	@Test
	public void yearFacetTest() {
		checkFacet(FACET_YEAR);
	}
	
	@Test
	public void countryFacetTest() {
		checkFacet(FACET_COUNTRY);
	}
	
	@Test
	public void copyrightFacetTest() {
		checkFacet(FACET_COPYRIGHT);
	}
	
	@Test
	public void providerFacetTest() {
		checkFacet(FACET_PROVIDER);
	}
	
	@Test
	public void dataProviderFacetTest() {
		Facet provider = page.getFacetLists().get(FACET_PROVIDER);
		Facet facet = page.getFacetLists().get(FACET_DATA_PROVIDER);
		assertTrue("Facet DATA PROVIDER should be hidden by default", !facet.isVisible());
		provider.click();
		assertTrue("Facet DATA PROVIDER should be visible after clicking PROVIDER", facet.isVisible());
		checkFacet(FACET_DATA_PROVIDER);
	}
	
	private void checkFacet(int facetIndex) {
		Facet facet = page.getFacetLists().get(facetIndex);
		checkFacet(facet, facetIndex);
	}

	private void checkFacet(Facet facet, int facetIndex) {
		String facetLabel = labels.get(facetIndex).get(0);
		String type = labels.get(facetIndex).get(1);
		List<String> queries = Arrays.asList(new String[] { "query=*:*" });
		List<Pattern> patterns = PatternUtils.transformPatterns(queries);
		patterns.add(PatternUtils.createPattern("rows=" + PortalConfig.SEACH_COUNT_ROWS));

		facet.click();

		assertTrue("click should open/close facet [" + facetIndex + "], visibility was " + facet.isListVisible()
				+ " instead of " + (facetIndex != FACET_TYPE), facet.isListVisible() == (facetIndex != FACET_TYPE));
		assertEquals(String.format("Facet label #%d should be %s", facetIndex, facetLabel), facetLabel, facet.label);
		
		if (!facet.isListVisible()) {
			facet.click();
		}

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

			assertTrue("Link contains rows", item.link.contains("&rows=" + PortalConfig.SEACH_COUNT_ROWS));
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
			} else if (!type.equals(LANGUAGE)) {

				// non-rights facet checking
				String linkTitle = item.label;
				if (StringUtils.endsWith(linkTitle, "...")) {
					linkTitle = StringUtils.substringBefore(linkTitle, "...");
				} else
				if (linkTitle.contains(" ")) {
					linkTitle = '"' + linkTitle + '"';
				}

				try {
					assertTrue(
							String.format("Link contains FACET:VALUE as %s:%s but get %s", type, PatternUtils.encodeFix(URLEncoder.encode(linkTitle, "UTF-8")), item.link),
							StringUtils.containsIgnoreCase(PatternUtils.encodeFix(item.link), "&qf=" + type + ":" + PatternUtils.encodeFix(URLEncoder.encode(linkTitle, "UTF-8"))) ||
							StringUtils.containsIgnoreCase(PatternUtils.encodeFix(item.link), "&qf=" + type + ":" + PatternUtils.encodeFix(URLEncoder.encode('"' + linkTitle, "UTF-8")))
					);
				} catch (UnsupportedEncodingException e) {
					fail(e.getMessage());
				}
			}
		}
		
	}

}
