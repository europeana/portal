package eu.europeana.portal2.web.presentation.model.submodel.impl;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.europeana.corelib.definitions.ApplicationContextContainer;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.web.service.EuropeanaUrlService;
import eu.europeana.corelib.web.service.impl.EuropeanaUrlServiceImpl;
import eu.europeana.portal2.web.presentation.model.submodel.BriefBeanView;
import eu.europeana.portal2.web.presentation.model.submodel.SearchFilter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/portal2-test.xml"})
public class BriefBeanViewImplTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {

		Map<String, String[]> urlParams = new HashMap<String, String[]>();
		urlParams.put("query", new String[]{"*:*"});
		urlParams.put("qf", new String[]{"DATA_PROVIDER:\"Tyne & Wear Archives & Museums\"", "TYPE:IMAGE"});
		urlParams.put("rows", new String[]{"24"});

		Query query = new Query("*:*")
			.setRefinements(
				"DATA_PROVIDER:\"Tyne & Wear Archives & Museums\"", 
				"TYPE:IMAGE"
			);
		BriefBeanViewImpl view = new BriefBeanViewImpl();

		view.makeFilters(query, urlParams);
		List<SearchFilter> filters = view.getSearchFilters();
		assertTrue(filters.size() == 3);

		assertEquals(
			"/portal/search.html?rows=24&query=*%3A*",
			filters.get(0).getBreadcrumbLinkUrl());
		assertEquals(
			"/portal/search.html?rows=24&qf=DATA_PROVIDER%3A%22Tyne+%26+Wear+Archives+%26+Museums%22&qf=TYPE%3AIMAGE",
			filters.get(0).getRemoveLinkUrl());

		assertEquals(
			"/portal/search.html?rows=24&query=*%3A*&qf=DATA_PROVIDER%3A%22Tyne+%26+Wear+Archives+%26+Museums%22",
			filters.get(1).getBreadcrumbLinkUrl());
		assertEquals(
			"/portal/search.html?rows=24&query=*%3A*&qf=TYPE%3AIMAGE",
			filters.get(1).getRemoveLinkUrl());

		assertEquals(
			"/portal/search.html?rows=24&query=*%3A*&qf=DATA_PROVIDER%3A%22Tyne+%26+Wear+Archives+%26+Museums%22&qf=TYPE%3AIMAGE",
			filters.get(2).getBreadcrumbLinkUrl());
		assertEquals(
			"/portal/search.html?rows=24&query=*%3A*&qf=DATA_PROVIDER%3A%22Tyne+%26+Wear+Archives+%26+Museums%22",
			filters.get(2).getRemoveLinkUrl());
	}

}
