package eu.europeana.portal2.web.presentation.semantic;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import eu.europeana.portal2.web.presentation.semantic.Namespace;

public class NamespaceTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testConstructor() {
		Namespace ns;
		ns = new Namespace("dc", "http://purl.org/dc/elements/1.1/");
		assertEquals("dc", ns.getPrefix());
		assertEquals("http://purl.org/dc/elements/1.1/", ns.getUri());
	}

	@Test
	public void testSetPrefix() {
		Namespace ns;
		ns = new Namespace("dc", "http://purl.org/dc/elements/1.1/");
		assertEquals("dc", ns.getPrefix());
		ns.setPrefix("dcterms");
		assertEquals("dcterms", ns.getPrefix());
		assertEquals("http://purl.org/dc/elements/1.1/", ns.getUri());
	}

	@Test
	public void testUri() {
		Namespace ns;
		ns = new Namespace("dc", "http://purl.org/dc/elements/1.1/");
		assertEquals("http://purl.org/dc/elements/1.1/", ns.getUri());
		ns.setUri("http://purl.org/dc/elements/1.1#");
		assertEquals("http://purl.org/dc/elements/1.1#", ns.getUri());
	}

	@Test
	public void testToString() {
		Namespace ns;
		ns = new Namespace("dc", "http://purl.org/dc/elements/1.1/");
		String expected = String.format("Namespace [prefix=%s, uri=%s]", "dc", "http://purl.org/dc/elements/1.1/");
		assertEquals(expected, ns.toString());
	}
}
