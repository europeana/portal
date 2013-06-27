package eu.europeana.portal2.web.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonSyntaxException;

import eu.europeana.corelib.logging.Log;
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

	@Log
	private Logger log;

	@Resource(name = "configurationService")
	private Configuration config;

	private static final String SEARCH = "search";
	private static final String RECORD = "record";

	@RequestMapping(value = "/api/console.html", produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView playground(
			@RequestParam(value = "function", required = false, defaultValue = "search") String function,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "query", required = false) String query,
			@RequestParam(value = "qf", required = false) String[] refinements,
			@RequestParam(value = "profile", required = false) String[] profile,
			@RequestParam(value = "start", required = false, defaultValue = "1") int start,
			@RequestParam(value = "rows", required = false, defaultValue = "12") int rows,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "collectionId", required = false) String collectionId,
			@RequestParam(value = "recordId", required = false) String recordId,
			@RequestParam(value = "phrases", required = false, defaultValue = "false") boolean phrases, // 0, no, false,
																										// 1 yes, true
			@RequestParam(value = "latMin", required = false) String latMin, // spatial search values
			@RequestParam(value = "latMax", required = false) String latMax,
			@RequestParam(value = "longMin", required = false) String longMin,
			@RequestParam(value = "longMax", required = false) String longMax,
			@RequestParam(value = "yearMin", required = false) String yearMin, // temporal search values
			@RequestParam(value = "yearMax", required = false) String yearMax, HttpServletRequest request,
			HttpServletResponse response, Locale locale) {
		Injector injector = new Injector(request, response, locale);
		log.info("===== /api/console.html =====");
		// workaround of a Spring issue (https://jira.springsource.org/browse/SPR-7963)
		String[] _qf = (String[]) request.getParameterMap().get("qf");
		if (_qf != null && _qf.length != refinements.length) {
			refinements = _qf;
		}

		String[] _profile = (String[]) request.getParameterMap().get("profile");
		if (_profile != null && _profile.length != profile.length) {
			profile = _profile;
		}

		ApiConsolePage model = new ApiConsolePage();
		injector.injectProperties(model);

		if (!model.getSupportedFunctions().contains(function)) {
			function = SEARCH;
		}

		refinements = clearRefinements(refinements);

		List<String> profiles = ArrayUtils.isEmpty(profile) ? new ArrayList<String>() : Arrays.asList(profile);
		if (function.equals(SEARCH)) {
			checkProfiles(profiles, "standard", model.getDefaultSearchProfiles());
		}

		if (function.equals(RECORD)) {
			checkProfiles(profiles, "full", model.getDefaultObjectProfiles());
		}

		if (!model.getDefaultRows().contains(Integer.toString(rows))) {
			rows = 12;
		}

		if (!StringUtils.isBlank(recordId)) {
			recordId = recordId.trim();
			if (recordId.startsWith("\"")) {
				recordId = recordId.substring(1);
			}
			if (recordId.endsWith("\"")) {
				recordId = recordId.substring(0, recordId.length() - 1);
			}
		}

		model.setFunction(function);
		model.setQuery(query);
		model.setRefinements(refinements);
		model.setProfile(StringUtils.join(profile, " "));
		model.setStart(start);
		model.setRows(rows);
		model.setSort(sort);
		model.setCollectionId(collectionId);
		model.setRecordId(recordId);
		model.setPhrases(phrases);
		model.setCallback(callback);
		model.setLatMin(latMin);
		model.setLatMax(latMax);
		model.setLongMin(longMin);
		model.setLongMax(longMax);
		model.setYearMin(yearMin);
		model.setYearMax(yearMax);

		model.setProfiles(profiles);

		if (function.equals(SEARCH)) {
			if (!StringUtils.isBlank(latMin) || !StringUtils.isBlank(latMax)) {
				if (StringUtils.isBlank(latMin)) {
					latMin = "*";
				}
				if (StringUtils.isBlank(latMax)) {
					latMax = "*";
				}
				refinements = (String[]) ArrayUtils.add(refinements, "pl_wgs84_pos_lat:[" + checkSpatial(latMin)
						+ " TO " + checkSpatial(latMax) + "]");
			}
			if (!StringUtils.isBlank(longMin) || !StringUtils.isBlank(longMax)) {
				if (StringUtils.isBlank(longMin)) {
					longMin = "*";
				}
				if (StringUtils.isBlank(longMax)) {
					longMax = "*";
				}
				refinements = (String[]) ArrayUtils.add(refinements, "pl_wgs84_pos_long:[" + checkSpatial(longMin)
						+ " TO " + checkSpatial(longMax) + "]");
			}
			if (!StringUtils.isBlank(yearMin) || !StringUtils.isBlank(yearMax)) {
				if (StringUtils.isBlank(yearMin)) {
					yearMin = "*";
				}
				if (StringUtils.isBlank(yearMax)) {
					yearMax = "*";
				}
				refinements = (String[]) ArrayUtils.add(refinements, "YEAR:[" + checkSpatial(yearMin) + " TO "
						+ checkSpatial(yearMax) + "]");
			}
			if (!ArrayUtils.isEmpty(refinements) && StringUtils.isBlank(query)) {
				query = "*:*";
			}
		}

		ApiWrapper api = new ApiWrapper(config.getApi2url(), config.getApi2key(), config.getApi2secret(),
				request.getSession());
		ApiResult apiResult = null;
		if (function.equals(SEARCH) && !StringUtils.isBlank(query)) {
			apiResult = api.getSearchResult(query, refinements, StringUtils.join(profile, "%20"), start, rows, sort,
					callback);
		} else if (function.equals("record") && !StringUtils.isBlank(collectionId) && !StringUtils.isBlank(recordId)) {
			apiResult = api.getObject(collectionId, recordId, StringUtils.join(profile, "%20"), callback);
		} else if (function.equals("record") && StringUtils.isBlank(collectionId) && !StringUtils.isBlank(recordId)) {
			apiResult = api.getObject(recordId, StringUtils.join(profile, "%20"), callback);
		} else if (function.equals("suggestions") && !StringUtils.isBlank(query)) {
			apiResult = api.getSuggestions(query, rows, phrases, callback);
		}

		if (apiResult != null) {
			String rawJsonString = apiResult.getContent();
			String niceJsonString = rawJsonString;
			if (StringUtils.isBlank(callback)) {
				try {
					niceJsonString = JsonFormatter.format(rawJsonString);
				} catch (JsonSyntaxException e) {
					log.error("JSON formatting exception: " + e.getMessage());
				}
			}

			model.setJsonString(niceJsonString);
			model.setApiUrl(api.getUrl());
			model.setHttpStatusCode(apiResult.getHttpStatusCode());
		}

		ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.API_CONCOLE);
		injector.postHandle(this, page);

		return page;
	}

	/**
	 * Clear out empty values
	 * 
	 * @param list
	 *            The original list
	 * @return The cleared list
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

	private String checkSpatial(String spatial) {
		return spatial;
		/*
		 * final Pattern pattern = Pattern.compile("^-?\\d(\\d*(\\.\\d+)?)?$"); if (pattern.matcher(spatial).matches())
		 * { return spatial; } return "*";
		 */
	}

	private void checkProfiles(List<String> profiles, String defaultProfile, Map<String, Boolean> allowedValues) {
		boolean hasValid = false;
		for (String profile : profiles) {
			if (allowedValues.containsKey(profile)) {
				allowedValues.put(profile, true);
				hasValid = true;
			}
		}
		if (!hasValid) {
			allowedValues.put(defaultProfile, true);
		}
	}
}
