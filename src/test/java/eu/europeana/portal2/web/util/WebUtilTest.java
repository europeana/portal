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
import eu.europeana.corelib.solr.bean.impl.FullBeanImpl;
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
			// fullBean.getDctermsIsPartOf();
			fullBean.getAggregations().get(0).getEdmDataProvider();
			// assertEquals(1, fullBean.getRelatedItems().size());
			// assertEquals("BriefBeanImpl", fullBean.getRelatedItems().get(0).getClass().getSimpleName());
			// assertNotNull(fullBean.getRelatedItems().get(0));
			// BriefBean relatedItem = fullBean.getRelatedItems().get(0);
			// assertEquals("DocType", relatedItem.getType().getClass().getSimpleName());
			assertEquals("DocType", fullBean.getProxies().get(0).getEdmType().getClass().getSimpleName());
			assertEquals("ProvidedCHOImpl", fullBean.getProvidedCHOs().get(0).getClass().getSimpleName());
			assertEquals("AggregationImpl", fullBean.getAggregations().get(0).getClass().getSimpleName());
			assertEquals(6, fullBean.getEuropeanaCompleteness());
			// assertEquals("Kulturhistoria", fullBean.getWhat()[0]);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void shortcutTest() {
		FullBean fullBean = null;
		// String baseDir = "/home/peterkiraly/workspace/europeana/trunk/portal2/";
		Json2FullBean parser = new Json2FullBean(new File("src/test/api2.results/91627.7E8AAB01E1C2AD825615C3153CF82C1B2D39B224.json.js"));
		try {
			FullBean bean = parser.extractFullBean();
			FullBeanShortcut shortcut = new FullBeanShortcut((FullBeanImpl)bean);
			assertNotNull(shortcut);
			if (bean.getAggregations().get(0).getEdmIsShownAt() != null) {
				assertEquals(bean.getAggregations().get(0).getEdmIsShownAt(), shortcut.get("EdmIsShownAt")[0]);
			}
			if (bean.getAggregations().get(0).getEdmIsShownBy() != null) {
				assertEquals(bean.getAggregations().get(0).getEdmIsShownBy(), shortcut.get("EdmIsShownBy")[0]);
			}
			if (bean.getAggregations().get(0).getEdmRights() != null) {
				assertEquals(bean.getAggregations().get(0).getEdmRights(), shortcut.get("EdmRights")[0]);
			}
			if (bean.getAggregations().get(0).getEdmProvider() != null) {
				assertEquals(bean.getAggregations().get(0).getEdmProvider(), shortcut.get("EdmProvider")[0]);
			}
			if (bean.getAggregations().get(0).getEdmObject() != null) {
				assertEquals(bean.getAggregations().get(0).getEdmObject(), shortcut.get("EdmObject")[0]);
			}
			if (bean.getAggregations().get(0).getEdmUgc() != null) {
				assertEquals(bean.getAggregations().get(0).getEdmUgc(), shortcut.get("EdmUGC")[0]);
			}
			if (bean.getAggregations().get(0).getEdmDataProvider() != null) {
				assertEquals(bean.getAggregations().get(0).getEdmDataProvider(), shortcut.get("DataProvider")[0]);
			}
			if (bean.getAggregations().get(0).getDcRights() != null) {
				assertEquals(bean.getAggregations().get(0).getDcRights(), shortcut.get("AggregationDcRights")[0]);
			}

			// proxy
			if (bean.getProxies().get(0).getDcContributor() != null) {
				assertEquals(bean.getProxies().get(0).getDcContributor(), shortcut.get("DcContributor")[0]);
			}
			if (bean.getProxies().get(0).getDcCoverage() != null) {
				assertEquals(bean.getProxies().get(0).getDcCoverage(), shortcut.get("DcCoverage")[0]);
			}
			if (bean.getProxies().get(0).getDcCreator() != null) {
				assertEquals(bean.getProxies().get(0).getDcCreator(), shortcut.get("DcCreator")[0]);
			}
			if (bean.getProxies().get(0).getDcIdentifier() != null) {
				assertEquals(bean.getProxies().get(0).getDcIdentifier(), shortcut.get("DcIdentifier"));
			}
			if (bean.getProxies().get(0).getDcPublisher() != null) {
				assertEquals(bean.getProxies().get(0).getDcPublisher(), shortcut.get("DcPublisher")[0]);
			}
			if (bean.getProxies().get(0).getDcRelation() != null) {
				assertEquals(bean.getProxies().get(0).getDcRelation(), shortcut.get("DcRelation")[0]);
			}
			if (bean.getProxies().get(0).getDcSource() != null) {
				assertEquals(bean.getProxies().get(0).getDcSource(), shortcut.get("DcSource"));
			}
			if (bean.getProxies().get(0).getDctermsAlternative() != null) {
				assertEquals(bean.getProxies().get(0).getDctermsAlternative(), shortcut.get("DctermsAlternative")[0]);
			}
			if (bean.getProxies().get(0).getDctermsConformsTo() != null) {
				assertEquals(bean.getProxies().get(0).getDctermsConformsTo(), shortcut.get("DctermsConformsTo")[0]);
			}
			if (bean.getProxies().get(0).getDctermsCreated() != null) {
				assertEquals(bean.getProxies().get(0).getDctermsCreated(), shortcut.get("DctermsCreated")[0]);
			}
			if (bean.getProxies().get(0).getDctermsExtent() != null) {
				assertEquals(bean.getProxies().get(0).getDctermsExtent(), shortcut.get("DctermsExtent")[0]);
			}
			if (bean.getProxies().get(0).getDctermsHasFormat() != null) {
				assertEquals(bean.getProxies().get(0).getDctermsHasFormat(), shortcut.get("DctermsHasFormat")[0]);
			}
			if (bean.getProxies().get(0).getDctermsHasPart() != null) {
				assertEquals(bean.getProxies().get(0).getDctermsHasPart(), shortcut.get("DctermsHasPart")[0]);
			}
			if (bean.getProxies().get(0).getDctermsIsReferencedBy() != null) {
				assertEquals(bean.getProxies().get(0).getDctermsIsReferencedBy(), shortcut.get("DctermsIsReferencedBy")[0]);
			}
			if (bean.getProxies().get(0).getDctermsIsReplacedBy() != null) {
				assertEquals(bean.getProxies().get(0).getDctermsIsReplacedBy(), shortcut.get("DctermsIsReplacedBy")[0]);
			}
			if (bean.getProxies().get(0).getDctermsIsRequiredBy() != null) {
				assertEquals(bean.getProxies().get(0).getDctermsIsRequiredBy(), shortcut.get("DctermsIsRequiredBy")[0]);
			}
			if (bean.getProxies().get(0).getDctermsIsPartOf() != null) {
				assertEquals(bean.getProxies().get(0).getDctermsIsPartOf(), shortcut.get("DctermsIsPartOf")[0]);
			}
			if (bean.getProxies().get(0).getDctermsIssued() != null) {
				assertEquals(bean.getProxies().get(0).getDctermsIssued(), shortcut.get("DctermsIssued"));
			}
			if (bean.getProxies().get(0).getDctermsIsVersionOf() != null) {
				assertEquals(bean.getProxies().get(0).getDctermsIsVersionOf(), shortcut.get("DctermsIsVersionOf")[0]);
			}
			if (bean.getProxies().get(0).getDctermsMedium() != null) {
				assertEquals(bean.getProxies().get(0).getDctermsMedium(), shortcut.get("DctermsMedium")[0]);
			}
			if (bean.getProxies().get(0).getDctermsProvenance() != null) {
				assertEquals(bean.getProxies().get(0).getDctermsProvenance(), shortcut.get("DctermsProvenance")[0]);
			}
			if (bean.getProxies().get(0).getDctermsReferences() != null) {
				assertEquals(bean.getProxies().get(0).getDctermsReferences(), shortcut.get("DctermsReferences")[0]);
			}
			if (bean.getProxies().get(0).getDctermsReplaces() != null) {
				assertEquals(bean.getProxies().get(0).getDctermsReplaces(), shortcut.get("DctermsReplaces")[0]);
			}
			if (bean.getProxies().get(0).getDctermsRequires() != null) {
				assertEquals(bean.getProxies().get(0).getDctermsRequires(), shortcut.get("DctermsRequires")[0]);
			}
			if (bean.getProxies().get(0).getDctermsSpatial() != null) {
				assertEquals(bean.getProxies().get(0).getDctermsSpatial(), shortcut.get("DctermsSpatial")[0]);
			}
			if (bean.getProxies().get(0).getDctermsTemporal() != null) {
				assertEquals(bean.getProxies().get(0).getDctermsTemporal(), shortcut.get("DctermsTemporal")[0]);
			}
			if (bean.getProxies().get(0).getDctermsTOC() != null) {
				assertEquals(bean.getProxies().get(0).getDctermsTOC(), shortcut.get("DctermsTableOfContents")[0]);
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
