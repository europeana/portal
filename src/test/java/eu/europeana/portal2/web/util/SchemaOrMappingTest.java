package eu.europeana.portal2.web.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import eu.europeana.portal2.web.presentation.enums.Field;
import eu.europeana.portal2.web.presentation.model.FullDocPreparation;
import eu.europeana.portal2.web.presentation.semantic.SchemaOrgElement;
import eu.europeana.portal2.web.presentation.semantic.SchemaOrgMapping;

public class SchemaOrMappingTest {

	// @Test
	public void test() {
		// fail("Not yet implemented");
		String mappingFile = "/home/peterkiraly/workspace/europeana/trunk/schema.org.mapping.properties";
		assertNotNull(mappingFile);
		assertTrue(new File(mappingFile).exists());
		SchemaOrgMapping.initialize(mappingFile);
		assertEquals(new SchemaOrgElement("schema:addressCountry", new String[]{}), SchemaOrgMapping.get("edm:country"));
	}

	@Test
	public void testTypes() {
		Object[] vars = new Object[]{
				"hello", 
				new String[]{"hello"}, 
				Arrays.asList("hello"), 
				new HashMap<String, String>(){{put("hello", "hello");}}, 
				new HashMap<Integer, String>(){{put(1, "hello");}}};

		for (Object obj : vars) {
			if (obj instanceof String) {
				System.out.println(obj + " is a String");
			}
			if (obj instanceof String[]) {
				System.out.println(obj + " is a String[]");
			}
			if (obj instanceof List<?>) {
				System.out.println(obj + " is a List<?>");
			}
			if (obj instanceof Map) {
				System.out.println(obj + " is a Map");
			}
			if (obj instanceof HashMap<?,?>) {
				System.out.println(obj + " is a HashMap<>");
			}
			if (obj instanceof Map<?,?>) {
					System.out.println(obj + " is a Map<>");
			}
		}
	}

	@Test
	public void testDescriptionIssue() {
		Object[] vars = new Object[]{
				new String[]{"Numero perxa: 581", "Vestido s. XIX. Luisa Fernanda"},
				FullDocPreparation.map(Field.DCTERMS_TABLEOFCONTENTS, Arrays.asList("http://colleccions.cdmae.cat/u?/indu,3341"))
		};
		Field fieldInfo = Field.DC_DESCRIPTION;

		for (Object fieldValueArray : vars) {
			if (fieldInfo.equals(Field.DC_DESCRIPTION)) {
				// log.info("fieldValueArray: " + fieldValueArray.getClass());
			}
			if (fieldValueArray == null) {
				continue;
			}
			if (fieldValueArray instanceof String[]) {
				System.out.println("is String[]");
			} else if (fieldValueArray instanceof List<?>) {
					System.out.println("is String[]");
				// extractFieldValues(fieldPresentation, fieldInfo, (String[])fieldValueArray);
			} else if (fieldValueArray instanceof Map) {
				System.out.println("is Map");
				if (fieldInfo.equals(Field.DC_DESCRIPTION)) {
					// log.info("-- map: " + map + ", fieldValueArray: " + fieldValueArray);
				}
				// Map<Field, String[]> map = (Map<Field, String[]>)fieldValueArray;
				// fieldPresentation.addMap(this, map);
			}
		}
	}
}
