package eu.europeana.portal2.web.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import eu.europeana.portal2.web.util.ControllerUtil;

@Controller
public class SuggestionController {

	private final Logger log = Logger.getLogger(getClass().getName());

	@Resource private SearchService searchService;

	@Resource(name="configurationService") private Configuration config;

	@RequestMapping("/suggestions.json")
	public ModelAndView suggestionHtml(
			@RequestParam(value = "term", required = false, defaultValue="") String term,
			@RequestParam(value = "size", required = false, defaultValue="10") int size,
			HttpServletRequest request,
			HttpServletResponse response) 
					throws EuropeanaQueryException {
		if (term == null) {
			throw new EuropeanaQueryException(ProblemType.MALFORMED_QUERY);
		}

		log.info("============== START SUGGESTIONS ==============");

		List<Term> suggestions = null;
		try {
			suggestions = searchService.suggestions(term, size);
		} catch (SolrTypeException e) {
			log.severe("SolrTypeException: " + e.getMessage());
			e.printStackTrace();
		}

		ArrayList<String> formattedSuggestion = new ArrayList<String>();
		if (suggestions != null) {
			for (Term suggestion : suggestions) {
				formattedSuggestion.add(suggestion.getTerm());
			}
		}

		log.info("formattedSuggestion: " + StringUtils.join(formattedSuggestion, ", "));
		SuggestionsPage model = new SuggestionsPage();
		model.setResults(formattedSuggestion);
		config.injectProperties(model, request);

		return ControllerUtil.createModelAndViewPage(model, PortalPageInfo.AJAX_SUGGESTION);
	}
}
