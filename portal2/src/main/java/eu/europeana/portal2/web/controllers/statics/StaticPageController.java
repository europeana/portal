/*
 * Copyright 2007-2013 The Europeana Foundation
 *
 *  Licenced under the EUPL, Version 1.1 (the "Licence") and subsequent versions as approved 
 *  by the European Commission;
 *  You may not use this work except in compliance with the Licence.
 *  
 *  You may obtain a copy of the Licence at:
 *  http://joinup.ec.europa.eu/software/page/eupl
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under 
 *  the Licence is distributed on an "AS IS" basis, without warranties or conditions of 
 *  any kind, either express or implied.
 *  See the Licence for the specific language governing permissions and limitations under 
 *  the Licence.
 */

package eu.europeana.portal2.web.controllers.statics;

import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.definitions.exception.ProblemType;
import eu.europeana.corelib.logging.Log;
import eu.europeana.corelib.logging.Logger;
import eu.europeana.corelib.solr.exceptions.EuropeanaQueryException;
import eu.europeana.corelib.web.support.Configuration;
import eu.europeana.portal2.services.ClickStreamLogService;
import eu.europeana.portal2.services.impl.ResponsiveImageCache;
import eu.europeana.portal2.services.impl.StaticPageCache;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.enums.Redirect;
import eu.europeana.portal2.web.presentation.model.EmptyModelPage;
import eu.europeana.portal2.web.presentation.model.StaticPage;
import eu.europeana.portal2.web.util.ControllerUtil;

/**
 * Generic controller for static pages.
 * 
 * @author Borys Omelayenko
 * @author Eric van der Meulen <eric.meulen@gmail.com>
 * @author Sjoerd Siebinga <sjoerd.siebinga@gmail.com>
 */

@Controller
public class StaticPageController {

	public static final String AFFIX_TEMPLATE_VAR_FOR_LEFT = "left";

	private static final String AFFIX_TEMPLATE_VAR_FOR_HEADER = "header";

	private static final String AFFIX_TEMPLATE_VAR_FOR_CONTENT = "content";

	private static final String AFFIX_TEMPLATE_VAR_FOR_TITLE = "title";

	@Log
	private Logger log;

	@Resource
	private Configuration config;

	@Resource
	private StaticPageCache staticPageCache;

	@Resource
	private ResponsiveImageCache responsiveImageCache;

	@Resource
	private ClickStreamLogService clickStreamLogger;

	/**
	 * All of the pages are served up from here
	 * 
	 * @param request
	 *            where we find locale
	 * @param response
	 *            where to write it
	 * @return ModelAndView
	 * @throws Exception
	 *             something went wrong
	 */
	@RequestMapping("/{pageName}.html")
	public ModelAndView fetchStaticPage(
			@PathVariable String pageName,
			HttpServletRequest request,
			HttpServletResponse response,
			Locale locale) throws Exception {
		pageName = "/" + pageName + getSuffix(pageName) + ".html";
		StaticPage model = new StaticPage();

		ModelAndView page = doFetchStaticPage(pageName, response, model, locale);

		if (!model.isPageFound()) {
			response.setStatus(404);
		}

		clickStreamLogger.logStaticPageView(request, PortalPageInfo.STATICPAGE);
		return page;
	}

	@RequestMapping("/rights/{pageName}.html")
	public ModelAndView fetchStaticRightsPage(
			@PathVariable String pageName,
			HttpServletRequest request,
			HttpServletResponse response,
			Locale locale) throws Exception {
		pageName = "/rights/" + pageName + getSuffix(pageName) + ".html";
		StaticPage model = new StaticPage();

		ModelAndView page = doFetchStaticPage(pageName, response, model, locale);

		if (!model.isPageFound()) {
			response.setStatus(404);
		}

		clickStreamLogger.logStaticPageView(request, PortalPageInfo.STATICPAGE);
		return page;
	}

