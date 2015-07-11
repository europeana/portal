package eu.europeana.portal2.web.presentation.semantic;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
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
public class EdmSchemaMappingTest {

	@Resource
	private SchemaOrgMapping mapping;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testTopLevel() {
		FieldInfo field;
		Element element;

		List<FieldInfo> edmTopLevels = EdmSchemaMapping.getTopLevel(mapping);
		assertEquals("There should be 11 top level elements", 11, edmTopLevels.size());

		field = edmTopLevels.get(0);
		assertEquals("fullBean", field.getSchemaName());
		assertEquals("fullBean", field.getJavaPropertyName());
		assertEquals("FullBean", field.getJavaType());
		assertFalse(field.isCollection());
		assertNull(field.getElement());

		field = edmTopLevels.get(10); // briefBean", "briefBean", "BriefBean
		assertEquals("briefBean", field.getSchemaName());
		assertEquals("briefBean", field.getJavaPropertyName());
		assertEquals("BriefBean", field.getJavaType());
		assertFalse(field.isCollection());
		assertNull(field.getElement());

		field = edmTopLevels.get(1); // edm:ProvidedCHO", "providedCHOs", "ProvidedCHO
		assertEquals("edm:ProvidedCHO", field.getSchemaName());
		assertEquals("providedCHOs", field.getJavaPropertyName());
		assertEquals("ProvidedCHO", field.getJavaType());
		assertFalse(field.isCollection());
		element = field.getElement();
		assertNotNull(element);
		assertNull(element.getAttributes());
		assertEquals("http://www.europeana.eu/schemas/edm/ProvidedCHO", element.getFullQualifiedURI());
		assertEquals("ProvidedCHO", element.getElementName());
		assertEquals("edm", element.getPrefix());
		assertEquals("edm:ProvidedCHO", element.getQualifiedName());
		assertEquals("http://www.europeana.eu/schemas/edm/", element.getNamespace().getUri());
		assertEquals("edm", element.getNamespace().getPrefix());

		field = edmTopLevels.get(2); // "edm:Agent", "agents", "Agent"
		assertEquals("edm:Agent", field.getSchemaName());
		assertEquals("agents", field.getJavaPropertyName());
		assertEquals("Agent", field.getJavaType());
		assertFalse(field.isCollection());
		element = field.getElement();
		assertNotNull(element);
		assertNull(element.getAttributes());
		assertEquals("http://www.europeana.eu/schemas/edm/Agent", element.getFullQualifiedURI());
		assertEquals("Agent", element.getElementName());
		assertEquals("edm", element.getPrefix());
		assertEquals("edm:Agent", element.getQualifiedName());
		assertEquals("http://www.europeana.eu/schemas/edm/", element.getNamespace().getUri());
		assertEquals("edm", element.getNamespace().getPrefix());

		field = edmTopLevels.get(3); // edm:Place", "places", "Place"
		assertEquals("edm:Place", field.getSchemaName());
		assertEquals("places", field.getJavaPropertyName());
		assertEquals("Place", field.getJavaType());
		assertFalse(field.isCollection());
		element = field.getElement();
		assertNotNull(element);
		assertNull(element.getAttributes());
		assertEquals("http://www.europeana.eu/schemas/edm/Place", element.getFullQualifiedURI());
		assertEquals("Place", element.getElementName());
		assertEquals("edm", element.getPrefix());
		assertEquals("edm:Place", element.getQualifiedName());
		assertEquals("http://www.europeana.eu/schemas/edm/", element.getNamespace().getUri());
		assertEquals("edm", element.getNamespace().getPrefix());

		field = edmTopLevels.get(4); // edm:TimeSpan", "timespans", "Timespan
		assertEquals("edm:TimeSpan", field.getSchemaName());
		assertEquals("timespans", field.getJavaPropertyName());
		assertEquals("Timespan", field.getJavaType());
		assertFalse(field.isCollection());
		element = field.getElement();
		assertNotNull(element);
		assertNull(element.getAttributes());
		assertEquals("http://www.europeana.eu/schemas/edm/TimeSpan", element.getFullQualifiedURI());
		assertEquals("TimeSpan", element.getElementName());
		assertEquals("edm", element.getPrefix());
		assertEquals("edm:TimeSpan", element.getQualifiedName());
		assertEquals("http://www.europeana.eu/schemas/edm/", element.getNamespace().getUri());
		assertEquals("edm", element.getNamespace().getPrefix());

		field = edmTopLevels.get(5); // skos:Concept", "concepts", "Concept
		assertEquals("skos:Concept", field.getSchemaName());
		assertEquals("concepts", field.getJavaPropertyName());
		assertEquals("Concept", field.getJavaType());
		assertFalse(field.isCollection());
		element = field.getElement();
		assertNotNull(element);
		assertNull(element.getAttributes());
		assertEquals("http://www.w3.org/2004/02/skos/core#Concept", element.getFullQualifiedURI());
		assertEquals("Concept", element.getElementName());
		assertEquals("skos", element.getPrefix());
		assertEquals("skos:Concept", element.getQualifiedName());
		assertEquals("http://www.w3.org/2004/02/skos/core#", element.getNamespace().getUri());
		assertEquals("skos", element.getNamespace().getPrefix());

		field = edmTopLevels.get(6); // ore:Aggregation", "aggregations", "Aggregation
		assertEquals("ore:Aggregation", field.getSchemaName());
		assertEquals("aggregations", field.getJavaPropertyName());
		assertEquals("Aggregation", field.getJavaType());
		assertFalse(field.isCollection());
		element = field.getElement();
		assertNotNull(element);
		assertNull(element.getAttributes());
		assertEquals("http://www.openarchives.org/ore/terms/Aggregation", element.getFullQualifiedURI());
		assertEquals("Aggregation", element.getElementName());
		assertEquals("ore", element.getPrefix());
		assertEquals("ore:Aggregation", element.getQualifiedName());
		assertEquals("http://www.openarchives.org/ore/terms/", element.getNamespace().getUri());
		assertEquals("ore", element.getNamespace().getPrefix());

		field = edmTopLevels.get(7); // edm:Proxy", "proxies", "Proxy
		assertEquals("edm:Proxy", field.getSchemaName());
		assertEquals("proxies", field.getJavaPropertyName());
		assertEquals("Proxy", field.getJavaType());
		assertFalse(field.isCollection());
		element = field.getElement();
		assertNotNull(element);
		assertNull(element.getAttributes());
		assertEquals("http://www.europeana.eu/schemas/edm/Proxy", element.getFullQualifiedURI());
		assertEquals("Proxy", element.getElementName());
		assertEquals("edm", element.getPrefix());
		assertEquals("edm:Proxy", element.getQualifiedName());
		assertEquals("http://www.europeana.eu/schemas/edm/", element.getNamespace().getUri());
		assertEquals("edm", element.getNamespace().getPrefix());

		field = edmTopLevels.get(8); // edm:EuropeanaAggregation", "europeanaAggregation", "EuropeanaAggregation
		assertEquals("edm:EuropeanaAggregation", field.getSchemaName());
		assertEquals("europeanaAggregation", field.getJavaPropertyName());
		assertEquals("EuropeanaAggregation", field.getJavaType());
		assertFalse(field.isCollection());
		element = field.getElement();
		assertNotNull(element);
		assertNull(element.getAttributes());
		assertEquals("http://www.europeana.eu/schemas/edm/EuropeanaAggregation", element.getFullQualifiedURI());
		assertEquals("EuropeanaAggregation", element.getElementName());
		assertEquals("edm", element.getPrefix());
		assertEquals("edm:EuropeanaAggregation", element.getQualifiedName());
		assertEquals("http://www.europeana.eu/schemas/edm/", element.getNamespace().getUri());
		assertEquals("edm", element.getNamespace().getPrefix());

		field = edmTopLevels.get(9); // edm:WebResource", "webResources", "WebResource
		assertEquals("edm:WebResource", field.getSchemaName());
		assertEquals("webResources", field.getJavaPropertyName());
		assertEquals("WebResource", field.getJavaType());
		assertFalse(field.isCollection());
		element = field.getElement();
		assertNotNull(element);
		assertNull(element.getAttributes());
		assertEquals("http://www.europeana.eu/schemas/edm/WebResource", element.getFullQualifiedURI());
		assertEquals("WebResource", element.getElementName());
		assertEquals("edm", element.getPrefix());
		assertEquals("edm:WebResource", element.getQualifiedName());
		assertEquals("http://www.europeana.eu/schemas/edm/", element.getNamespace().getUri());
		assertEquals("edm", element.getNamespace().getPrefix());
	}

