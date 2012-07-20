package eu.europeana.portal2.web.util;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.beans.FullBean;
import eu.europeana.portal2.web.controllers.utils.ApiFulldocParser;
import eu.europeana.portal2.web.model.ObjectResult;
import eu.europeana.portal2.web.model.json.Json2FullBean;

public class WebUtilTest {

	public void test() {
		//WebUtil.requestApiSession("http://localhost:8080/api2", "api2demo", "verysecret");
		WebUtil.getApiSession("http://localhost:8080/api2", "api2demo", "verysecret");
		ApiFulldocParser parser = new ApiFulldocParser("http://localhost:8080/api2", "api2demo", "verysecret", null);
		parser.getFullBean("91627", "7E8AAB01E1C2AD825615C3153CF82C1B2D39B224");
	}
	
	public void objectMapperCleanJsonTest() {
		FullBean fullBean = null;
		ObjectMapper mapper = new ObjectMapper();
		// Json2FullBean parser = new Json2FullBean();
		String json = Json2FullBean.fileToString("src/test/api2.results/91627.7E8AAB01E1C2AD825615C3153CF82C1B2D39B224.json.js");
		json = json.replaceAll(",\"[^\"]+\":null", "");
		assertNotNull(json);

		try {
			ObjectResult result = mapper.readValue(json, ObjectResult.class);
			System.out.println(result.getClass());
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void objectMapperTest() {
		FullBean fullBean = null;
		// String baseDir = "/home/peterkiraly/workspace/europeana/trunk/portal2/";
		Json2FullBean parser = new Json2FullBean(new File("src/test/api2.results/91627.7E8AAB01E1C2AD825615C3153CF82C1B2D39B224.json.js"));
		try {
			fullBean = parser.extractFullBean();
			fullBean.getDctermsIsPartOf();
			fullBean.getAggregations().get(0).getEdmDataProvider();
			assertEquals(1, fullBean.getRelatedItems().size());
			assertEquals("BriefBeanImpl", fullBean.getRelatedItems().get(0).getClass().getSimpleName());
			assertNotNull(fullBean.getRelatedItems().get(0));
			BriefBean relatedItem = fullBean.getRelatedItems().get(0);
			assertEquals("DocType", relatedItem.getType().getClass().getSimpleName());
			assertEquals("DocType", fullBean.getProxies().get(0).getEdmType().getClass().getSimpleName());
			assertEquals("ProvidedCHOImpl", fullBean.getProvidedCHOs().get(0).getClass().getSimpleName());
			assertEquals("AggregationImpl", fullBean.getAggregations().get(0).getClass().getSimpleName());
			assertEquals(6, fullBean.getEuropeanaCompleteness());
			assertEquals("Kulturhistoria", fullBean.getWhat()[0]);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
