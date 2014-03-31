package eu.europeana.portal2.web.presentation.semantic;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class NamespaceResolverTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testExistingNamespaces() {
		Element element;
		element = ElementFactory.createElement("dc:title");
		assertEquals("http://purl.org/dc/elements/1.1/", element.getNamespace().getUri());
		assertEquals("dc", element.getNamespace().getPrefix());

		element = ElementFactory.createElement("dcterms:title");
		assertEquals("http://purl.org/dc/terms/", element.getNamespace().getUri());
		assertEquals("dcterms", element.getNamespace().getPrefix());

		element = ElementFactory.createElement("edm:title");
		assertEquals("http://www.europeana.eu/schemas/edm/", element.getNamespace().getUri());
		assertEquals("edm", element.getNamespace().getPrefix());

		element = ElementFactory.createElement("foaf:title");
		assertEquals("http://xmlns.com/foaf/0.1", element.getNamespace().getUri());
		assertEquals("foaf", element.getNamespace().getPrefix());

		element = ElementFactory.createElement("ore:title");
		assertEquals("http://www.openarchives.org/ore/terms/", element.getNamespace().getUri());
		assertEquals("ore", element.getNamespace().getPrefix());

		element = ElementFactory.createElement("owl:title");
		assertEquals("http://www.w3.org/2002/07/owl#", element.getNamespace().getUri());
		assertEquals("owl", element.getNamespace().getPrefix());

		element = ElementFactory.createElement("rdaGr2:title");
		assertEquals("http://rdvocab.info/ElementsGr2", element.getNamespace().getUri());
		assertEquals("rdaGr2", element.getNamespace().getPrefix());

		element = ElementFactory.createElement("rdf:title");
		assertEquals("http://www.w3.org/1999/02/22-rdf-syntax-ns#", element.getNamespace().getUri());
		assertEquals("rdf", element.getNamespace().getPrefix());

		element = ElementFactory.createElement("rdfs:title");
		assertEquals("http://www.w3.org/2000/01/rdf-schema#", element.getNamespace().getUri());
		assertEquals("rdfs", element.getNamespace().getPrefix());

		element = ElementFactory.createElement("skos:title");
		assertEquals("http://www.w3.org/2004/02/skos/core#", element.getNamespace().getUri());
		assertEquals("skos", element.getNamespace().getPrefix());

		element = ElementFactory.createElement("rdf:title");
		assertEquals("http://www.w3.org/1999/02/22-rdf-syntax-ns#", element.getNamespace().getUri());
		assertEquals("rdf", element.getNamespace().getPrefix());

		element = ElementFactory.createElement("wgs84_pos:title");
		assertEquals("http://www.w3.org/2003/01/geo/wgs84_pos#", element.getNamespace().getUri());
		assertEquals("wgs84_pos", element.getNamespace().getPrefix());

		element = ElementFactory.createElement("schema:title");
		assertEquals("http://schema.org/", element.getNamespace().getUri());
		assertEquals("schema", element.getNamespace().getPrefix());
	}

}
