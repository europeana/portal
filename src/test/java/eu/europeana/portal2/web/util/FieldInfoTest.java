package eu.europeana.portal2.web.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class FieldInfoTest {

	@Test
	public void test() {
		FieldInfo field = new FieldInfo("edm:ProvidedCHO", "providedCHOs", "ProvidedCHO");
		assertEquals("Schema name should be edm:ProvidedCHO", "edm:ProvidedCHO", field.getSchemaName());
		assertEquals("Property name should be providedCHOs", "providedCHOs", field.getPropertyName());
		assertEquals("Type name should be ProvidedCHO", "ProvidedCHO", field.getType());

		// testing element
		assertNotNull("Element should not be null", field.getElement());
		assertEquals("Qualified name should be edm:ProvidedCHO", "edm:ProvidedCHO", field.getElement().getQualifiedName());
		assertEquals("Prefix should be edm", "edm", field.getElement().getPrefix());
		assertEquals("Namespace URI should be http://www.europeana.eu/schemas/edm/", "http://www.europeana.eu/schemas/edm/", field.getElement().getNamespace().getUri());
		assertEquals("Full Qualified URI should be http://www.europeana.eu/schemas/edm/", 
				"http://www.europeana.eu/schemas/edm/ProvidedCHO", field.getElement().getFullQualifiedURI());
	}
}
