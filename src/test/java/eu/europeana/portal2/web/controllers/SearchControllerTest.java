package eu.europeana.portal2.web.controllers;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Resource;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.solr.exceptions.SolrTypeException;
import eu.europeana.corelib.solr.service.SearchService;
import eu.europeana.portal2.querymodel.query.FacetQueryLinks;
import eu.europeana.portal2.web.presentation.model.BriefBeanView;
import eu.europeana.portal2.web.presentation.model.SearchPage;
import eu.europeana.portal2.web.presentation.model.data.decorators.BriefBeanDecorator;
import eu.europeana.portal2.web.util.SearchUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/servlet/portal2-mvc.xml", "/internal/portal2-development.xml"})
public class SearchControllerTest {

	@Resource
	private SearchService searchService;

	private final Logger log = Logger.getLogger(getClass().getName());

	// @Test
	public void test() {
		SearchController controller = new SearchController();
		String[] qf = new String[]{""};
		try {
			/*
			ModelAndView modelAndView = controller.searchHtml("ebook", "", "", "", "", "", qf, "", "", 10, 12, 
					"portal",
					null, null, null);
			assertEquals("search", modelAndView.getViewName());
			assertNull(modelAndView.getView());
			*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Exception happened");
		}
	}
	
	// @Test
	public void refinementTest() {
		String[] numbers = new String[]{"1", "2", "3"};
		String[] words = new String[]{"one", "two", "three"};
		
		String[] all = (String[]) ArrayUtils.addAll(numbers, words);
		assertEquals(3, numbers.length);
		assertEquals(6, all.length);
		assertArrayEquals(new String[]{"1", "2", "3", "one", "two", "three"}, all);
		testFunction(all);
	}
	
	private void testFunction(String... strings) {
		// do nothing
	}

	@Test
	public void testSearchResultFrom1() {
		testSearchResultFrom(1);
		// testSearchResultFrom(100);
	}

	private void testSearchResultFrom(int start) {
		SearchPage model = new SearchPage();
		model.setStart(start);

		Query query = new Query("*:*").setRefinements("general");
		Class<? extends BriefBean> clazz = BriefBean.class;
		Map<String, String[]> params = new HashMap<String, String[]>();
		params.put("query", new String[]{"*:*"});
		params.put("qf", new String[]{"general"});
		BriefBeanView briefBeanView;

		try {
			briefBeanView = SearchUtils.createResults(searchService, clazz, "portal", query, start, 12, params, "query=*:*&qf=general");
			model.setBriefBeanView(briefBeanView);
			int index = start;
			for (Object o : model.getBriefBeanView().getBriefDocs()) {
				assertTrue(o instanceof BriefBeanDecorator);
				BriefBeanDecorator bean = (BriefBeanDecorator)o;
				assertTrue(bean.getFullDocUrl().indexOf("start=" + index++) > -1);
			}
			for (FacetQueryLinks facet : briefBeanView.getFacetQueryLinks()) {
				if (facet.getType().equals("TYPE")) {
					assertEquals("&qf=general&qf=TYPE:TEXT", facet.getLinks().get(0).getUrl());
				}
			}
		} catch (SolrTypeException e) {
			fail("SolrTypeException: " + e.getLocalizedMessage());
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			fail("UnsupportedEncodingException: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
}
