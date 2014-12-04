package eu.europeana.portal2.web.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.definitions.exception.ProblemType;
import eu.europeana.corelib.definitions.solr.model.Term;
import eu.europeana.corelib.edm.exceptions.EuropeanaQueryException;
import eu.europeana.corelib.edm.exceptions.SolrTypeException;
import eu.europeana.corelib.edm.service.SearchService;
import eu.europeana.corelib.logging.Log;
import eu.europeana.corelib.logging.Logger;
import eu.europeana.portal2.services.ClickStreamLogService;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.SuggestionsPage;
import eu.europeana.portal2.web.util.ControllerUtil;

@Controller
public class SuggestionController {

	@Log
	private Logger log;

	@Resource
	private SearchService searchService;

	@Resource
	private ClickStreamLogService clickStreamLogger;

	@RequestMapping("/suggestions.json")
	public ModelAndView suggestionHtml(@RequestParam(value = "term", required = false, defaultValue = "") String term,
			@RequestParam(value = "size", required = false, defaultValue = "10") int size,
			@RequestParam(value = "field", required = false, defaultValue = "") String field
			) throws EuropeanaQueryException {

		if (term == null) {
			throw new EuropeanaQueryException(ProblemType.MALFORMED_QUERY);
		}

		List<Term> suggestions = new ArrayList<Term>();
		term = clearSuggestionTerm(term);
		if (term.length() >= 3 && term.indexOf(":") == -1) {
			try {
				suggestions = searchService.suggestions(term, size, field);
			} catch (SolrTypeException e) {
				log.error("SolrTypeException: " + e.getMessage(),e);
			}
		}

		// log.fine("number of suggestions: " + suggestions.size());
		SuggestionsPage model = new SuggestionsPage();
		model.setResults(suggestions);

		return ControllerUtil.createModelAndViewPage(model, PortalPageInfo.AJAX_SUGGESTION);
	}

	private String clearSuggestionTerm(String term) {
		term = term.trim().replaceAll("[\"'()]", "").replace("/", "\\/");
		if (term.endsWith(" AND") || term.endsWith(" NOT")) {
			term = term.substring(0, term.length() - 4);
		}
		if (term.endsWith(" OR")) {
			term = term.substring(0, term.length() - 3);
		}
		return term;
	}
}
