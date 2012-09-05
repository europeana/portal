package eu.europeana.portal2.web.controllers;

import static org.junit.Assert.*;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import eu.europeana.corelib.definitions.solr.model.Term;
import eu.europeana.corelib.solr.exceptions.EuropeanaQueryException;
import eu.europeana.corelib.solr.exceptions.SolrTypeException;
import eu.europeana.corelib.solr.service.SearchService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/servlet/portal2-mvc.xml", "/internal/portal2-development.xml"})
public class SuggestionControllerTest {

	@Resource
	private SearchService searchService;

	private ApplicationContext applicationContext;

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private HttpRequestHandlerAdapter handlerAdapter;
	private SuggestionController controller;

	@Test
	public void suggestionServiceTest() {
		try {
			List<Term> suggestions = searchService.suggestions("pari", 10);
			assertNotNull(suggestions);
			assertEquals(10, suggestions.size());
			for (Term term : suggestions) {
				System.out.println(StringUtils.join(
					new String[]{term.getField(), term.getTerm(), Long.toString(term.getFrequency())}, " // "));
			}
		} catch (SolrTypeException e) {
			e.printStackTrace();
		}
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
