package eu.europeana.portal2.web.util;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
				new HashMap<String, String>(){{put("hello", "hello");}}, 
				new HashMap<Integer, String>(){{put(1, "hello");}}};

		for (Object obj : vars) {
			if (obj instanceof String) {
				System.out.println(obj + " is a String");
			}
			if (obj instanceof String[]) {
				System.out.println(obj + " is a String[]");
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
}
