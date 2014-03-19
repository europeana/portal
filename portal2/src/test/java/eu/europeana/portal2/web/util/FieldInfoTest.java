package eu.europeana.portal2.web.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.europeana.portal2.web.presentation.semantic.FieldInfo;
import eu.europeana.portal2.web.presentation.semantic.SchemaOrgMapping;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/portal2-test.xml"})
public class FieldInfoTest {

	@Resource
	private SchemaOrgMapping mapping;

	@Test
	public void test() {
		FieldInfo field = new FieldInfo(mapping, "edm:ProvidedCHO", "providedCHOs", "ProvidedCHO");
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
