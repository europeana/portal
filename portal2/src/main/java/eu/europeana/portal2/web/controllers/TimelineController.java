package eu.europeana.portal2.web.controllers;

import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.solr.service.SearchService;
import eu.europeana.corelib.web.support.Configuration;
import eu.europeana.corelib.web.utils.RequestUtils;
import eu.europeana.portal2.services.ClickStreamLogService;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.SearchPageEnum;
import eu.europeana.portal2.web.presentation.model.SearchPage;
import eu.europeana.portal2.web.presentation.model.submodel.BriefBeanView;
import eu.europeana.portal2.web.util.ControllerUtil;
import eu.europeana.portal2.web.util.SearchUtils;

@Controller
public class TimelineController {

	@Resource
	private SearchService searchService;

	@Resource
	private ClickStreamLogService clickStreamLogger;

	@Resource
	private Configuration config;

	@RequestMapping("/timeline.html")
	public ModelAndView timelineHtml(@RequestParam(value = "query", required = false) String query,
			@RequestParam(value = "embedded", required = false) String embedded,
			@RequestParam(value = "start", required = false, defaultValue = "1") int start,
			@RequestParam(value = "startPage", required = false, defaultValue = "1") int startPage,
			@RequestParam(value = "qf", required = false) String[] qf,
			@RequestParam(value = "rq", required = false) String rq,
			HttpServletRequest request, Locale locale) throws Exception {
		// workaround of a Spring issue (https://jira.springsource.org/browse/SPR-7963)
		String[] _qf = (String[]) request.getParameterMap().get("qf");
		if (_qf != null && _qf.length != qf.length) {
			qf = _qf;
		}

		SearchPage model = new SearchPage();
		model.setCurrentSearch(SearchPageEnum.TIMELINE);
		model.setEmbedded(StringUtils.equalsIgnoreCase(embedded, "true"));
		model.setQuery(query);
		model.setStart(start);
		model.setStartPage(startPage);
		model.setRefinements(qf);
		model.setRefineKeyword(StringUtils.trimToNull(rq));

		ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.TIMELINE);
		clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.TIMELINE, page);
		return page;
	}

	@RequestMapping(value = { "/search.json" })
	public ModelAndView searchJson(@RequestParam(value = "query", required = false, defaultValue = "") String q,
			@RequestParam(value = "startFrom", required = false, defaultValue = "1") int start,
			@RequestParam(value = "rows", required = false, defaultValue = "1000") int rows,
			HttpServletRequest request) throws Exception {
		SearchPage model = new SearchPage();
		model.setCurrentSearch(SearchPageEnum.TIMELINE);

		Map<String, String[]> params = RequestUtils.getParameterMap(request);

		// TODO Add filter to defaultFilters when hierarchical objects are
		// introduced...
		// final String[] defaultFilters = new String[]{"YEAR:[* TO *]",
		// "title:[* TO *]", "-YEAR:0000", "europeana_object:[* TO *]"};
		final String[] defaultFilters = new String[] { "YEAR:[* TO *]", "-YEAR:0000" };
		String[] filters = (params.containsKey("qf")) ? (String[]) ArrayUtils.addAll((String[]) params.get("qf"),
				defaultFilters) : defaultFilters;

		// TODO: handle rq
		 // Solr starts from 0
		Query query = new Query(q).setRefinements(filters).setPageSize(rows).setStart(start - 1)
				.setParameter("f.YEAR.facet.mincount", "1");

		BriefBeanView briefBeanView = SearchUtils.createResults(searchService, BriefBean.class, "standard", query,
				start, rows, params, null);
		model.setBriefBeanView(briefBeanView);
		model.setQuery(briefBeanView.getPagination().getPresentationQuery().getUserSubmittedQuery());

		ModelAndView page = ControllerUtil.createModelAndViewPage(model, PortalPageInfo.TIMELINE_JSON);
		clickStreamLogger.logBriefResultView(request, briefBeanView, query, page);
		return page;
	}
}
