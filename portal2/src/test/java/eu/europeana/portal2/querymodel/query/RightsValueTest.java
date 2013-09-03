package eu.europeana.portal2.querymodel.query;

import static org.junit.Assert.*;

import org.junit.Test;

import eu.europeana.portal2.web.presentation.model.data.submodel.RightsValue;

public class RightsValueTest {

	@Test
	public void test() {
		String edmRight = "http://www.europeana.eu/rights/out-of-copyright-non-commercial/";
		String portalUrl = "http://localhost:8080/portal";

		RightsValue rightsValue = RightsValue.safeValueByUrl(edmRight, portalUrl);
		assertEquals("/rights/out-of-copyright-non-commercial/", rightsValue.getRightsUrl());
		assertEquals(edmRight, rightsValue.getUrl());
		assertEquals("Out of copyright - non commercial reuse", rightsValue.getRightsText());
		assertEquals("icon-publicdomain icon-nc", rightsValue.getRightsIcon());
		assertEquals(false, rightsValue.getRightsShowExternalIcon());
		assertEquals("http://localhost:8080/portal/rights/out-of-copyright-non-commercial.html",
				rightsValue.getRelativeUrl());
		assertEquals(false, rightsValue.isNoc());
	}
}
