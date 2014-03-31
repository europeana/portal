package eu.europeana.portal2.web.presentation.semantic;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/portal2-test.xml"})
public class ElementTest {

	Namespace nsDc;
	Namespace nsDcTerms;
	Map<String, String> attributes1;
	Map<String, String> attributes2;
	Map<String, String> attributes3;

	@Before
	public void setUp() throws Exception {
		nsDc = new Namespace("dc", "http://purl.org/dc/elements/1.1/");
		nsDcTerms = new Namespace("dcterms", "http://purl.org/dc/terms/");

		attributes1 = new HashMap<String, String>();
		attributes1.put("type", "normal");

		attributes2 = new HashMap<String, String>();
		attributes1.put("type", "small");

		attributes3 = new HashMap<String, String>();
		attributes3.put("type", "normal");
		attributes3.put("color", "green");
	}

	@Test
	public void testConstructor() {
		Element element = new Element(nsDc, "title");
		assertNull(element.getAttributes());
		assertEquals(nsDc, element.getNamespace());
		assertEquals("title", element.getElementName());
		assertEquals("dc:title", element.getQualifiedName());
		assertEquals("http://purl.org/dc/elements/1.1/title", element.getFullQualifiedURI());
		assertEquals("dc", element.getPrefix());
		assertEquals("Element [namespace=Namespace [prefix=dc, uri=http://purl.org/dc/elements/1.1/], elementName=title, attributes=null]", element.toString());
		assertNotNull(element.hashCode());
	}

	@Test
	public void testCompare() {
		Element element1 = new Element(nsDc, "title");
		Element element2 = new Element(nsDc, "title");
		Element element3 = new Element(nsDc, "creator");

		assertEquals(element1, element2);
		assertNotSame(element1, element3);

		assertTrue(element1.equals(element1));
		assertFalse(element1.equals(null));
		assertFalse(element1.equals(new String("")));

		assertTrue(element1.equals(element2));
		assertTrue(element2.equals(element1));

		assertFalse(element1.equals(element3));
		assertFalse(element3.equals(element1));
		assertFalse(element2.equals(element3));
		assertFalse(element3.equals(element2));

		// attributes
		element2.setAttributes(attributes1);
		assertNotNull(element2.getAttributes());
		assertFalse(element1.equals(element2));
		assertFalse(element2.equals(element1));

		element1.setAttributes(attributes2);
		assertNotNull(element1.getAttributes());
		assertNotNull(element2.getAttributes());
		assertFalse(element1.equals(element2));
		assertFalse(element2.equals(element1));

		element1.setAttributes(null);
		element2.setAttributes(null);
		assertTrue(element1.equals(element2));

		// element name
		element1.setAttributes(attributes1);
		element2.setAttributes(attributes1);
		element2.setElementName(null);
		assertNotNull(element1.getElementName());
		assertNull(element2.getElementName());
		assertFalse(element1.equals(element2));
		assertFalse(element2.equals(element1));

		element1.setElementName(null);
		assertTrue(element1.equals(element2));
		assertTrue(element2.equals(element2));

		element1.setElementName("title");
		element2.setElementName("title");
		assertNotNull(element1.getElementName());
		assertNotNull(element2.getElementName());
		assertTrue(element1.equals(element2));

		// namespace
		element2.setNamespace(null);
		assertNull(element2.getNamespace());
		assertNotNull(element1.getNamespace());
		assertFalse(element1.equals(element2));
		assertFalse(element2.equals(element1));

		element1.setNamespace(null);
		assertNull(element1.getNamespace());
		assertNull(element2.getNamespace());
		assertTrue(element1.equals(element2));
		assertTrue(element2.equals(element1));

		element1.setNamespace(nsDc);
		element2.setNamespace(nsDcTerms);
		assertNotNull(element1.getNamespace());
		assertNotNull(element2.getNamespace());
		assertFalse(element1.equals(element2));
		assertFalse(element2.equals(element1));
	}

	@Test
	public void testSetters() {
		Element element = new Element(nsDc, "title");
		assertEquals(nsDc, element.getNamespace());
		element.setNamespace(nsDcTerms);
		assertEquals(nsDcTerms, element.getNamespace());

		assertEquals("title", element.getElementName());
		element.setElementName("creator");
		assertEquals("creator", element.getElementName());

		assertNull(element.getAttributes());
		element.setAttributes(attributes1);
		assertEquals(attributes1, element.getAttributes());
	}

	@Test
	public void testHash() {
		Element element = new Element(nsDc, "title");
		assertEquals(calculateHash(element), element.hashCode());
		element.setAttributes(attributes1);
		assertEquals(calculateHash(element), element.hashCode());
		element.setNamespace(null);
		assertEquals(calculateHash(element), element.hashCode());
		element.setElementName(null);
		assertEquals(calculateHash(element), element.hashCode());
	}
	
	private int calculateHash(Element element) {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((element.getAttributes() == null) ? 0 : element.getAttributes().hashCode());
		result = prime * result
				+ ((element.getElementName() == null) ? 0 : element.getElementName().hashCode());
		result = prime * result
				+ ((element.getNamespace() == null) ? 0 : element.getNamespace().hashCode());
		return result;
	}
}
