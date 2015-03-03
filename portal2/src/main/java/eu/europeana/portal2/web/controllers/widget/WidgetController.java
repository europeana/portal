package eu.europeana.portal2.web.controllers.widget;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.definitions.edm.beans.BriefBean;
import eu.europeana.corelib.definitions.model.web.BreadCrumb;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.edm.exceptions.SolrTypeException;
import eu.europeana.corelib.search.SearchService;
import eu.europeana.corelib.web.model.PageInfo;
import eu.europeana.corelib.web.support.Configuration;
import eu.europeana.corelib.web.utils.RequestUtils;
import eu.europeana.portal2.querymodel.query.FacetQueryLinks;
import eu.europeana.portal2.querymodel.query.FacetQueryLinksImpl;
import eu.europeana.portal2.web.controllers.SearchController;
import eu.europeana.portal2.web.controllers.SitemapController;
import eu.europeana.portal2.web.model.facets.Facet;
import eu.europeana.portal2.web.model.facets.LabelFrequency;
import eu.europeana.portal2.web.model.json.BriefBeanImpl;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.EmptyModelPage;
import eu.europeana.portal2.web.presentation.model.SearchPage;
import eu.europeana.portal2.web.presentation.model.SearchWidgetEditorPage;
import eu.europeana.portal2.web.presentation.model.data.submodel.ContributorItem;
import eu.europeana.portal2.web.presentation.model.submodel.BriefBeanView;
import eu.europeana.portal2.web.presentation.model.submodel.ResultPagination;
import eu.europeana.portal2.web.presentation.model.submodel.impl.BriefBeanViewImpl;
import eu.europeana.portal2.web.presentation.model.submodel.impl.ResultPaginationImpl;
import eu.europeana.portal2.web.util.ControllerUtil;
import eu.europeana.portal2.web.util.SearchUtils;

@Controller
public class WidgetController {

Logger log = Logger.getLogger(this.getClass());
	@Resource
	private Configuration config;

	@Resource
	private SearchService searchService;

	private static final List<String> IDS = Arrays.asList(new String[] { "search", "searchGrid", "header", "facets",
			"navigation" });

	private static Date lastSolrUpdate;
	private static List<ContributorItem> contributorEntries;
	private static BriefBeanView briefBeanView;

	private volatile static Calendar lastCheck;

