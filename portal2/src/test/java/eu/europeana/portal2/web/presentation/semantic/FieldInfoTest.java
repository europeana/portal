package eu.europeana.portal2.web.presentation.semantic;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import eu.europeana.portal2.web.presentation.semantic.FieldInfo;
import eu.europeana.portal2.web.presentation.semantic.SchemaOrgMapping;

@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/portal2-test.xml"})
@Ignore // disabled as portal project is deprecated
public class FieldInfoTest {

	@Resource
	private SchemaOrgMapping mapping;

	@Test
	public void testConstructor() {
		FieldInfo field = new FieldInfo(mapping, "edm:ProvidedCHO", "providedCHOs", "ProvidedCHO");
		assertEquals("Schema name should be edm:ProvidedCHO", "edm:ProvidedCHO", field.getSchemaName());
		assertEquals("Property name should be providedCHOs", "providedCHOs", field.getJavaPropertyName());
		assertEquals("Type name should be ProvidedCHO", "ProvidedCHO", field.getJavaType());

		// testing element
		assertNotNull("Element should not be null", field.getElement());
		assertEquals("Qualified name should be edm:ProvidedCHO", "edm:ProvidedCHO", field.getElement().getQualifiedName());
		assertEquals("Prefix should be edm", "edm", field.getElement().getPrefix());
		assertEquals("Namespace URI should be http://www.europeana.eu/schemas/edm/", "http://www.europeana.eu/schemas/edm/", field.getElement().getNamespace().getUri());
		assertEquals("Full Qualified URI should be http://www.europeana.eu/schemas/edm/", 
				"http://www.europeana.eu/schemas/edm/ProvidedCHO", field.getElement().getFullQualifiedURI());
	}

	@Test
	public void testSetSchemaName() {
		FieldInfo field = new FieldInfo(mapping, "edm:ProvidedCHO", "providedCHOs", "ProvidedCHO");
		field.setSchemaName("dc:format");
		assertEquals("dc:format", field.getSchemaName());
	}

	@Test
	public void testSetJavaPropertyName() {
		FieldInfo field = new FieldInfo(mapping, "edm:ProvidedCHO", "providedCHOs", "ProvidedCHO");
		field.setJavaPropertyName("dcFormat");
		assertEquals("dcFormat", field.getJavaPropertyName());
	}

	@Test
	public void testSetJavaType() {
		FieldInfo field = new FieldInfo(mapping, "dc:format", "dcFormat", "Map<String,List<String>>", "WebResource");
		field.setJavaType("String");
		assertEquals("String", field.getJavaType());
	}

	@Test
	public void testTypeFinders() {
		FieldInfo field = new FieldInfo(mapping, "dc:format", "dcFormat", null, "WebResource");
		assertFalse(field.isCollection());
		assertFalse(field.isMap());
		assertFalse(field.isCollectionOfMaps());
		assertFalse(field.isMapsOfLists());
		assertFalse(field.isMultiValue());

		field.setJavaType("Map<String,List<String>>");
		assertTrue(field.isCollection());
		assertFalse(field.isMap());
		assertFalse(field.isCollectionOfMaps());
		assertTrue(field.isMapsOfLists());
		assertTrue(field.isMultiValue());

		field.setJavaType("List<Map<String,String>>");
		assertTrue(field.isCollection());
		assertFalse(field.isMap());
		assertTrue(field.isCollectionOfMaps());
		assertFalse(field.isMapsOfLists());
		assertTrue(field.isMultiValue());

		field.setJavaType("Map<String,String>");
		assertTrue(field.isCollection());
		assertTrue(field.isMap());
		assertFalse(field.isCollectionOfMaps());
		assertFalse(field.isMapsOfLists());
		assertTrue(field.isMultiValue());

		field.setJavaType("String[]");
		assertTrue(field.isCollection());
		assertFalse(field.isMap());
		assertFalse(field.isCollectionOfMaps());
		assertFalse(field.isMapsOfLists());
		assertTrue(field.isMultiValue());
	}

	@Test
	public void testSchemaOrgElementFunctions() {
		FieldInfo field;
		field = new FieldInfo(mapping, "edm:hasView", "hasView", "String[]", "Aggregation");
		assertEquals("url", field.getSchemaOrgElement());
		assertTrue(field.getSchemaOrgElementIsURL());

		field = new FieldInfo(mapping, "edm:dataProvider", "edmDataProvider", "Map<String,List<String>>", "Aggregation");
		assertEquals("provider", field.getSchemaOrgElement());
		assertFalse(field.getSchemaOrgElementIsURL());

		field = new FieldInfo(mapping, "edm:aggregatedCHO", "aggregatedCHO", "String", "Aggregation");
		assertNull(field.getSchemaOrgElement());
		assertFalse(field.getSchemaOrgElementIsURL());
	}
}
