package eu.europeana.portal2.querymodel.query;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import eu.europeana.portal2.web.presentation.model.data.submodel.RightsValue;

/**
 * Testing RightsValue objects
 */
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/portal2-test.xml"})
@Ignore // disabled as portal project is deprecated
public class RightsValueTest {

	String portalUrl = "http://europeana.eu/portal";
	String cczeroUrl = "http://creativecommons.org/publicdomain/zero";
	String orphanUrl = "http://www.europeana.eu/rights/test-orphan-work-test/";
	String oocUrl = "http://www.europeana.eu/rights/out-of-copyright-non-commercial/";
	RightsValue cczero;
	RightsValue orphan;
	RightsValue ooc;

	@Before
	/**
	 * Setting up the RightsValue objects
	 */
	public void setup() {
		cczero = RightsValue.safeValueByUrl(cczeroUrl, portalUrl, null);
		orphan = RightsValue.safeValueByUrl(orphanUrl, portalUrl, null);
		ooc = RightsValue.safeValueByUrl(oocUrl, portalUrl, null);
	}

	@Test
	/**
	 * Testing all methods of the out of copyright type
	 */
	public void testOutOfCopyright() {
		assertEquals(oocUrl, ooc.getUrl());
		assertEquals("/rights/out-of-copyright-non-commercial/", ooc.getRightsUrl());
		assertEquals("Out of copyright - non commercial re-use", ooc.getRightsText());
		assertEquals("icon-publicdomain icon-nceu", ooc.getRightsIcon());
		assertEquals(false, ooc.getRightsShowExternalIcon());
		assertEquals("http://europeana.eu/portal/rights/out-of-copyright-non-commercial.html",
				ooc.getRelativeUrl());
		assertEquals(false, ooc.isNoc());
	}

	@Test
	/**
	 * Testing the getUrl() method
	 */
	public void testGetUrl() {
		assertEquals(cczeroUrl, cczero.getUrl());
		assertEquals(orphanUrl, orphan.getUrl());
		assertEquals(oocUrl, ooc.getUrl());
	}

	@Test
	/**
	 * Testing the getRightsUrl() method
	 */
	public void testGetRightsUrl() {
		assertEquals(cczeroUrl, cczero.getRightsUrl());
		assertEquals("/rights/test-orphan-work-test/", orphan.getRightsUrl());
		assertEquals("/rights/out-of-copyright-non-commercial/", ooc.getRightsUrl());
	}

	@Test
	/**
	 * Testing the getRightsText() method
	 */
	public void testGetRightsText() {
		assertEquals("CC0", cczero.getRightsText());
		assertEquals("Orphan Work", orphan.getRightsText());
		assertEquals("Out of copyright - non commercial re-use", ooc.getRightsText());
	}

	@Test
	/**
	 * Testing the getRightsIcon() method
	 */
	public void testGetRightsIcon() {
		assertEquals("icon-cczero", cczero.getRightsIcon());
		assertEquals("icon-unknown", orphan.getRightsIcon());
		assertEquals("icon-publicdomain icon-nceu", ooc.getRightsIcon());
	}

	@Test
	/**
	 * Testing the getRightsShowExternalIcon() method
	 */
	public void testGetRightsShowExternalIcon() {
		assertEquals(true, cczero.getRightsShowExternalIcon());
		assertEquals(false, orphan.getRightsShowExternalIcon());
		assertEquals(false, ooc.getRightsShowExternalIcon());
	}

	@Test
	/**
	 * Testing the getRelativeUrl() method
	 */
	public void testGetRelativeUrl() {
		assertEquals("http://creativecommons.org/publicdomain/zero", cczero.getRelativeUrl());
		assertEquals("http://europeana.eu/portal/rights/test-orphan-work-test.html", orphan.getRelativeUrl());
		assertEquals("http://europeana.eu/portal/rights/out-of-copyright-non-commercial.html", ooc.getRelativeUrl());
	}

	@Test
	/**
	 * Testing the isNoc() method
	 */
	public void testIsNoc() {
		assertEquals(true, cczero.isNoc());
		assertEquals(false, orphan.isNoc());
		assertEquals(false, ooc.isNoc());
	}
}
