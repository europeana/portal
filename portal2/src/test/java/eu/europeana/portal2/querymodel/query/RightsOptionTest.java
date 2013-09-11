package eu.europeana.portal2.querymodel.query;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RightsOptionTest {

	String portalUrl = "http://europeana.eu/portal";

	@Test
	/**
	 * Testing all methods on CC_ZERO item
	 */
	public void testCCZero() {
		assertEquals("icon-cczero", RightsOption.CC_ZERO.getRightsIcon());
		assertEquals(true, RightsOption.CC_ZERO.getShowExternalIcon());
		assertEquals("CC0", RightsOption.CC_ZERO.getRightsText());
		assertEquals("http://creativecommons.org/publicdomain/zero",
			RightsOption.CC_ZERO.getUrl());
		assertEquals(false, RightsOption.CC_ZERO.isRelativeUrl());
		assertEquals("http://creativecommons.org/publicdomain/zero",
			RightsOption.CC_ZERO.getRelativeUrl(portalUrl));
	}

	@Test
	public void testSafeValueByUrl() {
		assertEquals(RightsOption.CC_ZERO,
				RightsOption.safeValueByUrl("http://creativecommons.org/publicdomain/zero"));
		assertEquals(RightsOption.CC_BY,
				RightsOption.safeValueByUrl("http://creativecommons.org/licenses/by/"));
		assertEquals(RightsOption.CC_BY_SA,
				RightsOption.safeValueByUrl("http://creativecommons.org/licenses/by-sa/"));
		assertEquals(RightsOption.CC_BY_NC_SA,
				RightsOption.safeValueByUrl("http://creativecommons.org/licenses/by-nc-sa/"));
		assertEquals(RightsOption.CC_BY_ND,
				RightsOption.safeValueByUrl("http://creativecommons.org/licenses/by-nd/"));
		assertEquals(RightsOption.CC_BY_NC,
				RightsOption.safeValueByUrl("http://creativecommons.org/licenses/by-nc/"));
		assertEquals(RightsOption.CC_BY_NC_ND,
				RightsOption.safeValueByUrl("http://creativecommons.org/licenses/by-nc-nd/"));
		assertEquals(RightsOption.NOC,
				RightsOption.safeValueByUrl("http://creativecommons.org/publicdomain/mark/"));
		assertEquals(RightsOption.EU_RR_F,
				RightsOption.safeValueByUrl("http://www.europeana.eu/rights/rr-f/"));
		assertEquals(RightsOption.EU_RR_P,
				RightsOption.safeValueByUrl("http://www.europeana.eu/rights/rr-p/"));
		assertEquals(RightsOption.EU_RR_R,
				RightsOption.safeValueByUrl("http://www.europeana.eu/rights/rr-r/"));
		assertEquals(RightsOption.EU_ORPHAN,
				RightsOption.safeValueByUrl("http://www.europeana.eu/rights/test-orphan-work-test/"));
		assertEquals(RightsOption.EU_U,
				RightsOption.safeValueByUrl("http://www.europeana.eu/rights/unknown/"));
		assertEquals(RightsOption.OOC_NC,
				RightsOption.safeValueByUrl("http://www.europeana.eu/rights/out-of-copyright-non-commercial/"));
		assertEquals("Fake URL should match null", null, 
				RightsOption.safeValueByUrl("http://creativecommons.org/fake"));
	}

	@Test
	/**
	 * Testing whether Europeana copyrights has relative URLs
	 */
	public void testIsRelativeUrl() {
		assertEquals("Europeana copyright has relative URL", true, RightsOption.EU_RR_F.isRelativeUrl());
		assertEquals("Europeana copyright has relative URL", true, RightsOption.EU_RR_P.isRelativeUrl());
		assertEquals("Europeana copyright has relative URL", true, RightsOption.EU_RR_R.isRelativeUrl());
		assertEquals("Europeana copyright has relative URL", true, RightsOption.EU_ORPHAN.isRelativeUrl());
		assertEquals("Europeana copyright has relative URL", true, RightsOption.EU_U.isRelativeUrl());
		assertEquals("Europeana copyright has relative URL", true, RightsOption.OOC_NC.isRelativeUrl());
	}

	@Test
	public void testGetRightsIcon() {
		assertEquals("icon-cc icon-by", RightsOption.CC_BY.getRightsIcon());
		assertEquals("icon-cc icon-by icon-sa", RightsOption.CC_BY_SA.getRightsIcon());
	}

	@Test
	/**
	 * Testing the showExternalIcon() function
	 */
	public void testShowExternalIcon() {
		assertEquals(true, RightsOption.CC_ZERO.getShowExternalIcon());
		assertEquals(true, RightsOption.CC_BY_SA.getShowExternalIcon());
		assertEquals(false, RightsOption.EU_U.getShowExternalIcon());
		assertEquals(false, RightsOption.OOC_NC.getShowExternalIcon());
	}

	@Test
	/**
	 * Testing the getRightsText() function
	 */
	public void testGetRightsText() {
		assertEquals("CC0", RightsOption.CC_ZERO.getRightsText());
		assertEquals("CC BY-SA", RightsOption.CC_BY_SA.getRightsText());
		assertEquals("Unknown copyright status", RightsOption.EU_U.getRightsText());
		assertEquals("Out of copyright - non commercial reuse", RightsOption.OOC_NC.getRightsText());
	}

	@Test
	/**
	 * Testing the getUrl() function
	 */
	public void testGetUrl() {
		assertEquals("http://creativecommons.org/publicdomain/zero",
			RightsOption.CC_ZERO.getUrl());
		assertEquals("http://creativecommons.org/licenses/by-sa/",
				RightsOption.CC_BY_SA.getUrl());
		assertEquals("http://www.europeana.eu/rights/unknown/",
				RightsOption.EU_U.getUrl());
		assertEquals("http://www.europeana.eu/rights/out-of-copyright-non-commercial/",
				RightsOption.OOC_NC.getUrl());
	}

	@Test
	/**
	 * Testing the getRealtiveUrl() function
	 */
	public void testGetRelativeUrl() {
		assertEquals("http://creativecommons.org/publicdomain/zero",
			RightsOption.CC_ZERO.getRelativeUrl(portalUrl));
		assertEquals("http://creativecommons.org/licenses/by-sa/",
				RightsOption.CC_BY_SA.getRelativeUrl(portalUrl));
		assertEquals("http://europeana.eu/portal/rights/unknown.html",
				RightsOption.EU_U.getRelativeUrl(portalUrl));
		assertEquals("http://europeana.eu/portal/rights/out-of-copyright-non-commercial.html",
				RightsOption.OOC_NC.getRelativeUrl(portalUrl));
	}

}
