package eu.europeana.portal2.web.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.solr.exceptions.SolrTypeException;
import eu.europeana.corelib.solr.service.SearchService;
import eu.europeana.corelib.web.model.PageInfo;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.BriefBeanView;
import eu.europeana.portal2.web.presentation.model.SitemapPage;
import eu.europeana.portal2.web.presentation.model.data.SearchData;
import eu.europeana.portal2.web.presentation.model.data.decorators.BriefBeanDecorator;
import eu.europeana.portal2.web.presentation.model.data.decorators.BriefBeanViewDecorator;
import eu.europeana.portal2.web.util.ClickStreamLogger;
import eu.europeana.portal2.web.util.ControllerUtil;
import eu.europeana.portal2.web.util.Injector;
import eu.europeana.portal2.web.util.SearchUtils;

@Controller
public class BrowsePageController {

	@Resource private SearchService searchService;

	@Resource private ClickStreamLogger clickStreamLogger;

	private final Logger log = Logger.getLogger(getClass().getName());

	@Value("#{europeanaProperties['portal.minCompletenessToPromoteInSitemaps']}")
	private int minCompletenessToPromoteInSitemaps;

	@RequestMapping("/browse-all.html")
	public ModelAndView browseAll(
			@RequestParam(value = "prefix", required = false) String prefix,
			HttpServletRequest request,
			HttpServletResponse response,
			Locale locale) 
				throws Exception {
		log.info("=========== browse-all.html ===========");

		Injector injector = new Injector(request, response, locale);

		SitemapPage<BriefBeanDecorator> model = new SitemapPage<BriefBeanDecorator>();
		injector.injectProperties(model);

		PageInfo pageInfo = PortalPageInfo.SITEMAP_BROWSE_INDEX;
		if (StringUtils.isEmpty(prefix)) {
			// index page
			prefix = "";
		} else {
			if (prefix.length() >= 4) {
				// landing page
				prefix = prefix.substring(0, 4);
				pageInfo = PortalPageInfo.SITEMAP_BROWSE_LANDING;
				Query query = new Query(
					SitemapController.solrQueryClauseToIncludeRecordsToPromoteInSitemaps(minCompletenessToPromoteInSitemaps))
					.setRefinements("id6hash:" + prefix + "*")
					.setPageSize(1000);

				BriefBeanView briefBeanView = null;
				Class<? extends BriefBean> clazz = BriefBean.class;
				Map<String, String[]> params = request.getParameterMap();

				try {
					briefBeanView = SearchUtils.createResults(searchService, clazz, "portal", query, 0, 1000, params);
					List<BriefBeanDecorator> beans = new ArrayList<BriefBeanDecorator>();
					int index = model.getStart();
					for (BriefBean briefDoc : briefBeanView.getBriefBeans()) {
						beans.add(new BriefBeanDecorator(model, briefDoc, index++));
					}
					model.setResults(beans);
					log.info("results: " + model.getResults().size());
				} catch (SolrTypeException e) {
					log.severe("SolrTypeException: " + e.getMessage());
					// return new ApiError("search.json", e.getMessage());
					e.printStackTrace();
				} catch (Exception e) {
					log.severe("Exception: " + e.getMessage());
					e.printStackTrace();
				}

			}
		}

		makePageObject(model, prefix);
		ModelAndView page = ControllerUtil.createModelAndViewPage(model, pageInfo);

		injector.postHandle(this, page);
		clickStreamLogger.logUserAction(request, ClickStreamLogger.UserAction.BROWSE_ALL, page);

		return page;
	}

	public static void makePageObject(SitemapPage<?> model, String prefix) {
		int clickNr = prefix.length() / 2;
		StringBuilder sb = new StringBuilder("Get to every object page in three clicks");
		sb.append((clickNr > 0 ? (", click " + clickNr) : ""));
		sb.append(". This is a randomly generated group of pages");
		model.setTitle(sb.toString());
		model.setPrefix(prefix);
	}

}
