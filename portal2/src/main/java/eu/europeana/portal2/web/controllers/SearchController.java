package eu.europeana.portal2.web.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.logging.Log;
import eu.europeana.corelib.logging.Logger;
import eu.europeana.corelib.solr.exceptions.SolrTypeException;
import eu.europeana.corelib.solr.service.SearchService;
import eu.europeana.corelib.solr.utils.SolrUtils;
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
		SearchPage model = new SearchPage();
		model.setRequest(request);
		model.setRefinements(qf);

		if (start < 1) {
			start = 1;
		}
		model.setStart(start);
		model.setRows(rows);

		q = SolrUtils.translateQuery(q);
		model.setQuery(q);

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
				.setAllowSpellcheck(false);

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
