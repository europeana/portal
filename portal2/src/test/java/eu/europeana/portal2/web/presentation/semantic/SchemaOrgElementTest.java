package eu.europeana.portal2.web.presentation.semantic;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

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
public class SchemaOrgElementTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testConstructors() {
		SchemaOrgElement element;
		element = new SchemaOrgElement("dc:title");
		assertNotNull(element);
		assertNotNull(element.getElement());
		assertNull(element.getEdmElement());
		assertEquals("dc:title", element.getElement().getQualifiedName());
		assertEquals(0, element.getParents().size());

		element = new SchemaOrgElement("dc:title", new String[] {"dc:creator"});
		assertEquals("dc:title", element.getElement().getQualifiedName());
		assertEquals(1, element.getParents().size());
		assertEquals("dc:creator", element.getParents().get(0).getQualifiedName());

		element = new SchemaOrgElement("dc:title", new String[] {"dc:creator"}, "dcterms:coverage");
		assertEquals("dc:title", element.getElement().getQualifiedName());
		assertEquals(1, element.getParents().size());
		assertEquals("dc:creator", element.getParents().get(0).getQualifiedName());
		assertEquals("dcterms:coverage", element.getEdmElement().getQualifiedName());

		element = new SchemaOrgElement("dc:title", new String[] {"dc:creator"}, "dcterms:coverage", 
				"dcterms:spatial");
		assertEquals("dc:title", element.getElement().getQualifiedName());
		assertEquals(1, element.getParents().size());
		assertEquals("dc:creator", element.getParents().get(0).getQualifiedName());
		assertEquals("dcterms:coverage", element.getEdmElement().getQualifiedName());
		assertEquals("dcterms:spatial", element.getEdmParent().getQualifiedName());
	}

	@Test
	public void testSetElements() {
		SchemaOrgElement element = new SchemaOrgElement("dc:title");
		assertNotNull(element);
		assertNotNull(element.getElement());
		assertNull(element.getEdmElement());
		assertEquals("dc:title", element.getElement().getQualifiedName());

		element.setElement("dc:author");
		assertNotNull(element.getElement());
		assertEquals("dc:author", element.getElement().getQualifiedName());

		Element generalElement = ElementFactory.createElement("dcterms:coverage");
		assertNotNull(generalElement);
		element.setElement(generalElement);
		assertNotNull(element.getElement());
		assertEquals("dcterms:coverage", element.getElement().getQualifiedName());
	}

	@Test
	public void testSetParents() {
		SchemaOrgElement element = new SchemaOrgElement("dc:title");
		element.setParents(new String[] {"dc:creator"});
		assertEquals(1, element.getParents().size());
		assertEquals("dc:creator", element.getParents().get(0).getQualifiedName());

		element.setParents(new String[] {"dc:creator", ""});
		assertEquals(1, element.getParents().size());
		assertEquals("dc:creator", element.getParents().get(0).getQualifiedName());

		element.setParents(new String[] {"dc:creator", "", "fake"});
		assertEquals(1, element.getParents().size());
		assertEquals("dc:creator", element.getParents().get(0).getQualifiedName());

		List<Element> parents = new ArrayList<Element>();
		parents.add(ElementFactory.createElement("dc:creator"));
		element.setParents(parents);
		assertEquals(1, element.getParents().size());
		assertEquals("dc:creator", element.getParents().get(0).getQualifiedName());
	}

	@Test
	public void testEdmElement() {
		SchemaOrgElement element = new SchemaOrgElement("dc:title");
		assertNull(element.getEdmElement());

		Element edmElement = ElementFactory.createElement("dc:creator");
		element.setEdmElement(edmElement);
		assertNotNull(element.getEdmElement());
		assertEquals(edmElement, element.getEdmElement());
	}

	@Test
	public void testEdmParents() {
		SchemaOrgElement element = new SchemaOrgElement("dc:title");
		assertNull(element.getEdmParent());

		Element edmParent = ElementFactory.createElement("dc:creator");
		element.setEdmParent(edmParent);
		assertNotNull(element.getEdmParent());
		assertEquals(edmParent, element.getEdmParent());
	}

	@Test
	public void testIsSemanticUrl() {
		SchemaOrgElement element = new SchemaOrgElement("dc:title");
		assertFalse(element.isSemanticUrl());

		Element element2 = ElementFactory.createElement("schema:url");
		element.setElement(element2);
		assertTrue(element.isSemanticUrl());
	}

	@Test
	public void testToString() {
		SchemaOrgElement element;
		element = new SchemaOrgElement("dc:title");
		assertEquals("SchemaOrgElement ["
			+ "element=Element [namespace=Namespace [prefix=dc, uri=http://purl.org/dc/elements/1.1/], elementName=title, attributes=null]"
			+ ", parents=[]"
			+ ", edmElement=null"
			+ ", edmParent=null]",
			element.toString());

		element = new SchemaOrgElement("dc:title", new String[] {"dc:creator"}, "dcterms:coverage", 
				"dcterms:spatial");
		assertEquals("SchemaOrgElement ["
			+ "element=Element [namespace=Namespace [prefix=dc, uri=http://purl.org/dc/elements/1.1/], elementName=title, attributes=null]"
			+ ", parents=[Element [namespace=Namespace [prefix=dc, uri=http://purl.org/dc/elements/1.1/], elementName=creator, attributes=null]]"
			+ ", edmElement=Element [namespace=Namespace [prefix=dcterms, uri=http://purl.org/dc/terms/], elementName=coverage, attributes=null]"
			+ ", edmParent=Element [namespace=Namespace [prefix=dcterms, uri=http://purl.org/dc/terms/], elementName=spatial, attributes=null]]", 
			element.toString());
	}

	@Test
	public void testHashCode() {
		SchemaOrgElement element;
		element = new SchemaOrgElement("dc:title");
		assertEquals(calculateHashCode(element), element.hashCode());

		element.setParents(new String[] {"dc:creator"});
		assertEquals(calculateHashCode(element), element.hashCode());

		element.setElement("");
		assertEquals(calculateHashCode(element), element.hashCode());
	}

	private int calculateHashCode(SchemaOrgElement element) {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((element.getElement() == null) ? 0 : element.getElement().hashCode());
		result = prime * result + ((element.getParents() == null) ? 0 : element.getParents().hashCode());
		return result;
	}

}
