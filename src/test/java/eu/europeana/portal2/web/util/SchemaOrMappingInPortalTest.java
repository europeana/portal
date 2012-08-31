package eu.europeana.portal2.web.util;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/servlet/portal2-mvc.xml", "/internal/portal2-development.xml"})
public class SchemaOrMappingInPortalTest {

	@Resource(name="runtimeConfig")
	private Properties config;

	@Test
	public void test() {
		// fail("Not yet implemented");
		String mappingFile = config.getProperty("schema.org.mapping");
		assertNotNull(mappingFile);
		assertTrue(new File(mappingFile).exists());
		Properties schemaProp = new Properties();
		try {
			schemaProp.load(new FileInputStream(mappingFile));
			for (Object key : schemaProp.keySet()) {
				System.out.println(key);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
