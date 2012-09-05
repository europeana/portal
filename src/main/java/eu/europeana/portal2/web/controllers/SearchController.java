package eu.europeana.portal2.web.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.solr.exceptions.SolrTypeException;
import eu.europeana.corelib.solr.service.SearchService;
import eu.europeana.corelib.web.interceptor.ConfigInterceptor;
import eu.europeana.corelib.web.interceptor.LocaleInterceptor;
import eu.europeana.corelib.web.model.PageInfo;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.BriefBeanView;
import eu.europeana.portal2.web.presentation.model.SearchPage;
import eu.europeana.portal2.web.util.ControllerUtil;
import eu.europeana.portal2.web.util.SearchUtils;

@Controller
public class SearchController {

	// @Resource
	// private ThumbnailService thumbnailService;

	private final Logger log = Logger.getLogger(getClass().getName());

	/**
	 * Possible sort options
	 */
	private final List<String> sortValues = Arrays.asList(new String[]{
		"score desc", "title desc", "date desc"
	});

	/**
	 * Deafult sort order
	 */
	private static final String DEFAULT_SORT = "score desc";

	@Value("#{europeanaProperties['portal.theme']}")
	private String defaultTheme;

	@Resource
	private SearchService searchService;
	
	@Resource
	private InternalResourceViewResolver viewResolver;

	@Resource
	private ConfigInterceptor corelib_web_configInterceptor;
	
	@Resource
	private  LocaleInterceptor localeChangeInterceptor;

	@RequestMapping({"/search.html", "/brief-doc.html"})
	public ModelAndView searchHtml(
		@RequestParam(value = "query", required = false, defaultValue="*:*") String q,
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
		@RequestParam(value = "sort", required = false, defaultValue="") String sort,
		@RequestParam(value = "profile", required = false, defaultValue="portal") String profile,
		@RequestParam(value = "theme", required = false, defaultValue="") String theme,
		HttpServletRequest request, HttpServletResponse response,
		Locale locale
	) {
		localeChangeInterceptor.preHandle(request, response, this);

		log.info("============== START SEARCHING ==============");

		SearchPage model = new SearchPage();
		model.setEmbeddedBgColor(embeddedBgColor);
		model.setEmbeddedForeColor(embeddedForeColor);
		model.setEmbedded(embedded);
		model.setEmbeddedLang(embeddedLang);
		log.info("set embedded land to " + embeddedLang);

		model.setEmbeddedLogo(embeddedLogo);
		model.setRswUserId(rswUserId);
		model.setRswDefqry(rswDefqry);
		model.setRefinements(qf);
		log.info("setRefinements on model to " + qf);

		model.setStart(start);
		model.setRows(rows);
		model.setQuery(q);
		if (!sortValues.contains(sort)) {
			sort = DEFAULT_SORT;
		}
		// TODO: the sorting is not solved yet in Solr, so we maintain only one sorting option regardless
		//       what the user sets.
		//       REMOVE THIS LINE AS SOON AS POSSIBLE, but not earlier ;-)
		sort = DEFAULT_SORT;
		model.setSort(sort);
		model.setTheme(ControllerUtil.getSessionManagedTheme(request, theme, defaultTheme));
		PageInfo view = model.isEmbedded() ? PortalPageInfo.SEARCH_EMBED_HTML : PortalPageInfo.SEARCH_HTML;
		ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, view);

		Query query = new Query(q)
						.setRefinements(qf)
						.setPageSize(rows)
						.setStart(start-1) // Solr starts from 0
						.setParameter("f.YEAR.facet.mincount", "1")
						.setParameter("sort", sort);
		Class<? extends BriefBean> clazz = BriefBean.class;

		log.info("query: " + query);
		try {
			BriefBeanView briefBeanView = SearchUtils.createResults(searchService, clazz, profile, query, start, rows);
			model.setBriefBeanView(briefBeanView);
			log.info("NumFound: " + briefBeanView.getPagination().getNumFound());
			model.setEnableRefinedSearch(briefBeanView.getPagination().getNumFound() > 0);
		} catch (SolrTypeException e) {
			log.severe("SolrTypeException: " + e.getMessage());
			// return new ApiError("search.json", e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			log.severe("Exception: " + e.getMessage());
			e.printStackTrace();
		}

		try {
			corelib_web_configInterceptor.postHandle(request, response, this, page);
		} catch (Exception e) {
			log.severe(e.getMessage());
			e.printStackTrace();
		}
		// model.addMessage("theme: " + model.getTheme());

		return page;
	}

	public ConfigInterceptor getCorelib_web_configInterceptor() {
		return corelib_web_configInterceptor;
	}

	public void setCorelib_web_configInterceptor(
			ConfigInterceptor corelib_web_configInterceptor) {
		this.corelib_web_configInterceptor = corelib_web_configInterceptor;
	}

	public InternalResourceViewResolver getViewResolver() {
		return viewResolver;
	}

	public void setViewResolver(InternalResourceViewResolver viewResolver) {
		this.viewResolver = viewResolver;
	}
}
