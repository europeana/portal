package eu.europeana.portal2.web.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.util.WebUtils;

import eu.europeana.corelib.db.service.UserService;
import eu.europeana.corelib.definitions.db.entity.RelationalDatabase;
import eu.europeana.corelib.definitions.db.entity.relational.User;
import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.logging.Log;
import eu.europeana.corelib.logging.Logger;
import eu.europeana.corelib.solr.exceptions.SolrTypeException;
import eu.europeana.corelib.solr.service.SearchService;
import eu.europeana.corelib.solr.utils.SolrUtils;
import eu.europeana.corelib.utils.StringArrayUtils;
import eu.europeana.corelib.utils.model.LanguageVersion;
import eu.europeana.corelib.web.model.PageInfo;
import eu.europeana.corelib.web.model.rights.RightReusabilityCategorizer;
import eu.europeana.corelib.web.support.Configuration;
import eu.europeana.corelib.web.utils.RequestUtils;
import eu.europeana.portal2.services.ClickStreamLogService;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.SearchPage;
import eu.europeana.portal2.web.presentation.model.submodel.BriefBeanView;
import eu.europeana.portal2.web.util.ControllerUtil;
import eu.europeana.portal2.web.util.SearchUtils;

@Controller
public class SearchController {

	@Log
	private Logger log;

	@Resource
	private SearchService searchService;

	@Resource
	private InternalResourceViewResolver viewResolver;

	@Resource
	private Configuration config;

	@Resource
	private ClickStreamLogService clickStreamLogger;

	@Resource
	private UserService userService;

	private static final String PORTAL_LANGUAGE_COOKIE = "portalLanguage";
	private static final String SEARCH_LANGUAGES_COOKIE = "keywordLanguages";

	/**
	 * Possible sort options
	 */
	private final List<String> sortValues = Arrays.asList(new String[] { "score desc", "title desc", "date desc" });

	/**
	 * Deafult sort order
	 */
	public static final String DEFAULT_SORT = "score desc";

	@RequestMapping({ "/search.html", "/brief-doc.html" })
	public ModelAndView searchHtml(
			@RequestParam(value = "query", required = false, defaultValue = "*:*") String q,
			@RequestParam(value = "qf", required = false) String[] qf,
			@RequestParam(value = "qt", required = false) String[] qt,
			@RequestParam(value = "start", required = false, defaultValue = "1") int start,
			@RequestParam(value = "rows", required = false, defaultValue = "24") int rows,
			@RequestParam(value = "sort", required = false, defaultValue = "") String sort,
			@RequestParam(value = "profile", required = false, defaultValue = "portal") String profile,
			HttpServletRequest request, Locale locale) {

		rows = Math.min(rows, config.getRowLimit());

		Map<String, String[]> params = RequestUtils.getParameterMap(request);
		// workaround of a Spring issue (https://jira.springsource.org/browse/SPR-7963)
		if (params.get("qf") != null && params.get("qf").length != qf.length) {
			qf = params.get("qf");
		}
		if (params.get("qt") != null && params.get("qt").length != qt.length) {
			qt = params.get("qt");
		}
		SearchPage model = new SearchPage();
		model.setRequest(request);
		model.setRefinements(qf);

		if (start < 1) {
			start = 1;
		}
		model.setStart(start);
		model.setRows(rows);

		q = SolrUtils.rewriteQueryFields(q);
		model.setQuery(q);

		List<LanguageVersion> queryTranslations = null;
		if (StringArrayUtils.isNotBlank(qt)) {
			if (!preventQueryTranslation(qt)) {
				queryTranslations = parseQueryTranslations(qt);
			}
		} else {
			List<String> translatableLanguages = getTranslatableLanguages(request);
			if (translatableLanguages != null) {
				queryTranslations = SolrUtils.translateQuery(q, translatableLanguages);
			}
		}
		model.setQueryTranslations(queryTranslations);

		if (!sortValues.contains(sort)) {
			sort = DEFAULT_SORT;
		}
		// TODO: the sorting is not solved yet in Solr, so we maintain only one sorting option regardless
		// what the user sets.
		// REMOVE THIS LINE AS SOON AS POSSIBLE, but not earlier ;-)
		sort = DEFAULT_SORT;
		model.setSort(sort);

		PageInfo view = PortalPageInfo.SEARCH_HTML;
		ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, view);

