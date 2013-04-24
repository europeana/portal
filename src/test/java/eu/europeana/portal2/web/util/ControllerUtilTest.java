package eu.europeana.portal2.web.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class ControllerUtilTest {

	@Test
	public void testClearSeeAlso() {
		String test = "Cranach, Lucas (der Altere) [Herstellung]";
		String expected = "Cranach, Lucas";
		assertEquals(expected, ControllerUtil.clearSeeAlso(test));
	}

}
