package eu.europeana.portal2.web.controllers.widget;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.definitions.model.web.BreadCrumb;
import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.web.model.PageInfo;
import eu.europeana.portal2.querymodel.query.FacetQueryLinks;
import eu.europeana.portal2.querymodel.query.FacetQueryLinksImpl;
import eu.europeana.portal2.web.model.facets.Facet;
import eu.europeana.portal2.web.model.facets.LabelFrequency;
import eu.europeana.portal2.web.model.json.BriefBeanImpl;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.BriefBeanView;
import eu.europeana.portal2.web.presentation.model.BriefBeanViewImpl;
import eu.europeana.portal2.web.presentation.model.EmptyModelPage;
import eu.europeana.portal2.web.presentation.model.ResultPagination;
import eu.europeana.portal2.web.presentation.model.ResultPaginationImpl;
import eu.europeana.portal2.web.presentation.model.SearchPage;
import eu.europeana.portal2.web.util.ControllerUtil;
import eu.europeana.portal2.web.util.Injector;

@Controller
public class WidgetController {

	private static final List<String> IDS = Arrays.asList(new String[]{"search", "searchGrid", "header", "facets", "navigation"});
	
	@RequestMapping({"/template.html"})
	public ModelAndView templateHtml(
		@RequestParam(value = "id", required = false, defaultValue="searchGrid") String id,
		HttpServletRequest request,
		HttpServletResponse response,
		Locale locale
	) {
		if (StringUtils.isBlank(id) || !IDS.contains(id)) {
			id = "searchGrid";
		}

		PageInfo view = PortalPageInfo.TEMPLATE_SEARCHGRID_HTML;
		if (id.equals("searchGrid")) {
			view = PortalPageInfo.TEMPLATE_SEARCHGRID_HTML;
		} else if (id.equals("header")) {
			view = PortalPageInfo.TEMPLATE_HEADER_HTML;
		} else if (id.equals("facets")) {
			view = PortalPageInfo.TEMPLATE_FACETS_HTML;
		} else if (id.equals("navigation")) {
			view = PortalPageInfo.TEMPLATE_NAVIGATION_HTML;
		} else if (id.equals("search")) {
			view = PortalPageInfo.SEARCH_HTML;
		}

		ModelAndView page = null;
		if (id.equals("search")) {
			Injector injector = new Injector(request, response, locale);
			SearchPage model = new SearchPage();
			model.setRequest(request);
			model.setEmbeddedBgColor("");
			model.setEmbeddedForeColor("");
			model.setEmbedded("");
			model.setEmbeddedLang("");
			model.setEmbeddedLogo("");
			model.setRswUserId("");
			model.setRswDefqry("");
			model.setRefinements(new String[]{});
			model.setStart(1);
			model.setRows(1);
			model.setQuery("fake");
			model.setSort("score desc");

			try {
				model.setBriefBeanView(getFakeBriefBeanView());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			injector.injectProperties(model);
			page = ControllerUtil.createModelAndViewPage(model, locale, view);
			injector.postHandle(this, page);
		} else {
			EmptyModelPage model = new EmptyModelPage();
			page = ControllerUtil.createModelAndViewPage(model, locale, view);
		}
		return page;
	}

	private BriefBeanView getFakeBriefBeanView() {
		BriefBeanViewImpl briefBeanView = new BriefBeanViewImpl();
		BriefBeanImpl bean = new BriefBeanImpl();
		bean.setId("xxx");
		bean.setDocType(new String[]{"TEXT"});
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

		return briefBeanView;
	}

}