		Query query = new Query(q)
				.setRefinements(qf)
				.setPageSize(rows)
				.setStart(start - 1) // Solr starts from 0
				.setParameter("facet.mincount", "1") // .setParameter("f.YEAR.facet.mincount", "1")
				.setParameter("sort", sort)
				.setProduceFacetUnion(true)
				.setAllowSpellcheck(false)
				.setQueryTranslations(queryTranslations)
				;

		if (model.isEmbedded()) {
			query.setAllowFacets(false);
			query.setAllowSpellcheck(false);
		}

		RightReusabilityCategorizer.setPermissionStrategy(RightReusabilityCategorizer.PERMISSION_STRATEGY_NEGATIVE_ALL);
		query.setValueReplacements(RightReusabilityCategorizer.mapValueReplacements(qf));
		query.setFacetQueries(RightReusabilityCategorizer.getQueryFacets());

		Class<? extends BriefBean> clazz = BriefBean.class;

		if (log.isDebugEnabled()) {
			log.debug("query: " + query);
		}
		BriefBeanView briefBeanView = null;
		try {
			briefBeanView = SearchUtils.createResults(searchService, clazz, profile, query, start, rows, params);
			model.setBriefBeanView(briefBeanView);
			if (log.isDebugEnabled()) {
				log.debug("NumFound: " + briefBeanView.getPagination().getNumFound());
			}
			model.setEnableRefinedSearch(briefBeanView.getPagination().getNumFound() > 0);
		} catch (SolrTypeException e) {
			log.error("SolrTypeException: " + e.getMessage());
			// return new ApiError("search.json", e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			log.error("Exception: " + e.getMessage());
			e.printStackTrace();
		}

		clickStreamLogger.logBriefResultView(request, briefBeanView, query, page);
		return page;
	}

	private boolean preventQueryTranslation(String[] qt) {
		return qt.length == 1 && StringUtils.equals(qt[0], "false");
	}

	private List<LanguageVersion> parseQueryTranslations(String[] qt) {
		List<LanguageVersion> queryTranslations = new ArrayList<LanguageVersion>();
		for (String term : qt) {
			String[] parts = term.split(":", 2);
			queryTranslations.add(new LanguageVersion(parts[1], parts[0]));
		}
		return queryTranslations;
	}

	private List<String> getTranslatableLanguages(HttpServletRequest request) {
		User user = ControllerUtil.getUser(userService);
		List<String> languageCodes = getKeywordLanguages(request, user);
		String portalLanguage = getPortalLanguage(request, user);
		if (!languageCodes.contains(portalLanguage)) {
			languageCodes.add(portalLanguage);
		}
		return languageCodes;
	}

	private List<String> getKeywordLanguages(HttpServletRequest request,
			User user) {
		List<String> languageCodes = new ArrayList<String>();
		String rawLanguageCodes = null;
		if (user != null) {
			rawLanguageCodes = StringUtils.join(
					user.getLanguageSearch(), RelationalDatabase.SEARCH_LANGUAGES_SEPARATOR);
		} else {
			Cookie cookie = WebUtils.getCookie(request, SEARCH_LANGUAGES_COOKIE);
			if (cookie != null) {
				rawLanguageCodes = cookie.getValue();
			}
		}
		if (rawLanguageCodes != null) {
			languageCodes.addAll(Arrays.asList(
					StringUtils.split(rawLanguageCodes.trim(), RelationalDatabase.SEARCH_LANGUAGES_SEPARATOR)));
		}
		return languageCodes;
	}

	private String getPortalLanguage(HttpServletRequest request, User user) {
		String languageCode = null;
		if (user != null) {
			languageCode = user.getLanguagePortal();
		} else {
			Cookie cookie = WebUtils.getCookie(request, PORTAL_LANGUAGE_COOKIE);
			if (cookie != null) {
				languageCode = cookie.getValue();
			}
		}
		return languageCode;
	}

	private boolean hasReusabilityFilter(String[] qf) {
		boolean hasReusability = false;
		if (qf != null) {
			for (String filter : qf) {
				if (StringUtils.contains(filter, "REUSABILITY:")) {
					hasReusability = true;
					break;
				}
			}
		}
		return hasReusability;
	}

	public InternalResourceViewResolver getViewResolver() {
		return viewResolver;
	}

	public void setViewResolver(InternalResourceViewResolver viewResolver) {
		this.viewResolver = viewResolver;
	}
}
