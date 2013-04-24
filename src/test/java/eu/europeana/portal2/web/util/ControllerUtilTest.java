package eu.europeana.portal2.web.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class ControllerUtilTest {

	@Test
	public void testClearSeeAlso() {
		String[] tests = new String[]{"Cranach, Lucas (der Altere) [Herstellung]", "Portrat Lucas Cranach <der Ältere>."};
		String[] expected = new String[]{"Cranach, Lucas", "Portrat Lucas Cranach"};
		for (int i=0, l=tests.length; i < l; i++) {
			assertEquals(expected[i], ControllerUtil.clearSeeAlso(tests[i]));
		}
	}

}
