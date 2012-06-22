/*
 * Copyright 2007 EDL FOUNDATION
 *
 *  Licensed under the EUPL, Version 1.0 or as soon they
 *  will be approved by the European Commission - subsequent
 *  versions of the EUPL (the "Licence");
 *  you may not use this work except in compliance with the
 *  Licence.
 *  You may obtain a copy of the Licence at:
 *
 *  http://ec.europa.eu/idabc/eupl
 *
 *  Unless required by applicable law or agreed to in
 *  writing, software distributed under the Licence is
 *  distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *  express or implied.
 *  See the Licence for the specific language governing
 *  permissions and limitations under the Licence.
 */

package eu.europeana.portal2.web.util;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import eu.europeana.corelib.definitions.db.entity.relational.User;
import eu.europeana.corelib.web.model.PageData;
import eu.europeana.corelib.web.model.PageInfo;

/**
 * Utility methods for controllers
 * 
 * @author Gerald de Jong <geralddejong@gmail.com>
 */

public class ControllerUtil {

	public static Locale getLocale(HttpServletRequest request) {
		LocaleResolver localeResolver = RequestContextUtils
				.getLocaleResolver(request);
		return localeResolver.resolveLocale(request);
	}

	/*
	 * This creates the default ModelAndView for the portal applications. It
	 * should be used in every Controller.
	 */
	public static ModelAndView createModelAndViewPage(PageData model, Locale locale, PageInfo view) {
		model.setLocale(locale);
		// model.setUser(ControllerUtil.getUser());
		model.setPageInfo(view);
		model.setPageTitle(view.getPageTitle());
		ModelAndView page = new ModelAndView(view.getTemplate());
		page.addObject(PageData.PARAM_MODEL, model);
		return page;
	}

	public static ModelAndView createModelAndViewPage(PageData model, PageInfo view) {
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

}