	@Test
	public void testFullMap() {
		Map<String, List<FieldInfo>> edmFullMap = EdmSchemaMapping.getFullMap(mapping);
		assertEquals(11, edmFullMap.size());

		assertNotNull(edmFullMap.get("ProvidedCHO"));
		assertEquals(3, edmFullMap.get("ProvidedCHO").size());

		assertNotNull(edmFullMap.get("WebResource"));
		assertEquals(14, edmFullMap.get("WebResource").size());

		assertNotNull(edmFullMap.get("Agent"));
		assertEquals(22, edmFullMap.get("Agent").size());

		assertNotNull(edmFullMap.get("Place"));
		assertEquals(12, edmFullMap.get("Place").size());

		assertNotNull(edmFullMap.get("Timespan"));
		assertEquals(11, edmFullMap.get("Timespan").size());

		assertNotNull(edmFullMap.get("Concept"));
		assertEquals(16, edmFullMap.get("Concept").size());

		assertNotNull(edmFullMap.get("Aggregation"));
		assertEquals(16, edmFullMap.get("Aggregation").size());

		assertNotNull(edmFullMap.get("Proxy"));
		assertEquals(59, edmFullMap.get("Proxy").size());
		assertEquals("description", edmFullMap.get("Proxy").get(5).getSchemaOrgElement());

		assertNotNull(edmFullMap.get("EuropeanaAggregation"));
		assertEquals(13, edmFullMap.get("EuropeanaAggregation").size());

		assertNotNull(edmFullMap.get("FullBean"));
		assertEquals(16, edmFullMap.get("FullBean").size());

		assertNotNull(edmFullMap.get("BriefBean"));
		assertEquals(25, edmFullMap.get("BriefBean").size());
	}

