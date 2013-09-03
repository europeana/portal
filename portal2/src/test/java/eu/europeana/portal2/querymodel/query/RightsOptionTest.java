package eu.europeana.portal2.querymodel.query;

import static org.junit.Assert.*;

import org.junit.Test;

public class RightsOptionTest {

	@Test
	public void test() {
		String portalUrl = "http://localhost:8080/portal";

		assertEquals("icon-cczero", RightsOption.CC_ZERO.getRightsIcon());
		assertEquals(true, RightsOption.CC_ZERO.getShowExternalIcon());
		assertEquals("CC0", RightsOption.CC_ZERO.getRightsText());
		assertEquals("http://creativecommons.org/publicdomain/zero",
			RightsOption.CC_ZERO.getUrl());
		assertEquals(false, RightsOption.CC_ZERO.isRelativeUrl());
		assertEquals("http://creativecommons.org/publicdomain/zero",
			RightsOption.CC_ZERO.getRelativeUrl(portalUrl));

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

		// Testing whether Europeana copyrights has relative URLs
		assertEquals("Europeana copyright has relative URL", true, RightsOption.EU_RR_F.isRelativeUrl());
		assertEquals("Europeana copyright has relative URL", true, RightsOption.EU_RR_P.isRelativeUrl());
		assertEquals("Europeana copyright has relative URL", true, RightsOption.EU_RR_R.isRelativeUrl());
		assertEquals("Europeana copyright has relative URL", true, RightsOption.EU_ORPHAN.isRelativeUrl());
		assertEquals("Europeana copyright has relative URL", true, RightsOption.EU_U.isRelativeUrl());
		assertEquals("Europeana copyright has relative URL", true, RightsOption.OOC_NC.isRelativeUrl());

		assertEquals("icon-cc icon-by", RightsOption.CC_BY.getRightsIcon());
	}

}
