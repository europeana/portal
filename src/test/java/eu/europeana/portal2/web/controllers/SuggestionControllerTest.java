package eu.europeana.portal2.web.controllers;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import eu.europeana.corelib.definitions.solr.model.Term;
import eu.europeana.corelib.solr.exceptions.SolrTypeException;
import eu.europeana.corelib.solr.service.SearchService;
import eu.europeana.portal2.web.controllers.speed.SpeedTestUtils;
import eu.europeana.portal2.web.controllers.speed.TermProvider;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/servlet/portal2-mvc.xml", "/internal/portal2-development.xml"})
public class SuggestionControllerTest {

	@Resource private SearchService searchService;

	// private String baseUrl = "http://europeana-ese2edm.isti.cnr.it:9191/solr/search/select?&wt=javabin&rows=0&version=2&";
	private String baseUrl = "http://10.101.38.1:9595/solr/";

	private HttpSolrServer solrServer = null;

	private int cents = 3;
	private int iterations = 100 * cents;

	private List<String> words;

	// @Inject
	private ApplicationContext applicationContext;

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private HttpRequestHandlerAdapter handlerAdapter;
	private SuggestionController controller;

	@Test
	public void allSpeedTest() {
		words = TermProvider.getRandomWords(iterations);
		solrJSpeedTest();
		suggestionServiceSpeedTest();
		restSpeedTest();
		// solrSpeedTest();
	}

	// @Test
	public void escapingTest() {
		String[] terms = new String[]{"'baking in bavaria", "\"baking in bavaria", "(baking in bavaria", "(baking in bavaria)"};
		for (String term : terms) {
			term = clearSuggestionTerm(term);
			assertEquals("baking in bavaria", term);
		}

		terms = new String[]{"text/image"};
		for (String term : terms) {
			term = clearSuggestionTerm(term);
			assertEquals("text\\/image", term);
		}

	}
	private String clearSuggestionTerm(String term) {
		term = term.replaceAll("[\"'()]", "").replace("/", "\\/");

		return term;
	}

