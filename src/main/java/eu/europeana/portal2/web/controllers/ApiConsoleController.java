package eu.europeana.portal2.web.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.portal2.services.Configuration;
import eu.europeana.portal2.web.controllers.utils.ApiResult;
import eu.europeana.portal2.web.controllers.utils.ApiWrapper;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.ApiConsolePage;
import eu.europeana.portal2.web.util.ControllerUtil;
import eu.europeana.portal2.web.util.Injector;
import eu.europeana.portal2.web.util.JsonFormatter;

/**
 * Controller for Api playground.
 * 
 * API playground is an interactive form, where user can play with API parameters
 * 
 * @author peter.kiraly@kb.nl
 */
@Controller
public class ApiConsoleController {

	@Resource(name="configurationService") private Configuration config;

	private static final String SEARCH = "search";
	private static final String RECORD = "record";

	private final Logger log = Logger.getLogger(getClass().getName());

	@RequestMapping(value = "/api/console.html", produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView playground(
			@RequestParam(value = "function", required = false, defaultValue="search") String function,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "query", required = false) String query,
			@RequestParam(value = "qf", required = false) String[] refinements,
			@RequestParam(value = "profile", required = false, defaultValue="standard") String profile,
			@RequestParam(value = "start", required = false, defaultValue="1") int start,
			@RequestParam(value = "rows", required = false, defaultValue="12") int rows,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "collectionId", required = false) String collectionId,
			@RequestParam(value = "recordId", required = false) String recordId,
			@RequestParam(value = "phrases", required = false, defaultValue="false") boolean phrases, // 0, no, false, 1 yes, true
			HttpServletRequest request,
			HttpServletResponse response, 
			Locale locale) {
		Injector injector = new Injector(request, response, locale);
		log.info("===== /api/console.html =====");

		ApiConsolePage model = new ApiConsolePage();
		injector.injectProperties(model);

		if (!model.getSupportedFunctions().contains(function)) {
			function = SEARCH;
		}

		refinements = clearRefinements(refinements);
		if (function.equals(SEARCH) && !model.getDefaultSearchProfiles().contains(profile)) {
			profile = "standard";
		}

		if (function.equals(RECORD) && !model.getDefaultObjectProfiles().contains(profile)) {
			profile = "full";
		}

		if (!model.getDefaultRows().contains(Integer.toString(rows))) {
			rows = 12;
		}

		model.setFunction(function);
		model.setQuery(query);
		model.setRefinements(refinements);
		model.setProfile(profile);
		model.setStart(start);
		model.setRows(rows);
		model.setSort(sort);
		model.setCollectionId(collectionId);
		model.setRecordId(recordId);
		model.setPhrases(phrases);
		model.setCallback(callback);

		ApiWrapper api = new ApiWrapper(config.getApi2url(), config.getApi2key(), config.getApi2secret(), request.getSession());
		
		ApiResult apiResult = null;
		// "";
		if (function.equals("search") && !StringUtils.isBlank(query)) {
			apiResult = api.getSearchResult(query, refinements, profile, start, rows, sort, callback);
		} else if (function.equals("record") && !StringUtils.isBlank(collectionId) && !StringUtils.isBlank(recordId)) {
			apiResult = api.getObject(collectionId, recordId, profile, callback);
		} else if (function.equals("record") && StringUtils.isBlank(collectionId) && !StringUtils.isBlank(recordId)) {
			apiResult = api.getObject(recordId, profile, callback);
		} else if (function.equals("suggestions") && !StringUtils.isBlank(query)) {
			apiResult = api.getSuggestions(query, rows, phrases, callback);
		}

		String rawJsonString = apiResult.getContent();
		String niceJsonString = rawJsonString;
		niceJsonString = JsonFormatter.format(rawJsonString);

		model.setJsonString(niceJsonString);
		model.setApiUrl(api.getUrl());
		model.setHttpStatusCode(apiResult.getHttpStatusCode());
		log.info("API URL: " + model.getApiUrl());

		ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.API_CONCOLE);
		injector.postHandle(this, page);

		return page;
	}

	/**
	 * Clear out empty values
	 *
	 * @param list
	 *   The original list
	 * @return
	 *   The cleared list
	 */
	private String[] clearRefinements(String[] list) {
		if (ArrayUtils.isEmpty(list)) {
			return list;
		}
		List<String> cleared = new ArrayList<String>();
		for (String str : list) {
			if (!StringUtils.isBlank(str)) {
				cleared.add(str);
			}
		}
		return cleared.toArray(new String[cleared.size()]);
	}
}
