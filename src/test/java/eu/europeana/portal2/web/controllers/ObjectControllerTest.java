package eu.europeana.portal2.web.controllers;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.beans.FullBean;
import eu.europeana.corelib.definitions.solr.entity.Aggregation;
import eu.europeana.corelib.definitions.solr.entity.ProvidedCHO;
import eu.europeana.corelib.definitions.solr.entity.Proxy;
import eu.europeana.corelib.solr.exceptions.SolrTypeException;
import eu.europeana.corelib.solr.service.SearchService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/servlet/portal2-mvc.xml", "/internal/portal2-development.xml"})
public class ObjectControllerTest {

	@Resource
	private SearchService searchService;

	@Test
	public void testRecordSimple() {
		String propertiesFile = System.getenv().get("EUROPEANA_PROPERTIES");
		assertNotNull("Properties file should not be null", propertiesFile);
		assertTrue("Properties file should be existing", new File(propertiesFile).exists());

		FullBean fullBean = null;
		try {
			fullBean = searchService.findById("91637", "9B40B6F5434D21550352BCE6DEBA0C4B7CACCDC6");
		} catch (SolrTypeException e) {
			fail("Exception happened during retrieving");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull("Full bean should not be null", fullBean);
	}

	@Test
	public void testRecord() {
		String propertiesFile = System.getenv().get("EUROPEANA_PROPERTIES");
		assertNotNull("Properties file should not be null", propertiesFile);
		assertTrue("Properties file should be existing", new File(propertiesFile).exists());

		Map<String, String> env = System.getenv();
		for (String envName : env.keySet()) {
			System.out.format("%s=%s%n", envName, env.get(envName));
		}
		try {
			FullBean fullBean = searchService.findById("91627", "B056315A5C6D63CF55A8735DBAA45884EC3F1ADE");
			assertNotNull("Full bean should not be null", fullBean);
			assertEquals(fullBean.getAbout(), "/91627/B056315A5C6D63CF55A8735DBAA45884EC3F1ADE");
			assertEquals(fullBean.getEuropeanaCompleteness(), 10);
			// assertEquals(fullBean.getWhat()[0], "Kulturhistoria");
			// assertEquals(fullBean.getWhere()[0], "Kulturhistoria");
			// assertEquals(fullBean.getWhen()[0], "Kulturhistoria");

			Aggregation aggregation = fullBean.getAggregations().get(0);
			assertEquals(aggregation.getEdmIsShownAt(), "http://www9.vgregion.se/vastarvet/objekt.aspx?ID=VGM_A29408");
			assertEquals(aggregation.getEdmObject(), "http://media1.vgregion.se/vastarvet/VGM/Fotobilder/K-bilder 2/22/1M16_A29408.JPG");
			assertEquals(aggregation.getEdmProvider(), "Swedish Open Cultural Heritage");
			assertEquals(aggregation.getAbout(), "/91627/B056315A5C6D63CF55A8735DBAA45884EC3F1ADE");

			ProvidedCHO providedCHO = fullBean.getProvidedCHOs().get(0);
			assertEquals(providedCHO.getAbout(), "/91627/B056315A5C6D63CF55A8735DBAA45884EC3F1ADE");

			Proxy proxy = fullBean.getProxies().get(0);
			assertEquals(proxy.getAbout(), "/91627/B056315A5C6D63CF55A8735DBAA45884EC3F1ADE");
			assertEquals(proxy.getDcIdentifier().values().iterator().next(), "http://kulturarvsdata.se/VGM/media/VGM_A29408");
			assertEquals(proxy.getDcSubject().values().iterator().next(), "Kulturhistoria");
			assertEquals(proxy.getDcType().values().iterator().next(), "Foto");
			assertEquals(proxy.getDctermsIssued().values().iterator().next(), "1900-01-01");
			assertEquals(proxy.getEdmType().name(), "IMAGE");

			// System.out.println(BeanUtil.toString(fullBean));
			// assertEquals("The text representation size should be 10433", 10433, BeanUtil.toString(fullBean).length());
		} catch (SolrTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testMoreLikeThis() {
		try {
			List<BriefBean> beans = searchService.findMoreLikeThis("/91637/9B40B6F5434D21550352BCE6DEBA0C4B7CACCDC6");
			assertNotNull(beans);
			assertEquals(10, beans.size());
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