	@Test
	public void testEdmElements() {
		Map<String, Element> edmElements = EdmSchemaMapping.getEdmElements(mapping);
		assertNotNull(edmElements);
		assertEquals(99, edmElements.size());
	}

	@Test
	public void testGetMapNames() {
		assertEquals(11, EdmSchemaMapping.getMapNames().size());
	}

	@Test
	public void testGetMap() {
		assertNull(EdmSchemaMapping.getMap(mapping, "nonexisting"));

		assertNotNull(EdmSchemaMapping.getMap(mapping, "ProvidedCHO"));
		assertEquals(3, EdmSchemaMapping.getMap(mapping, "ProvidedCHO").size());

		assertNotNull(EdmSchemaMapping.getMap(mapping, "WebResource"));
		assertEquals(14, EdmSchemaMapping.getMap(mapping, "WebResource").size());

		assertNotNull(EdmSchemaMapping.getMap(mapping, "Agent"));
		assertEquals(22, EdmSchemaMapping.getMap(mapping, "Agent").size());

		assertNotNull(EdmSchemaMapping.getMap(mapping, "Place"));
		assertEquals(12, EdmSchemaMapping.getMap(mapping, "Place").size());

		assertNotNull(EdmSchemaMapping.getMap(mapping, "Timespan"));
		assertEquals(11, EdmSchemaMapping.getMap(mapping, "Timespan").size());

		assertNotNull(EdmSchemaMapping.getMap(mapping, "Concept"));
		assertEquals(16, EdmSchemaMapping.getMap(mapping, "Concept").size());

		assertNotNull(EdmSchemaMapping.getMap(mapping, "Aggregation"));
		assertEquals(16, EdmSchemaMapping.getMap(mapping, "Aggregation").size());

		assertNotNull(EdmSchemaMapping.getMap(mapping, "Proxy"));
		assertEquals(59, EdmSchemaMapping.getMap(mapping, "Proxy").size());

		assertNotNull(EdmSchemaMapping.getMap(mapping, "EuropeanaAggregation"));
		assertEquals(13, EdmSchemaMapping.getMap(mapping, "EuropeanaAggregation").size());

		assertNotNull(EdmSchemaMapping.getMap(mapping, "FullBean"));
		assertEquals(16, EdmSchemaMapping.getMap(mapping, "FullBean").size());

		assertNotNull(EdmSchemaMapping.getMap(mapping, "BriefBean"));
		assertEquals(25, EdmSchemaMapping.getMap(mapping, "BriefBean").size());
	}
}
