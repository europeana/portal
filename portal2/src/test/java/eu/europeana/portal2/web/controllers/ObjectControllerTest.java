package eu.europeana.portal2.web.controllers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.europeana.corelib.definitions.solr.beans.FullBean;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.solr.exceptions.SolrTypeException;
import eu.europeana.corelib.solr.service.SearchService;
import eu.europeana.portal2.web.presentation.model.FullBeanView;
import eu.europeana.portal2.web.presentation.model.FullBeanViewImpl;
import eu.europeana.portal2.web.presentation.model.FullDocPage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/portal2-test.xml"})
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
				e1.printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			model.setFullBeanView(fullBeanView);
			try {
				System.out.println(model.getFullBeanView().getDocIdWindowPager().getNextFullDocUrl());
				System.out.println(model.getFullBeanView().getDocIdWindowPager().getPreviousFullDocUrl());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} catch (SolrTypeException e) {
			fail("Exception happened during retrieving");
			e.printStackTrace();
		}
		assertNotNull("Full bean should not be null", fullBean);
	}

}
