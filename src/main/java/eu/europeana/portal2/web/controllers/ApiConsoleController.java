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
import org.json.JSONException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.portal2.services.Configuration;
import eu.europeana.portal2.web.controllers.utils.ApiWrapper;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.ApiConsolePage;
import eu.europeana.portal2.web.util.ControllerUtil;
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

	private final Logger log = Logger.getLogger(getClass().getName());

	@RequestMapping(value = "/api/console.html", produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView playground(
			@RequestParam(value = "function", required = false, defaultValue="search") String function,
			@RequestParam(value = "query", required = false) String query,
			@RequestParam(value = "qf", required = false) String[] refinements,
			@RequestParam(value = "profile", required = false, defaultValue="standard") String profile,
			@RequestParam(value = "start", required = false, defaultValue="1") int start,
			@RequestParam(value = "rows", required = false, defaultValue="12") int rows,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "collectionId", required = false) String collectionId,
			@RequestParam(value = "recordId", required = false) String recordId,
			HttpServletRequest request,
			HttpServletResponse response, 
			Locale locale) {
		config.registerBaseObjects(request, response, locale);
		ApiConsolePage model = new ApiConsolePage();
		config.injectProperties(model);

		if (!model.getDefaultFunctions().contains(function)) {
			function = "search";
		}

		refinements = clearRefinements(refinements);
		if (!model.getDefaultProfiles().contains(profile)) {
			profile = "standard";
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

		ApiWrapper api = new ApiWrapper(config.getApi2url(), config.getApi2key(), config.getApi2secret(), request.getSession());
		String rawJsonString = "";
		if (function.equals("search") && !StringUtils.isBlank(query)) {
			rawJsonString = api.getSearchResult(query, refinements, profile, start, rows, sort);
		} else if (function.equals("record") && !StringUtils.isBlank(collectionId) && !StringUtils.isBlank(recordId)) {
			rawJsonString = api.getObject(collectionId, recordId);
		} else if (function.equals("record") && StringUtils.isBlank(collectionId) && !StringUtils.isBlank(recordId)) {
			rawJsonString = api.getObject(recordId);
		}

		String niceJsonString = rawJsonString;
		try {
			niceJsonString = JsonFormatter.format(rawJsonString);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		model.setJsonString(niceJsonString);
		model.setApiUrl(api.getUrl());

		ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.API_CONCOLE);
		config.postHandle(this, page);

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
