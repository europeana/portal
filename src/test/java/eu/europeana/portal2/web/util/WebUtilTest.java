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

// {"apikey":"api2demo","action":"record.json","success":true,"statsDuration":0,"object":{"about":"/91627/7E8AAB01E1C2AD825615C3153CF82C1B2D39B224","europeanaCompleteness":6,"relatedItems":[{"id":"/91627/7E8AAB01E1C2AD825615C3153CF82C1B2D39B224","timestamp":1338887080246,"provider":["Swedish Open Cultural Heritage","Swedish Open Cultural Heritage"],"edmObject":["http://media1.vgregion.se/vastarvet/VGM/Fotobilder/Bilder 7/9/1M16_B145208_70.jpg"],"europeanaCompleteness":0,"language":["sv"],"title":["Porträtt","Porträtt"],"type":"IMAGE","dataProvider":["Västergötlands museum"]}],"aggregations":[{"id":null,"edmDataProvider":"Västergötlands museum","edmIsShownAt":"http://www9.vgregion.se/vastarvet/objekt.aspx?ID=VGM_B145208_70","edmObject":"http://media1.vgregion.se/vastarvet/VGM/Fotobilder/Bilder 7/9/1M16_B145208_70.jpg","edmProvider":"Swedish Open Cultural Heritage","about":"/91627/7E8AAB01E1C2AD825615C3153CF82C1B2D39B224"}],"providedCHOs":[{"id":null,"about":"/91627/7E8AAB01E1C2AD825615C3153CF82C1B2D39B224"}],"what":["Kulturhistoria"],"where":["Kulturhistoria"],"when":["Kulturhistoria"],"proxies":[{"about":"/91627/7E8AAB01E1C2AD825615C3153CF82C1B2D39B224","dcDescription":["Louis Enjolras, Paris.\n \n inv. nr. 86879."],"dcIdentifier":["http://kulturarvsdata.se/VGM/media/VGM_B145208_70"],"dcRights":["Okänd"],"dcSource":["Västergötlands museum"],"dcSubject":["Kulturhistoria"],"dcTitle":["Porträtt"],"dcType":["Foto"],"dctermsIssued":["2002-10-21"],"edmType":"IMAGE"}],"id":"4fcdce07ccda6d72281de470","dcPublisher":[],"edmObject":["http://media1.vgregion.se/vastarvet/VGM/Fotobilder/Bilder 7/9/1M16_B145208_70.jpg"],"edmIsShownBy":[null],"edmIsShownAt":["http://www9.vgregion.se/vastarvet/objekt.aspx?ID=VGM_B145208_70"],"edmProvider":["Swedish Open Cultural Heritage"],"edmUGC":[null],"dctermsAlternative":[],"dctermsConformsTo":[],"dctermsCreated":[],"dctermsExtent":[],"dctermsHasFormat":[],"dctermsIsPartOf":[],"dctermsIsReferencedBy":[],"dctermsIsReplacedBy":[],"dctermsIsRequiredBy":[],"dctermsIssued":["2002-10-21"],"dctermsIsVersionOf":[],"dctermsMedium":[],"dctermsProvenance":[],"dctermsReferences":[],"dctermsReplaces":[],"dctermsRequires":[],"dctermsSpatial":[],"dctermsTableOfContents":[],"dctermsTemporal":[],"dcContributor":[],"dcCoverage":[],"dcCreator":[],"dcIdentifier":["http://kulturarvsdata.se/VGM/media/VGM_B145208_70"],"dcRelation":[],"dcSource":["Västergötlands museum"],"dataProvider":["Västergötlands museum"],"aggregationDcRights":[],"oreProxy":[],"owlSameAs":[],"proxyDcRights":["Okänd"],"edmCurrentLocation":[null],"edmIsNextInSequence":[null],"edmPreviewNoDistribute":[null],"edmWebResource":[],"edmWebResourceDcRights":[],"edmWebResourceEdmRights":[],"aggregationEdmRights":[null]}}

public class WebUtilTest {

	public void test() {
		//WebUtil.requestApiSession("http://localhost:8080/api2", "api2demo", "verysecret");
		WebUtil.getApiSession("http://localhost:8080/api2", "api2demo", "verysecret");
		ApiFulldocParser parser = new ApiFulldocParser("http://localhost:8080/api2", "api2demo", "verysecret", null);
		parser.getFullBean("91627", "7E8AAB01E1C2AD825615C3153CF82C1B2D39B224");
	}
	
	public void objectMapperCleanJsonTest() {
		FullBean fullBean = null;
		String baseDir = "/home/peterkiraly/workspace/europeana/trunk/portal2/";
		ObjectMapper mapper = new ObjectMapper();
		// Json2FullBean parser = new Json2FullBean();
		String json = Json2FullBean.fileToString(baseDir + "/src/test/api2.results/91627.7E8AAB01E1C2AD825615C3153CF82C1B2D39B224.json.js");
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
		String baseDir = "/home/peterkiraly/workspace/europeana/trunk/portal2/";
		Json2FullBean parser = new Json2FullBean(new File(baseDir + "/src/test/api2.results/91627.7E8AAB01E1C2AD825615C3153CF82C1B2D39B224.json.js"));
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