	// @Test
	public void suggestionServiceTest() {
		try {
			List<Term> suggestions = searchService.suggestions("pari", 10);
			assertNotNull(suggestions);
			// TODO: change it when it is OK.
			assertEquals(10, suggestions.size());
			for (Term term : suggestions) {
				System.out.println(StringUtils.join(
					new String[]{term.getField(), term.getTerm(), Long.toString(term.getFrequency())}, " // "));
			}
		} catch (SolrTypeException e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void suggestionServiceSpeedTest() {
		try {
			long t = new Date().getTime();
			int i;
			long min = 0, max = 0, t1, t3;
			String maxw = "";
			List<String> slowQueries = new ArrayList<String>();

			for (i = 0; i<iterations; i++) {
				if (i > words.size()-1) {
					break;
				}
				t1 = new Date().getTime();
				List<Term> suggestions = searchService.suggestions(words.get(i), 10);
				t3 = (new Date().getTime() - t1);
				if (t3 > 1000) {
					slowQueries.add(words.get(i) + " (" + t3 + ")");
				}
				if (min == 0) {min = max = t3;}
				if (t3 < min) {min = t3;}
				if (t3 > max) {max = t3; maxw = words.get(i);}
			}
			long time = (new Date().getTime() - t);
			System.out.println("[searchService] took " + ((new Date().getTime() - t)/cents) 
					+ " (" + min + "-" + max + ") " + maxw
					+ ", slow queries: " + slowQueries);
		} catch (SolrTypeException e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void solrJSpeedTest() {

		long t = new Date().getTime();
		int i;
		long total = 0;
		long min = 0, max = 0, t1, t3, time, tmax = 0, tmin = 0;
		String maxw = "", tmaxw = "";
		List<String> slowQueries = new ArrayList<String>();
		Map<String, String> fields = new HashMap<String, String>(){{
			put("who", "suggestWho");
			put("title", "suggestTitle");
			put("what", "suggestWhat");
			put("where", "suggestWhere");
			put("when", "suggestWhen");
		}};
		for (i = 0; i<iterations; i++) {
			if (i > words.size()-1) {
				break;
			}
			t1 = new Date().getTime();
			for (Map.Entry<String, String> field : fields.entrySet()) {
				time = getSuggestions(words.get(i), field.getKey(), field.getValue());
				total += time;
				if (time > 1000) {
					slowQueries.add(field.getKey() + ":" + words.get(i) + " (" + time + ")");
				}
				if (tmax == 0) {tmax = tmin = time;}
				if (time > tmax) {tmax = time; tmaxw = field.getKey() + ":" + words.get(i);}
				if (time < tmin) {tmin = time;}
			}
			t3 = (new Date().getTime() - t1);
			if (min == 0) {min = max = t3;}
			if (t3 < min) {min = t3;}
			if (t3 > max) {max = t3; maxw = words.get(i) + " (" + t3 + ")";}
		}
		System.out.println("[solrJ] took " + ((new Date().getTime() - t)/cents) 
				+ " -- internal part: " + (total/cents) 
				+ " (" + min + "-" + max + ") " + maxw
				+ " (" + tmin + "-" + tmax + ") " + tmaxw
				+ ", slow queries: " + slowQueries
		);
	}

	private long getSuggestions(String query, String field, String rHandler) {
		if (solrServer == null) {
			solrServer = new HttpSolrServer("http://10.101.38.1:9595/solr/search");
			solrServer.setFollowRedirects(false);
			solrServer.setConnectionTimeout(60000);
			solrServer.setDefaultMaxConnectionsPerHost(64);
			solrServer.setMaxTotalConnections(125);
		}

		ModifiableSolrParams params = new ModifiableSolrParams();
		params.set("qt", "/" + rHandler);
		params.set("q", field + ":" + query);
		params.set("rows", 0);

		try {
			if (solrServer != null) {
				QueryResponse qResp = solrServer.query(params);
				return (Integer)qResp.getHeader().get("QTime");
				// return qResp.getElapsedTime();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// @Test
	public void restSpeedTest() {
		String baseUrl = "http://localhost:8080/portal/suggestions.json?term=%s";

		long t = new Date().getTime();
		long min = 0, max = 0, t1, t3;
		String minw = "", maxw = "";
		int i;
		List<String> slowQueries = new ArrayList<String>();
		for (i = 0; i<iterations; i++) {
			if (i > words.size()-1) {
				break;
			}
			String url = String.format(baseUrl, words.get(i));
			// System.out.println(url);
			t1 = new Date().getTime();
			SpeedTestUtils.getWebContent(url);
			t3 = (new Date().getTime() - t1);
			if (t3 > 1000) {
				slowQueries.add(words.get(i) + " (" + t3 + ")");
			}
			if (min == 0) {min = max = t3;}
			if (t3 < min) {min = t3; minw = words.get(i);}
			if (t3 > max) {max = t3; maxw = words.get(i);}
		}
		long time = (new Date().getTime() - t);
		System.out.println("[suggestion] took " + (time/cents) + " (" + min + "-" + max + ") " + maxw
				+ ", slow queries: " + slowQueries);
	}

	// @Test
	public void solrSpeedTest() {
		String path = "search/select?&wt=javabin&rows=0&version=2&";
		String[] urls = new String[]{
			"qt=/suggestTitle&q=title:",
			"qt=/suggestWho&q=who:",
			"qt=/suggestWhat&q=what:",
			"qt=/suggestWhere&q=where:",
			"qt=/suggestWhen&q=when:"
		};

		long t = new Date().getTime();
		int i;
		for (i = 0; i<iterations; i++) {
			if (i > words.size()-1) {
				break;
			}
			if (StringUtils.isBlank(words.get(i))) {
				continue;
			}
			for (String params : urls) {
				String url = baseUrl + path + params + words.get(i);
				// System.out.println(url);
				SpeedTestUtils.getWebContent(url);
			}
		}
		long time = (new Date().getTime() - t);
		System.out.println("[solr] took " + (time/cents));
	}

	// @Test
	public void solrSpeedTestTitle() {

		String[] urls = new String[]{
			"qt=/suggestTitle&q=title:%s",
		};

		long t = new Date().getTime();
		int i;
		for (i = 0; i<iterations; i++) {
			if (i > words.size()-1) {
				break;
			}
			for (String params : urls) {
				String url = baseUrl + params + words.get(i);
				// System.out.println(url);
				SpeedTestUtils.getWebContent(url);
			}
		}
		long time = (new Date().getTime() - t);
		System.out.println("[title] took " + (time/cents));
	}

	// TODO: find a way to make controller tests working. This is not working right now.
	// @Test
	public void suggestionControllerTest() {
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		// applicationContext = 
		applicationContext = new ClassPathXmlApplicationContext(
				new String[]{"/servlet/portal2-mvc.xml", "/internal/portal2-development.xml"});
		handlerAdapter = applicationContext.getBean(HttpRequestHandlerAdapter.class);
		controller = new SuggestionController();
		request.addParameter("term", "paris");
		request.addParameter("size", "10");
		request.setRequestURI("/suggestion.json");
		try {
			final ModelAndView mav = handlerAdapter.handle(request, response, controller);
			assertNotNull(mav);
			assertTrue(mav.getModelMap().containsKey("results"));
			List<String> results = (List<String>)mav.getModelMap().get("results");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			fail(e1.getMessage());
			e1.printStackTrace();
		}
	}
}