	@RequestMapping({ "/template.html" })
	public ModelAndView templateHtml(
			@RequestParam(value = "id", required = false, defaultValue = "searchGrid") String id,
			HttpServletRequest request, Locale locale) {
		if (StringUtils.isBlank(id) || !IDS.contains(id)) {
			id = "searchGrid";
		}

		PageInfo view = PortalPageInfo.WIDGET_SEARCHGRID_TPL;
		if (id.equals("searchGrid")) {
			view = PortalPageInfo.WIDGET_SEARCHGRID_TPL;
		} else if (id.equals("header")) {
			view = PortalPageInfo.WIDGET_HEADER_TPL;
		} else if (id.equals("facets")) {
			view = PortalPageInfo.WIDGET_FACETS_TPL;
		} else if (id.equals("navigation")) {
			view = PortalPageInfo.WIDGET_NAVIGATION_TPL;
		} else if (id.equals("search")) {
			view = PortalPageInfo.WIDGET_SEARCH_TPL;
		}

		ModelAndView page = null;
		if (id.equals("search")) {
			SearchPage model = new SearchPage();
			model.setImageUri(config.getImageCacheUrl());
			model.setRequest(request);

			model.setRefinements(new String[] {});
			model.setStart(1);
			model.setRows(1);
			model.setQuery("fake");
			model.setSort("score desc");

			try {
				model.setBriefBeanView(getFakeBriefBeanView());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			page = ControllerUtil.createModelAndViewPage(model, locale, view);
		} else {
			EmptyModelPage model = new EmptyModelPage();
			page = ControllerUtil.createModelAndViewPage(model, locale, view);
		}
		return page;
	}

	@RequestMapping({ "/widget/editor.html" })
	public ModelAndView editWidget(HttpServletRequest request, Locale locale) {
		SearchWidgetEditorPage model = new SearchWidgetEditorPage();

		String portalServer = new StringBuilder(config.getPortalServer()).toString();
		String providerQueryFormat = String.format("%s/search.html?query=*:*&qf=PROVIDER:", portalServer) + "%s";

		if (solrOutdated() || contributorEntries == null) {
			contributorEntries = new ArrayList<ContributorItem>();
			List<Count> providers;

			Query query = new Query("*:*").setPageSize(0).setStart(0)
					// Solr starts from 0
					.setParameter("facet.mincount", "1")
					// .setParameter("f.YEAR.facet.mincount", "1")
					.setParameter("sort", SearchController.DEFAULT_SORT).setProduceFacetUnion(true)
					.setAllowSpellcheck(false);

			briefBeanView = null;
			try {
				Map<String, String[]> params = RequestUtils.getParameterMap(request);
				briefBeanView = SearchUtils
						.createResults(searchService, BriefBean.class, "portal", query, 0, 0, params);
			} catch (SolrTypeException e) {
				log.error("SolrTypeException: " + e.getMessage());
				// return new ApiError("search.json", e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				log.error("Exception: " + e.getMessage());
				e.printStackTrace();
			}

			try {
				providers = searchService.createCollections("PROVIDER", "*:*");
				for (Count provider : providers) {
					try {
						String queryString = StringEscapeUtils.escapeXml(String.format(providerQueryFormat,
								SitemapController.convertProviderToUrlParameter(provider.getName())));
						ContributorItem contributorItem = new ContributorItem(queryString, provider.getName(),
								provider.getCount(), portalServer);

						List<ContributorItem.DataProviderItem> dataProviders = new ArrayList<ContributorItem.DataProviderItem>();

						List<Count> rawDataProviders = searchService.createCollections("DATA_PROVIDER", "*:*",
								"PROVIDER:\"" + provider.getName() + "\"");
						for (Count dataProvider : rawDataProviders) {
							if (dataProvider.getCount() > 0) {
								dataProviders.add(contributorItem.new DataProviderItem(contributorItem, dataProvider
										.getName(), dataProvider.getCount()));
							}
						}

						contributorItem.setDataProviders(dataProviders);
						contributorEntries.add(contributorItem);
					} catch (UnsupportedEncodingException e) {
						log.warn(e.getMessage() + " on " + provider.getName());
					}
				}
			} catch (SolrTypeException e1) {
				e1.printStackTrace();
			}
		}

		model.setProviders(contributorEntries);
		model.setApiUrl(config.getApi2url());
		model.setApiKey(config.getApi2key());
		model.setApiSecret(config.getApi2secret());

		try {
			model.setBriefBeanView(briefBeanView);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		model.setEnableRefinedSearch(briefBeanView.getPagination().getNumFound() > 0);

		return ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.WIDGET_EDITOR);
	}

	private BriefBeanView getFakeBriefBeanView() {
		BriefBeanViewImpl briefBeanView = new BriefBeanViewImpl();
		BriefBeanImpl bean = new BriefBeanImpl();
		bean.setId("xxx");
		bean.setDocType(new String[] { "TEXT" });
		List<BriefBean> beans = new ArrayList<BriefBean>();
		beans.add(bean);

		briefBeanView.setBriefBeans(beans);

		Query query = new Query("fake");

		List<Facet> facets = new ArrayList<Facet>();
		Facet facet = new Facet();
		facet.name = eu.europeana.corelib.definitions.solr.Facet.COUNTRY.name();
		facet.fields = new ArrayList<LabelFrequency>();
		facet.fields.add(new LabelFrequency("fake", 1));
		facets.add(facet);
		List<FacetQueryLinks> queryLinks = FacetQueryLinksImpl.createDecoratedFacets(facets, query);
		briefBeanView.setQueryLinks(queryLinks);

		List<BreadCrumb> breadCrumbs = new ArrayList<BreadCrumb>();
		ResultPagination pagination = new ResultPaginationImpl(1, 1, 1, query, breadCrumbs);
		briefBeanView.setPagination(pagination);

		// set search filters
		Map<String, String[]> urlParams = new HashMap<String, String[]>();
		urlParams.put("q", new String[] {"fake"});
		briefBeanView.makeFilters(query, urlParams);

		return briefBeanView;
	}

	synchronized private boolean solrOutdated() {
		// check it once a day
		Calendar timeout = DateUtils.toCalendar(DateUtils.addDays(new Date(), -1));
		if (lastCheck == null || lastCheck.before(timeout)) {
			log.info(String.format("%s requesting solr outdated (timeout: %s, lastCheck: %s)", Thread.currentThread()
					.getName(), timeout.getTime().toString(), (lastCheck == null ? "null" : lastCheck.getTime()
					.toString())));
			lastCheck = Calendar.getInstance();
			Date actualSolrUpdate = null;
			try {
				log.info("start checking Solr update time");
				actualSolrUpdate = searchService.getLastSolrUpdate();
				log.info("Solr update time checked");
			} catch (SolrServerException e) {
				log.error("SolrServerException " + e.getLocalizedMessage());
			} catch (IOException e) {
				log.error("IOException " + e.getLocalizedMessage());
			}

			if (actualSolrUpdate == null) {
				return true;
			}

			if (lastSolrUpdate == null) {
				lastSolrUpdate = actualSolrUpdate;
				return true;
			} else {
				return !actualSolrUpdate.equals(lastSolrUpdate);
			}
		} else {
			return false;
		}
	}

}
