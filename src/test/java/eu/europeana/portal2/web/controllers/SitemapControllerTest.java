package eu.europeana.portal2.web.controllers;

import static org.junit.Assert.*;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.beans.IdBean;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.solr.exceptions.SolrTypeException;
import eu.europeana.corelib.solr.model.ResultSet;
import eu.europeana.corelib.solr.service.SearchService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/servlet/portal2-mvc.xml", "/internal/portal2-development.xml"})
public class SitemapControllerTest {

	@Resource
	private SearchService searchService;

	@Test
	public void test() {
		// fail("Not yet implemented");
		Query query = new Query("id3hash:" + 143)
						.setRefinements("COMPLETENESS:[3 TO *]")
						.setPageSize(20000)
						.setParameter("fl", "europeana_id,COMPLETENESS");
		ResultSet<? extends IdBean> resultSet = null;
		List<? extends IdBean> results = null;
		try {
			resultSet = searchService.sitemap(BriefBean.class, query);
			results = resultSet.getResults();
		} catch (SolrTypeException e) {
			e.printStackTrace();
		}

		System.out.println(String.format("Checking %d results", resultSet.getResultSize()));
		if (results != null) {
			for (BriefBean bean : (List<BriefBean>)results) {
				assertNotSame(0, bean.getEuropeanaCompleteness());
			}
		}

	}

}
