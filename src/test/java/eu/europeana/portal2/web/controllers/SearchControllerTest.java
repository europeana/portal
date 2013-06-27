package eu.europeana.portal2.web.controllers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.solr.client.solrj.response.FacetField;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.solr.exceptions.SolrTypeException;
import eu.europeana.corelib.solr.model.ResultSet;
import eu.europeana.corelib.solr.service.SearchService;
import eu.europeana.portal2.querymodel.query.FacetQueryLinks;
import eu.europeana.portal2.web.model.ModelUtils;
import eu.europeana.portal2.web.model.facets.Facet;
import eu.europeana.portal2.web.presentation.model.BriefBeanView;
import eu.europeana.portal2.web.presentation.model.BriefBeanViewImpl;
import eu.europeana.portal2.web.presentation.model.SearchPage;
import eu.europeana.portal2.web.presentation.model.data.decorators.BriefBeanDecorator;
import eu.europeana.portal2.web.util.SearchUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/servlet/portal2-mvc.xml", "/internal/portal2-development.xml" })
public class SearchControllerTest {

	@Resource
	private SearchService searchService;

	private float cents = 0.2f;
	private int iterations = (int) (100 * cents);

	private List<String> words;

	@Test
	public void allSpeedTest() {
		words = randomWords(iterations);
		// solrJSpeedTest();
		searchServiceSpeedTest();
		restSpeedTest();
		// solrSpeedTest();
	}

	public void restSpeedTest() {
		String baseUrl = "http://localhost:8080/portal/search.html?rows=12&query=%s";

		long t = new Date().getTime();
		long min = 0, max = 0, t1, t3;
		String maxw = "";
		int i;
		List<String> slowQueries = new ArrayList<String>();
		for (i = 0; i < iterations; i++) {
			if (i > words.size() - 1) {
				break;
			}
			String url = String.format(baseUrl, words.get(i));
			t1 = new Date().getTime();
			getWebContent(url);
			t3 = (new Date().getTime() - t1);
			System.out.println("t3: " + t3);
			if (t3 > 1000) {
				slowQueries.add(words.get(i) + " (" + t3 + ")");
			}
			if (min == 0) {
				min = max = t3;
			}
			if (t3 < min) {
				min = t3;
				words.get(i);
			}
			if (t3 > max) {
				max = t3;
				maxw = words.get(i);
			}
		}
		long time = (new Date().getTime() - t);
		System.out.println("[search] took " + (time / iterations) + " (" + min + "-" + max + ") " + maxw
				+ ", slow queries: " + slowQueries);
	}

	private void searchServiceSpeedTest() {
		Class<? extends BriefBean> clazz = BriefBean.class;

		try {
			long t = new Date().getTime();
			int i;
			long min = 0, max = 0, t1, t3;
			String maxw = "";
			List<String> slowQueries = new ArrayList<String>();

			for (i = 0; i < iterations; i++) {
				if (i > words.size() - 1) {
					break;
				}
				Query query = new Query(words.get(i)).setPageSize(12).setStart(0).addRefinement("YEAR:1981")
						.setParameter("facet.mincount", "1").setProduceFacetUnion(true).setAllowSpellcheck(false);

				t1 = new Date().getTime();
				searchService.search(clazz, query);
				searchService.suggestions(words.get(i), 10);
				t3 = (new Date().getTime() - t1);
				if (t3 > 1000) {
					slowQueries.add(words.get(i) + " (" + t3 + ")");
				}
				if (min == 0) {
					min = max = t3;
				}
				if (t3 < min) {
					min = t3;
				}
				if (t3 > max) {
					max = t3;
					maxw = words.get(i);
				}
			}
			System.out.println("[searchService] took " + ((new Date().getTime() - t) / iterations) + " (" + min + "-"
					+ max + ") " + maxw + ", slow queries: " + slowQueries);
		} catch (SolrTypeException e) {
			e.printStackTrace();
		}
	}

	private void getWebContent(String _url) {
		URL url;
		InputStream is = null;
		DataInputStream dis;
		String line;

		try {
			url = new URL(_url);
			is = url.openStream(); // throws an IOException
			dis = new DataInputStream(new BufferedInputStream(is));
			StringBuilder content = new StringBuilder();

			while ((line = dis.readLine()) != null) {
				// System.out.println(line);
				content.append(line);
			}
			// System.out.println(content.length());
		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException ioe) {
				// nothing to see here
			}
		}
	}

	// @Test
	public void refinementTest() {
		String[] numbers = new String[] { "1", "2", "3" };
		String[] words = new String[] { "one", "two", "three" };

		String[] all = (String[]) ArrayUtils.addAll(numbers, words);
		assertEquals(3, numbers.length);
		assertEquals(6, all.length);
		assertArrayEquals(new String[] { "1", "2", "3", "one", "two", "three" }, all);
		testFunction(all);
	}

	private void testFunction(String... strings) {
		// do nothing
	}

	// @Test
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
		params.put("query", new String[] { "*:*" });
		params.put("qf", new String[] { "general" });
		BriefBeanView briefBeanView;

		try {
			briefBeanView = SearchUtils.createResults(searchService, clazz, "portal", query, start, 12, params);
			model.setBriefBeanView(briefBeanView);
			int index = start;
			for (Object o : model.getBriefBeanView().getBriefBeans()) {
				assertTrue(o instanceof BriefBeanDecorator);
				BriefBeanDecorator bean = (BriefBeanDecorator) o;
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

	// @Test
	public void testFacetCreation() {
		SearchPage model = new SearchPage();
		model.setStart(1);

		Query query = new Query("haag");
		Class<? extends BriefBean> clazz = BriefBean.class;
		Map<String, String[]> params = new HashMap<String, String[]>();
		params.put("query", new String[] { "*:*" });
		// BriefBeanView briefBeanView;

		try {
			BriefBeanViewImpl briefBeanView = new BriefBeanViewImpl();

			ResultSet<? extends BriefBean> resultSet = searchService.search(clazz, query);
			briefBeanView.setBriefBeans(resultSet.getResults());
			List<FacetField> facetFields = resultSet.getFacetFields();
			for (FacetField field : facetFields) {
				System.out.println(field);
			}

			List<Facet> facets = ModelUtils.conventFacetList(facetFields);
			for (Facet facet : facets) {
				System.out.println(facet);
			}

			briefBeanView.makeQueryLinks(facets, query);

			for (FacetQueryLinks facet : briefBeanView.getFacetQueryLinks()) {
				if (facet.getType().equals("TYPE")) {
					assertEquals("&qf={!tag=TYPE}TYPE:IMAGE", facet.getLinks().get(0).getUrl());
				}
			}
		} catch (SolrTypeException e) {
			fail("SolrTypeException: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	private List<String> randomWords(int count) {

		List<String> queries = null;
		try {
			queries = FileUtils.readLines(new File("/home/peterkiraly/words.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<String> random = new ArrayList<String>();
		String query;
		long size = 0;
		while (random.size() < count) {
			int startRecord = (int) (Math.random() * queries.size());
			query = queries.get(startRecord).trim().replaceAll("[\"'()]", "").replace("/", "\\/");
			if (query.length() >= 3 && !random.contains(query)) {
				size += query.length();
				random.add(query);
			}
		}
		System.out.println("avarage size: " + ((float) size / random.size()));

		return random;
	}
}
