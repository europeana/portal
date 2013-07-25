package eu.europeana.search;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import eu.europeana.Config;
import eu.europeana.EuropeanaTest;
import eu.europeana.Utils;
import eu.europeana.WebWindow;

public class FacetTest extends EuropeanaTest{

	private final String	RIGHTS = "RIGHTS";
	private final String	RIGHTS_PREFIX = RIGHTS + ":";
	private final String	RIGHTS_REGEX = RIGHTS_PREFIX + "(\".*\"|.*\\*)$";
	private final int		ROWS = 24;
	
	//private ScreenshotHelper screenshotHelper;

	public FacetTest(){
		this.realData = true;
	}

	
	@Test(description = "Search *:* and check that all facets open and that all their links work.")
	public void runFacetsTest() throws IndexOutOfBoundsException, Exception {

		System.out.println("testFacets");

		
		List<String> queries = Arrays.asList(new String[]{"query=*:*"});
		List<Pattern> patterns = Utils.transformPatterns(queries);
		patterns.add(Utils.createPattern("rows=" + ROWS));

		Utils.searchFor(driver, "*:*");
		
		Map<Integer, List<String>> labels = new HashMap<Integer, List<String>>(){
			private static final long serialVersionUID = 1L;
			{
				put(2, Arrays.asList(new String[]{"By media type",				"TYPE"}));
				put(3, Arrays.asList(new String[]{"By language of description",	"LANGUAGE"}));
				put(4, Arrays.asList(new String[]{"By year",					"YEAR"}));
				put(5, Arrays.asList(new String[]{"By providing country",		"COUNTRY"}));
				put(6, Arrays.asList(new String[]{"By copyright",				RIGHTS}));
				put(7, Arrays.asList(new String[]{"By provider",				"PROVIDER"}));
				put(8, Arrays.asList(new String[]{"By data provider",			"DATA_PROVIDER"}));
			}
		};

		List<WebElement> facetLists = driver.findElements(By.xpath("//ul[@id=\"filter-search\"]/li"));
		
		int i = 0;
		StringBuffer verificationErrors = new StringBuffer();
		
		for (WebElement facetList : facetLists) {
			
			i++;
			if (i == 1 || i == 8 || i == 9) {	// skip add keyword, (hidden) data provider and ugc filter
				continue;
			}
			
			String facetLabel	= labels.get(i).get(0);
			String type			= labels.get(i).get(1);
			
			WebElement title	= facetList.findElement(By.cssSelector("h3 a"));
				
			title.click();
			
			Utils.setWebElement(facetList);
			
			
			if(i==2){ // TODO set a static int
				(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						WebElement options	= Utils.getWebElement().findElement(By.cssSelector("ul"));
						return !options.isDisplayed();
					}
				});				
			}
			else{
				(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						WebElement options	= Utils.getWebElement().findElement(By.cssSelector("ul"));
						return options.isDisplayed();
					}
				});
			}
			
			WebElement options = facetList.findElement(By.cssSelector("ul"));
			
			Assert.assertTrue(  options.isDisplayed() == (i!=2), "click should open facet [" + i + "], visibility was " + options.isDisplayed() + " instead of " + (i!=2) );
			Assert.assertEquals(title.getText(), facetLabel, String.format("Facet label #%d should be %s", i, facetLabel));
			List<WebElement> lis = facetList.findElements(By.cssSelector("ul li"));

			
			for (WebElement li : lis) {
				
				if (li.getAttribute("id").equals("rights-info")) {
					continue;
				}

				WebElement a = li.findElement(By.cssSelector("h4 a"));
				
				// check rel attribute correct
				
				Assert.assertNotNull(a, "link is present.");
				Assert.assertNotNull(a.getAttribute("rel"), "@rel is mandatory.");
				Assert.assertEquals(a.getAttribute("rel"), "nofollow", "@rel=nofollow.");

				// check tooltips present
				
				String linkTitle = a.getAttribute("title").replace("\"", "\\\"");
				Assert.assertNotNull(linkTitle, "Link has title.");

				// check links include search
				
				String link		= a.getAttribute("href");
				String linkText	= a.getText().length() > 0 ? a.getText() : a.findElement(By.cssSelector("label")).getText();
				Assert.assertNotNull(link, "@href");
				
				// fake an invalid query
				/*
				if(count%4==0){
					String link1 = link.substring(0, link.indexOf("?"));
					String link2 = link.indexOf("&") > -1 ? link.substring(link.indexOf("&"), link.length()) : "";
					link1 += "?query=paris:paris" + link2;
					link = link1;					
				}
				*/
				
				
				WebWindow ww = new WebWindow(driver, link);
				
				(new WebDriverWait(driver, 15)).until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return d.getTitle().toLowerCase().endsWith(" - europeana - search results") && d.findElement(By.cssSelector("div#search-results")).isDisplayed();
					}
				});
				
				WebElement results = driver.findElement(By.cssSelector("div#search-results"));
				
				try{
					Assert.assertFalse( results.getText().indexOf("Invalid query") > -1, "");					
				}
				catch(AssertionError e){
					verificationErrors.append("Link should be a valid query (" + facetLabel + " / " + linkText + ") [" + link + "]\n");
				}

				ww.close();

				Assert.assertTrue(link.contains(Config.searchUrl), "Link contains search URL: " + Config.searchUrl);
				
				for (Pattern pattern : patterns) {
					try{
						Assert.assertTrue(pattern.matcher(link).find(), "Link should contain " + pattern + " but it is " + link);
					}
					catch(AssertionError e){}
				}

				
				Assert.assertTrue(link.contains("&rows=" + ROWS), "Link contains rows");
				Assert.assertTrue(link.contains("&qf="), "Link contains qf parameter");
				
				
				if (type.equals(RIGHTS)) {
					
					// rights facet checking
					
					String tempLink = link;
					while (tempLink.indexOf("&qf=" + RIGHTS_PREFIX) > -1) {
						int start = tempLink.indexOf("&qf=" + RIGHTS_PREFIX) + 4;
						int end = tempLink.indexOf("&", start);
						String qf = tempLink.substring(start, end).replace("%22", "\"");
						tempLink = tempLink.substring(end);
						Assert.assertTrue(qf.startsWith(RIGHTS_PREFIX), "Rights prefix have to start with " + RIGHTS_PREFIX);
						Assert.assertTrue(qf.matches(RIGHTS_REGEX), "Rights prefix have to match " + RIGHTS_REGEX);
					}
				}
				else{
					
					// non-rights facet checking
					
					if (linkTitle.contains(" ")) {
						linkTitle = '"' + linkTitle + '"';
					}
					
					String encoded = "";
					try {
						encoded = Utils.encodeFix(URLEncoder.encode(linkTitle, "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					// System.out.println("value: " + value);

					Assert.assertTrue(Utils.encodeFix(link).contains("&qf=" + type + ":" + encoded), 
						String.format("Link contains FACET:VALUE as %s:%s but get %s", type, encoded, link)
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
