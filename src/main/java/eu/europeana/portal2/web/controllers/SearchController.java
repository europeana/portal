package eu.europeana.portal2.web.controllers;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.db.service.ThumbnailService;
import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.beans.IdBean;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.solr.exceptions.SolrTypeException;
import eu.europeana.corelib.solr.model.ResultSet;
import eu.europeana.corelib.solr.service.SearchService;
import eu.europeana.corelib.web.interceptor.ConfigInterceptor;
import eu.europeana.corelib.web.utils.NavigationUtils;
import eu.europeana.portal2.web.model.ApiError;
import eu.europeana.portal2.web.model.ModelUtils;
import eu.europeana.portal2.web.model.SearchResults;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.BriefBeanView;
import eu.europeana.portal2.web.presentation.model.BriefBeanViewImpl;
import eu.europeana.portal2.web.presentation.model.ResultPagination;
import eu.europeana.portal2.web.presentation.model.ResultPaginationImpl;
import eu.europeana.portal2.web.presentation.model.SearchPage;
import eu.europeana.portal2.web.util.ControllerUtil;

@Controller
public class SearchController {
	
	@Resource
	private ThumbnailService thumbnailService;

	@Resource
	private SearchService searchService;

	@Resource
	private ConfigInterceptor corelib_web_configInterceptor;

	@RequestMapping({"/search.html", "/brief-doc.html"})
	public ModelAndView searchHtml(
		@RequestParam(value = "query", required = false) String q,
		@RequestParam(value = "embedded", required = false) String embedded,
		@RequestParam(value = "embeddedLogo", required = false) String embeddedLogo,
		@RequestParam(value = "embeddedBgColor", required = false) String embeddedBgColor,
		@RequestParam(value = "embeddedForeColor", required = false) String embeddedForeColor,
		@RequestParam(value = "embeddedlang", required = false) String embeddedLang,
		@RequestParam(value = "qf", required = false) String[] qf,
		@RequestParam(value = "rswUserId", required = false) String rswUserId,
		@RequestParam(value = "rswDefqry", required = false) String rswDefqry,
		@RequestParam(value = "start", required = false, defaultValue = "1") int start,
		@RequestParam(value = "rows", required = false, defaultValue="12") int rows,
		@RequestParam(value = "profile", required = false, defaultValue="portal") String profile,
		HttpServletRequest request, HttpServletResponse response,
		Locale locale
	) {

		SearchPage model = new SearchPage();
		model.setEmbeddedBgColor(embeddedBgColor);
		model.setEmbeddedForeColor(embeddedForeColor);
		model.setEmbedded(embedded);
		model.setEmbeddedLang(embeddedLang);
		model.setEmbeddedLogo(embeddedLogo);
		model.setRswUserId(rswUserId);
		model.setRswDefqry(rswDefqry);
		model.setRefinements(qf);
		model.setStart(start);
		model.setQuery(q);
		ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, 
			model.isEmbedded() 
				? PortalPageInfo.SEARCH_EMBED_HTML
				: PortalPageInfo.SEARCH_HTML
		);

		Query query = new Query(q).setRefinements(qf).setPageSize(12).setStart(start);
		Class<? extends IdBean> clazz = BriefBean.class;
//		if (StringUtils.containsIgnoreCase(profile, "minimal")) {
//			clazz = BriefBean.class;
//		}
		BriefBeanView briefBeanView = null;
		try {
			briefBeanView = createResults(profile, query, start);
			model.setBriefBeanView(briefBeanView);
		} catch (SolrTypeException e) {
			// return new ApiError("search.json", e.getMessage());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO:
		// BriefBeanView briefBeanView = beanQueryModelFactory.getBriefResultView(solrQuery, request.getQueryString());
		model.setEnableRefinedSearch(briefBeanView.getPagination().getNumFound() > 0);
		try {
			corelib_web_configInterceptor.postHandle(request, response, this, page);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return page;
	}

	private BriefBeanView createResults(String profile, Query q, int start) 
			throws SolrTypeException {
		BriefBeanViewImpl briefBeanView = new BriefBeanViewImpl();

		SearchResults response = new SearchResults("search.json");
		ResultSet<BriefBean> resultSet = searchService.search(BriefBean.class, q);
		resultSet.getQuery();
		response.totalResults = resultSet.getResultSize();
		response.itemsCount = resultSet.getResults().size();
		response.items = resultSet.getResults();
		briefBeanView.setBriefDocs(resultSet.getResults());
		if (StringUtils.containsIgnoreCase(profile, "facets") || StringUtils.containsIgnoreCase(profile, "portal")) {
			response.facets = ModelUtils.conventFacetList(resultSet.getFacetFields());
		}
		if (StringUtils.containsIgnoreCase(profile, "breadcrumb") || StringUtils.containsIgnoreCase(profile, "portal")) {
			response.breadCrumbs = NavigationUtils.createBreadCrumbList(q);
		}
		if (StringUtils.containsIgnoreCase(profile, "spelling") || StringUtils.containsIgnoreCase(profile, "portal")) {
			briefBeanView.setSpellcheck(ModelUtils.convertSpellCheck(resultSet.getSpellcheck()));
		}
//		if (StringUtils.containsIgnoreCase(profile, "suggestions") || StringUtils.containsIgnoreCase(profile, "portal")) {
//		}
		
		ResultPagination pagination = new ResultPaginationImpl(start, (int)resultSet.getResultSize(), 
				resultSet.getResults().size(), q.getQuery(), q.getQuery(), response.breadCrumbs);
		briefBeanView.setPagination(pagination);
		return briefBeanView;
	}

	public ConfigInterceptor getCorelib_web_configInterceptor() {
		return corelib_web_configInterceptor;
	}

	public void setCorelib_web_configInterceptor(
			ConfigInterceptor corelib_web_configInterceptor) {
		this.corelib_web_configInterceptor = corelib_web_configInterceptor;
	}
}
