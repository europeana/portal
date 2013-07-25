package eu.europeana.portal2.web.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
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
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.solr.exceptions.SolrTypeException;
import eu.europeana.corelib.solr.service.SearchService;
import eu.europeana.portal2.web.presentation.model.FullBeanView;
import eu.europeana.portal2.web.presentation.model.FullBeanViewImpl;
import eu.europeana.portal2.web.presentation.model.FullDocPage;

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
			FullDocPage model = new FullDocPage();
			fullBean = searchService.findById("91626", "5AB4F1FA6904FC9BA63B5B58ADCDB4BEF2900147",false);
			Query query = new Query("*:*").setRefinements("TYPE:IMAGE");
			Map<String, String[]> params = new HashMap<String, String[]>();
			params.put("start", new String[]{"39"});
			params.put("startPage", new String[]{"37"});
			params.put("query", new String[]{"*:*"});
			params.put("qf", new String[]{"TYPE:TEXT"});
			FullBeanView fullBeanView = new FullBeanViewImpl(fullBean, params, query, searchService);
			try {
				assertNotNull("DocIdWindowPager should not be null", fullBeanView.getDocIdWindowPager());
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			model.setFullBeanView(fullBeanView);
			try {
				System.out.println(model.getFullBeanView().getDocIdWindowPager().getNextFullDocUrl());
				System.out.println(model.getFullBeanView().getDocIdWindowPager().getPreviousFullDocUrl());
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// model.fullBeanView.docIdWindowPager
		} catch (SolrTypeException e) {
			fail("Exception happened during retrieving");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull("Full bean should not be null", fullBean);
	}

	// @Test
	public void testRecord() {
		String propertiesFile = System.getenv().get("EUROPEANA_PROPERTIES");
		assertNotNull("Properties file should not be null", propertiesFile);
		assertTrue("Properties file should be existing", new File(propertiesFile).exists());

		Map<String, String> env = System.getenv();
		for (String envName : env.keySet()) {
			System.out.format("%s=%s%n", envName, env.get(envName));
		}
		try {
			FullBean fullBean = searchService.findById("91627", "B056315A5C6D63CF55A8735DBAA45884EC3F1ADE",false);
			assertNotNull("Full bean should not be null", fullBean);
			assertEquals("/91627/B056315A5C6D63CF55A8735DBAA45884EC3F1ADE", fullBean.getAbout());
			assertEquals(10, fullBean.getEuropeanaCompleteness());
			// assertEquals(fullBean.getWhat()[0], "Kulturhistoria");
			// assertEquals(fullBean.getWhere()[0], "Kulturhistoria");
			// assertEquals(fullBean.getWhen()[0], "Kulturhistoria");

			Aggregation aggregation = fullBean.getAggregations().get(0);
			assertEquals("http://www9.vgregion.se/vastarvet/objekt.aspx?ID=VGM_A29408", aggregation.getEdmIsShownAt());
			assertEquals("http://media1.vgregion.se/vastarvet/VGM/Fotobilder/K-bilder 2/22/1M16_A29408.JPG", aggregation.getEdmObject());
			assertEquals("Swedish Open Cultural Heritage", aggregation.getEdmProvider().get("def"));
			assertEquals("/aggregation/provider/91627/B056315A5C6D63CF55A8735DBAA45884EC3F1ADE", aggregation.getAbout());

			ProvidedCHO providedCHO = fullBean.getProvidedCHOs().get(0);
			assertEquals("/item/91627/B056315A5C6D63CF55A8735DBAA45884EC3F1ADE", providedCHO.getAbout());

			Proxy proxy = fullBean.getProxies().get(0);
			assertEquals("/proxy/provider/91627/B056315A5C6D63CF55A8735DBAA45884EC3F1ADE", proxy.getAbout());
			assertEquals("http://kulturarvsdata.se/VGM/media/VGM_A29408", proxy.getDcIdentifier().values().iterator().next());
			assertEquals("Kulturhistoria", proxy.getDcSubject().values().iterator().next());
			assertEquals("Foto", proxy.getDcType().values().iterator().next());
			assertEquals("1900-01-01", proxy.getDctermsIssued().values().iterator().next());
			assertEquals("IMAGE", proxy.getEdmType().name());

			// System.out.println(BeanUtil.toString(fullBean));
			// assertEquals("The text representation size should be 10433", 10433, BeanUtil.toString(fullBean).length());
		} catch (SolrTypeException e) {
			// TODO Auto-generated catch block
			fail("Solr exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// @Test
	public void testMoreLikeThis() {
		try {
			List<BriefBean> beans = searchService.findMoreLikeThis("/91637/9B40B6F5434D21550352BCE6DEBA0C4B7CACCDC6");
			assertNotNull(beans);
			// TODO: change it later, now the data wrong
			assertEquals(0, beans.size());
		} catch (SolrServerException e) {
			fail("Solr exception: " + e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
