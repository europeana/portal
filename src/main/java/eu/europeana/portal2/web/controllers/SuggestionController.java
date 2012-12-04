package eu.europeana.portal2.web.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.definitions.exception.ProblemType;
import eu.europeana.corelib.definitions.solr.model.Term;
import eu.europeana.corelib.solr.exceptions.EuropeanaQueryException;
import eu.europeana.corelib.solr.exceptions.SolrTypeException;
import eu.europeana.corelib.solr.service.SearchService;
import eu.europeana.portal2.services.Configuration;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.SuggestionsPage;
import eu.europeana.portal2.web.util.ClickStreamLogger;
import eu.europeana.portal2.web.util.ControllerUtil;
import eu.europeana.portal2.web.util.Injector;

@Controller
public class SuggestionController {

	private final Logger log = Logger.getLogger(getClass().getName());

	@Resource private SearchService searchService;

	@Resource(name="configurationService") private Configuration config;

	@Resource private ClickStreamLogger clickStreamLogger;

	@RequestMapping("/suggestions.json")
	public ModelAndView suggestionHtml(
			@RequestParam(value = "term", required = false, defaultValue="") String term,
			@RequestParam(value = "size", required = false, defaultValue="10") int size,
			@RequestParam(value = "field", required = false, defaultValue="") String field,
			HttpServletRequest request,
			HttpServletResponse response,
			Locale locale)
					throws EuropeanaQueryException {
		Injector injector = new Injector(request, response, locale);

		if (term == null) {
			throw new EuropeanaQueryException(ProblemType.MALFORMED_QUERY);
		}

		log.info("============== START SUGGESTIONS ==============");

		List<Term> suggestions = new ArrayList<Term>();
		term = clearSuggestionTerm(term);
		if (term.length() >= 3 && term.indexOf(":") == -1) {
			try {
				suggestions = searchService.suggestions(term, size, field);
			} catch (SolrTypeException e) {
				log.severe("SolrTypeException: " + e.getMessage());
				e.printStackTrace();
			}
		}

		log.info("number of suggestions: " + suggestions.size());
		SuggestionsPage model = new SuggestionsPage();
		model.setResults(suggestions);
		injector.injectProperties(model);

		ModelAndView page = ControllerUtil.createModelAndViewPage(model, PortalPageInfo.AJAX_SUGGESTION);
		injector.postHandle(this, page);
		return page;
	}
	
	private String clearSuggestionTerm(String term) {
		term = term.trim().replaceAll("[\"'()]", "").replace("/", "\\/");
		return term;
	}

}
