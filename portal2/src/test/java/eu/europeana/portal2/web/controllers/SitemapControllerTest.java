package eu.europeana.portal2.web.controllers;

import java.util.List;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.logging.Log;
import eu.europeana.corelib.solr.exceptions.SolrTypeException;
import eu.europeana.corelib.solr.service.SearchService;

@RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration({"/servlet/portal2-mvc.xml", "/portal2-test.xml"})
@ContextConfiguration({"/portal2-context.xml"})
public class SitemapControllerTest {

	@Log
	private Logger log;

	@Resource
	private SearchService searchService;

	private static final String PREFIX_PATTERN = "^[0-9A-F]{3}$";

	@Test
	public void test() {
		Query query = new Query("*:*")
			.setPageSize(0)
			.setParameter("facet", "on")
			.setParameter("facet.field", "id3hash")
			.setParameter("facet.limit", "1000000")
			.setParameter("facet.sort", "lexical")
			;
		try {
			int prefixes = 0;
			long total = 0;
			int hashProblems = 0;
			int millions = 0;
			List<FacetField> results = searchService.sitemap(BriefBean.class, query).getFacetFields();
			// List<Count> counts = new ArrayList<Count>();
			for (FacetField facet : results) {
				if (facet.getName().equals("id3hash")) {
					List<Count> values = facet.getValues();
					for (Count value : values) {
						prefixes++;
						total += value.getCount();
						if (total > (millions * 10000)) {
							log.info("total: " + total);
							millions++;
						}
						if (hasMoreId(value.getName())) {
							hashProblems++;
						}
					}
				}
			}
			log.info(String.format("prefixes: %d, total: %d", prefixes, total));
			log.info(String.format("hashProblems: %d", hashProblems));
		} catch (SolrTypeException e) {
			e.printStackTrace();
		}
	}

	private boolean hasMoreId(String name) {
		Query query = new Query("*:*")
					.setRefinements("id3hash:" + name)
					.setPageSize(100000)
					.setParameter("fl", "europeana_id,id3hash")
		;
		try {
			List<BriefBean> records = searchService.sitemap(BriefBean.class, query).getResults();
			for (BriefBean bean : records) {
				/*
				if (bean.getId3hash().length != 1) {
					log.info(StringUtils.join(bean.getId3hash(), ", "));
					return true;
				} else {
					String hash = bean.getId3hash()[0].replaceAll("^/[^/]+/", "").substring(0, 3);
					// String strinctHash = bean.getId3hash()[0].replaceAll("^/[^/]+/", "").substring(0, 3);
					if (!hash.equals(name)) {
						log.info("name: " + name + " versus hash: " + hash);
						return true;
					}
				}
				*/
			}
		} catch (SolrTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private long getCount(String hash1, String hash2) {
		Query query = new Query("*:*")
			.setPageSize(0)
			.setRefinements(String.format("id3hash:%s OR id3hash:%s", hash1, hash2))
		;
		
		try {
			return searchService.sitemap(BriefBean.class, query).getResultSize();
		} catch (SolrTypeException e) {
			e.printStackTrace();
		}
		return 0;
	}
}