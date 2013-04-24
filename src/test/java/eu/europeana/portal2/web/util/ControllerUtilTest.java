package eu.europeana.portal2.web.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class ControllerUtilTest {

	@Test
	public void testClearSeeAlso() {
		String[] tests = new String[]{
				"Cranach, Lucas (der Altere) [Herstellung]", 
				"Portrat Lucas Cranach <der Altere>.",
				"Benedikt von Nursia, Abt von Monte Cassino und Grunder des Benediktinerordens, mogliche Attribute: Weihwedel, Buch, Kelch (manchmal zerbrochen), Rabe mit Brotlaib, Sieb (oder Tafel)",
				"Graduale I — Initiale M mit dem Heiligen Benedikt (?), Folio 34recto"
		};
		String[] expected = new String[]{
				"Cranach, Lucas", 
				"Portrat Lucas Cranach",
				"Benedikt von Nursia, Abt von Monte Cassino und Grunder des Benediktinerordens, mogliche Attribute: Weihwedel, Buch, Kelch (manchmal zerbrochen), Rabe mit Brotlaib, Sieb",
				"Graduale I — Initiale M mit dem Heiligen Benedikt (?), Folio 34recto"
		};
		for (int i=0, l=tests.length; i < l; i++) {
			assertEquals(expected[i], ControllerUtil.clearSeeAlso(tests[i]));
		}
	}

}
