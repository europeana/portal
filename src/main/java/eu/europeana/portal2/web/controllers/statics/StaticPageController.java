/*
 * Copyright 2007-2012 The Europeana Foundation
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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.web.interceptor.ConfigInterceptor;
import eu.europeana.portal2.web.model.CorePageInfo;
import eu.europeana.portal2.web.util.ClickStreamLogger;
import eu.europeana.portal2.web.util.ClickStreamLoggerImpl;
import eu.europeana.portal2.web.util.ControllerUtil;
import eu.europeana.portal2.web.util.StaticPageCache;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.enums.Redirect;
import eu.europeana.portal2.web.presentation.model.EmptyModelPage;
import eu.europeana.portal2.web.presentation.model.StaticPage;

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

	private StaticPageCache staticPageCache = new StaticPageCache();

	private ClickStreamLogger clickStreamLogger = new ClickStreamLoggerImpl();
	
	private final Logger log = Logger.getLogger(getClass().getName());

	// @Resource
	// private ResourceBundleMessageSource messageSource;

	@Resource
	private ConfigInterceptor corelib_web_configInterceptor;

	@Value("#{europeanaProperties['portal.name']}")
	private String portalName;

	@Value("#{europeanaProperties['portal.server']}")
	private String portalServer;

	@Value("#{europeanaProperties['static.page.path']}")
	private String staticPagePath;

	@Value("#{europeanaProperties['portal.theme']}")
	private String defaultTheme;

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
			@RequestParam(value = "theme", required = false, defaultValue="") String theme,
			HttpServletRequest request, HttpServletResponse response,
			Locale locale) throws Exception {
		pageName = "/" + pageName + ".html";

		log.info("=========== fetchStaticPage ==============");
		log.info("pageName: " + pageName);
		log.info("portalName: " + portalName);
		log.info("portalServer: " + portalServer);
		log.info("staticPagePath: " + staticPagePath);
		staticPageCache.setStaticPagePath(staticPagePath);

		// test for possible redirects first!
		Redirect redirect = Redirect.safeValueOf(pageName);
		if (redirect != null) {
			StringBuilder sb = new StringBuilder();
			if (!redirect.getRedirect().startsWith("http")) {
				sb.append(portalServer);
				sb.append(portalName);
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
		StaticPage model = new StaticPage();
		model.setBodyContent(getStaticPagePart(pageName, AFFIX_TEMPLATE_VAR_FOR_CONTENT, locale));
		model.setHeaderContent(getStaticPagePart(pageName, AFFIX_TEMPLATE_VAR_FOR_HEADER, locale));
		model.setLeftContent(getStaticPagePart(pageName, AFFIX_TEMPLATE_VAR_FOR_LEFT, locale));
		model.setTitleContent(getStaticPagePart(pageName, AFFIX_TEMPLATE_VAR_FOR_TITLE, locale));
		// TODO: check it!
		// model.setDefaultContent(getStaticPagePart(pageName, "", locale));
		model.setDefaultContent(model.getBodyContent());
		model.setTheme(ControllerUtil.getSessionManagedTheme(request, theme, defaultTheme));

		// clickStreamLogger.logCustomUserAction(request, ClickStreamLogger.UserAction.STATICPAGE, "view=" + request.getPathInfo());

		ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.STATICPAGE);
		corelib_web_configInterceptor.postHandle(request, response, this, page);
		model.addMessage("theme: " + model.getTheme());

		return page;
	}

	private String getStaticPagePart(String fileName, String partName, Locale language) {

		if (!StringUtils.isEmpty(partName)) {
			fileName = StringUtils.replaceOnce(fileName, ".", "_" + partName + ".");
		}

		return staticPageCache.getPage(fileName, language);
	}

	/**
	 * composed from servletPath and pathInfo that may be null and may be called
	 * w/o language for verbatim pages
	 */
	private String makePageFileName(String pageNamePrefix, String pageName) {
		String pageFileName = (pageNamePrefix == null ? "" : pageNamePrefix)
				+ (pageName == null ? "" : pageName);
		return pageFileName;
	}

	/**
	 * All sp/ css are served up from here
	 * 
	 * @param request
	 *            where we find locale
	 * @return ModelAndView
	 * @throws Exception
	 *             something went wrong
	 */

	@RequestMapping("/css/**/*.css")
	public void fetchMcCss(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("text/css");
		fetchVerbatimPage(request, response);
	}

	@RequestMapping("/css/**/*.xml")
	public void fetchMcXml(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("application/rss+xml");
		fetchVerbatimPage(request, response);
	}

	/**
	 * All sp/ js are served up from here
	 * 
	 * @param request
	 *            where we find locale
	 * @return ModelAndView
	 * @throws Exception
	 *             something went wrong
	 */

	@RequestMapping("/js/**/*.js")
	public void fetchMcJs(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("application/x-javascript");
		fetchVerbatimPage(request, response);
	}

	/**
	 * All sp/ jpg are served up from here
	 * 
	 * @param request
	 *            where we find locale
	 * @return ModelAndView
	 * @throws Exception
	 *             something went wrong
	 */

	@RequestMapping("/img/**/*.jpg")
	public void fetchMcJpg(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("image/jpeg");
		fetchVerbatimPage(request, response);
	}

	@RequestMapping("/rss-blog-cache/**/*.jpg")
	public void fetchRssJpg(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("image/jpeg");
		fetchVerbatimPage(request, response);
	}

	/**
	 * All sp/ png are served up from here
	 * 
	 * @param request
	 *            where we find locale
	 * @return ModelAndView
	 * @throws Exception
	 *             something went wrong
	 */

	@RequestMapping("/img/**/*.png")
	public void fetchMcPng(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("image/png");
		fetchVerbatimPage(request, response);
	}

	@RequestMapping("/rss-blog-cache/**/*.png")
	public void fetchRssPng(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("image/png");
		fetchVerbatimPage(request, response);
	}

	/**
	 * All sp/ gif are served up from here
	 * 
	 * @param request
	 *            where we find locale
	 * @return ModelAndView
	 * @throws Exception
	 *             something went wrong
	 */

	@RequestMapping("/img/**/*.gif")
	public void fetchMcGif(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("image/gif");
		fetchVerbatimPage(request, response);
	}

	@RequestMapping("/rss-blog-cache/**/*.gif")
	public void fetchRssGif(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("image/gif");
		fetchVerbatimPage(request, response);
	}

	private void fetchVerbatimPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		staticPageCache.setStaticPagePath(staticPagePath);
		OutputStream out = response.getOutputStream();
		try {
			String pageFileName = makePageFileName(request.getServletPath(), request.getPathInfo());
			staticPageCache.writeBinaryPage(pageFileName, out);
		} catch (Exception e) {
			log.severe("Exception during fetchVerbatimPage: " + e.getLocalizedMessage());
		} finally {
			out.flush();
			out.close();
		}
	}

	/*
	 * freemarker Template not loadable from database
	 */

	@RequestMapping("/error.html")
	public ModelAndView errorPageHandler(HttpServletRequest request, Locale locale) throws Exception {
		CorePageInfo pageType = CorePageInfo.ERROR;
		clickStreamLogger.logStaticPageView(request, pageType);
		return ControllerUtil.createModelAndViewPage(new EmptyModelPage(), locale, pageType);
	}
	
	public void setStaticPageCache(StaticPageCache staticPageCache) {
		this.staticPageCache = staticPageCache;
	}

}