	/**
	 * Adds suffix which can be used by adding page name. It distinguises portal1 and portal2 static pages.
	 * 
	 * @param pageName
	 *            The page name
	 * @return The suffix
	 */
	private String getSuffix(String pageName) {
		String suffix = config.getStaticPageSuffix();

		if (StringUtils.isBlank(suffix)) {
			return "";
		}

		if (pageName.endsWith(suffix)) {
			if (!config.getStaticPageInVersions().contains(pageName.substring(0, pageName.length() - suffix.length()))) {
				log.warn("pageName not registered in the StaticPageInVersions property: " + pageName);
			}
			return "";
		}

		if (!config.getStaticPageInVersions().contains(pageName)) {
			return "";
		}

		return suffix;
	}

	private ModelAndView doFetchStaticPage(String pageName, HttpServletResponse response, StaticPage model,
			Locale locale) {
		log.info("=========== fetchStaticPage ==============");
		log.info("pageName: " + pageName);
		staticPageCache.setStaticPagePath(config.getStaticPagePath());

		// test for possible redirects first!
		Redirect redirect = Redirect.safeValueOf(pageName);
		if (redirect != null) {
			StringBuilder sb = new StringBuilder();
			if (!redirect.getRedirect().startsWith("http")) {
				sb.append(config.getPortalServer());
				sb.append(config.getPortalName());
			}
			sb.append(redirect.getRedirect());
			response.setStatus(301);
			response.setHeader("Location", sb.toString());
			response.setHeader("Connection", "close");
			return null;
		}
		// String pageName = makePageFileName(request.getServletPath(),
		// request.getPathInfo());

		// generate static page
		model.setTc(pageName.indexOf("rights") > -1);
		model.setBodyContent(getStaticPagePart(pageName, AFFIX_TEMPLATE_VAR_FOR_CONTENT, locale));
		model.setHeaderContent(getStaticPagePart(pageName, AFFIX_TEMPLATE_VAR_FOR_HEADER, locale));
		model.setLeftContent(getStaticPagePart(pageName, AFFIX_TEMPLATE_VAR_FOR_LEFT, locale));
		String titleContent = getStaticPagePart(pageName, AFFIX_TEMPLATE_VAR_FOR_TITLE, locale);
		model.setTitleContent(titleContent);
		model.setDefaultContent(model.getBodyContent());
		if (StringUtils.isBlank(model.getBodyContent()) && StringUtils.isBlank(model.getHeaderContent())
				&& StringUtils.isBlank(model.getLeftContent()) && StringUtils.isBlank(titleContent)) {
			model.setPageFound(false);
		}

		return ControllerUtil.createModelAndViewPage(model, PortalPageInfo.STATICPAGE);
	}

	private String getStaticPagePart(String fileName, String partName, Locale locale) {

		if (!StringUtils.isEmpty(partName)) {
			fileName = StringUtils.replaceOnce(fileName, ".", "_" + partName + ".");
		}

		return staticPageCache.getPage(fileName, locale);
	}

	@RequestMapping("/page-not-found.html")
	public ModelAndView pageNotFoundHandler() throws Exception {
		log.info("====== page-not-found.html ======");
		throw new EuropeanaQueryException(ProblemType.PAGE_NOT_FOUND);
	}

	@RequestMapping("/error.html")
	public ModelAndView errorPageHandler(HttpServletRequest request, Locale locale)
			throws Exception {
		log.info("====== error.html ======");
		PortalPageInfo pageType = PortalPageInfo.ERROR;

		clickStreamLogger.logStaticPageView(request, pageType);
		EmptyModelPage model = new EmptyModelPage();
		model.setProblem(ProblemType.UNKNOWN);

		return ControllerUtil.createModelAndViewPage(model, locale, pageType);
	}

	public void setStaticPageCache(StaticPageCache staticPageCache) {
		this.staticPageCache = staticPageCache;
	}

	public void setResponsiveImageCache(ResponsiveImageCache responsiveImageCache) {
		this.responsiveImageCache = responsiveImageCache;
	}
}