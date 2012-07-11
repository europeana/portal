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

package eu.europeana.portal2.web.util;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import eu.europeana.corelib.web.model.PageData;
import eu.europeana.corelib.web.model.PageInfo;
import eu.europeana.portal2.web.presentation.ThemeChecker;
import eu.europeana.portal2.web.presentation.model.PortalPageData;

/**
 * Utility methods for controllers
 * 
 * @author Gerald de Jong <geralddejong@gmail.com>
 */

public class ControllerUtil {

	@Value("#{europeanaProperties['portal.google.plus.publisher.id']}")
	private static String portalGooglePlusPublisherId;

	@Value("#{europeanaProperties['portal.theme']}")
	private static String defaultTheme;

	private static Logger log = Logger.getLogger(ControllerUtil.class.getName());
	
	public static Locale getLocale(HttpServletRequest request) {
		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
		return localeResolver.resolveLocale(request);
	}

	/*
	 * This creates the default ModelAndView for the portal applications. It
	 * should be used in every Controller.
	 */
	public static ModelAndView createModelAndViewPage(PortalPageData model, Locale locale, PageInfo view) {
		// adjust model
		model.setLocale(locale);
		// model.setUser(ControllerUtil.getUser());
		model.setPageInfo(view);
		model.setPageTitle(view.getPageTitle());
		model.setGooglePlusPublisherId(StringUtils.trimToEmpty(portalGooglePlusPublisherId));

		// create page
		ModelAndView page = new ModelAndView(model.getTheme() + "/" + view.getTemplate());
		page.addObject(PageData.PARAM_MODEL, model);
		return page;
	}

	public static ModelAndView createModelAndViewPage(PortalPageData model, PageInfo view) {
		return createModelAndViewPage(model, null, view);
	}

	/*
	 * Format full requested uri from HttpServletRequest
	 */
	@SuppressWarnings("unchecked")
	public static String formatFullRequestUrl(HttpServletRequest request) {
		StringBuffer requestURL = request.getRequestURL();
		if (request.getQueryString() != null) {
			requestURL.append("?").append(request.getQueryString());
		} else if (request.getParameterMap() != null) {
			requestURL.append(formatParameterMapAsQueryString(request.getParameterMap()));
		}
		return requestURL.toString();
	}

	public static String formatParameterMapAsQueryString(
			Map<String, String[]> parameterMap) {
		StringBuilder output = new StringBuilder();
		output.append("?");
		Iterator<Map.Entry<String, String[]>> iterator1 = parameterMap.entrySet().iterator();
		while (iterator1.hasNext()) {
			Map.Entry<String, String[]> entry = iterator1.next();
			if (entry.getValue().length > 0) {
				output.append(MessageFormat.format("{0}={1}", entry.getKey(), entry.getValue()[0]));
			} else {
				output.append(MessageFormat.format("{0}={1}", entry.getKey(), ""));
			}
			if (iterator1.hasNext()) {
				output.append("&");
			}
		}
		return output.toString();
	}

	public static String getFullServletUrl(HttpServletRequest request) {
		String url = request.getRequestURL().toString();
		int index = url.indexOf(request.getServerName());
		url = url.substring(0, index) + request.getServerName() + ":"
				+ request.getServerPort() + request.getRequestURI();
		return url;
	}

	
	public static String getSessionManagedTheme(HttpServletRequest request, String theme) {
		HttpSession session = request.getSession(true);
		if (!theme.equals("")) {
			theme = ThemeChecker.check(theme);
			session.setAttribute("theme", theme);
		}
		else {
			String storedTheme = (String)session.getAttribute("theme");
			if (storedTheme != null && !storedTheme.equals("")) {
				theme = ThemeChecker.check(storedTheme);
			}
		}
		if (theme.equals("")) {
			theme = ThemeChecker.check(defaultTheme);
		}
		return theme;
	}
}
