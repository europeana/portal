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

package eu.europeana.portal2.services.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.joda.time.DateTime;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.definitions.db.entity.relational.User;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.logging.Log;
import eu.europeana.corelib.logging.Logger;
import eu.europeana.corelib.web.model.PageData;
import eu.europeana.corelib.web.model.PageInfo;
import eu.europeana.corelib.web.utils.RequestUtils;
import eu.europeana.portal2.services.ClickStreamLogService;
import eu.europeana.portal2.web.presentation.model.data.submodel.clickstream.AffixDAO;
import eu.europeana.portal2.web.presentation.model.data.submodel.clickstream.BriefViewDAO;
import eu.europeana.portal2.web.presentation.model.data.submodel.clickstream.CustomLogDAO;
import eu.europeana.portal2.web.presentation.model.data.submodel.clickstream.FullViewDAO;
import eu.europeana.portal2.web.presentation.model.data.submodel.clickstream.LanguageChangeDAO;
import eu.europeana.portal2.web.presentation.model.data.submodel.clickstream.UserActionDAO;
import eu.europeana.portal2.web.presentation.model.submodel.BriefBeanView;
import eu.europeana.portal2.web.presentation.model.submodel.DocIdWindowPager;
import eu.europeana.portal2.web.presentation.model.submodel.FullBeanView;
import eu.europeana.portal2.web.util.ControllerUtil;

/**
 * @author Sjoerd Siebinga <sjoerd.siebinga@gmail.com>
 */
public class ClickStreamJsonLogServiceImpl implements ClickStreamLogService {

	@Log
	private static Logger log;

	public static String VERSION = "2.0";

	@Override
	public void logUserAction(HttpServletRequest request, UserAction action, ModelAndView page) {
		UserActionDAO dao = new UserActionDAO();
		setLogAffix(request, page, dao);
		dao.setAction(action.toString());
		dao.setView(page.getViewName());
		log.info(toJson(dao));
	}

	/**
	 * This method is used the basic information from the
	 * <code>HttpServletRequest<code>
	 * (@See <code>setLogAffix</code> )
	 * 
	 * @param request
	 *            the HttpServletRequest from the controller
	 * @param action
	 *            the UserAction performed in the controller
	 */
	@Override
	public void logUserAction(HttpServletRequest request, UserAction action) {
		UserActionDAO dao = new UserActionDAO();
		setLogAffix(request, null, dao);
		dao.setAction(action.toString());
		log.info(toJson(dao));
	}

	@Override
	public void logCustomUserAction(HttpServletRequest request, UserAction action, String logString) {
		CustomLogDAO dao = new CustomLogDAO();
		setLogAffix(request, null, dao);
		dao.setAction(action.toString());
		dao.setMessage(logString);
		log.info(toJson(dao));
	}

	@Override
	public void logStaticPageView(HttpServletRequest request, PageInfo pageType) {
		UserActionDAO dao = new UserActionDAO();
		setLogAffix(request, null, dao);
		dao.setAction(UserAction.STATICPAGE.toString());
		dao.setView(pageType.getTemplate());
		log.info(toJson(dao));
	}

	@Override
	public void logLanguageChange(HttpServletRequest request, Locale oldLocale, UserAction languageChange) {
		LanguageChangeDAO dao = new LanguageChangeDAO();
		setLogAffix(request, null, dao);
		dao.setAction(languageChange.toString());
		dao.setOldLanguage(oldLocale.getLanguage());
		log.info(toJson(dao));
	}

	@Override
	public void logBriefResultView(HttpServletRequest request, BriefBeanView briefBeanView, Query solrQuery, ModelAndView page) {
		BriefViewDAO dao = new BriefViewDAO();
		setLogAffix(request, page, dao);
		dao.setQueryType(solrQuery.getQueryType());

		if (briefBeanView != null) {
			dao.setPageNr(briefBeanView.getPagination().getPageNumber());
			dao.setNrResults(briefBeanView.getPagination().getNumFound());
			dao.setLanguageFacets(briefBeanView.getFacetLogs().get("LANGUAGE"));
			dao.setCountryFacet(briefBeanView.getFacetLogs().get("COUNTRY"));
			if (briefBeanView.getPagination() != null) {
				dao.setQuery(briefBeanView.getPagination().getPresentationQuery().getUserSubmittedQuery());
			}
			if (solrQuery.getRefinements() != null) {
				dao.setQueryConstraints(StringUtils.join(solrQuery.getRefinements(), ","));
			}
		}

		UserAction userAction = UserAction.BRIEF_RESULT;
		Map<String, String[]> params = RequestUtils.getParameterMap(request); 
		if (params.containsKey("bt")) {
			if (request.getParameter("bt").equalsIgnoreCase("pacta")) {
				userAction = UserAction.BRIEF_RESULT_FROM_PACTA;
			} else if (request.getParameter("bt").equalsIgnoreCase("savedSearch")) {
				userAction = UserAction.BRIEF_RESULT_FROM_SAVED_SEARCH;
			}
		} else if (params.containsKey("rtr")
				&& request.getParameter("rtr").equalsIgnoreCase("true")) {
			userAction = UserAction.RETURN_TO_RESULTS;
		}
		dao.setUserAction(userAction.toString());
		dao.setView(page.getViewName());

		log.info(toJson(dao));
	}

	@Override
	public void logFullResultView(HttpServletRequest request, UserAction userAction, FullBeanView fullResultView, ModelAndView page, String europeanaUri) {
		FullViewDAO dao = new FullViewDAO();
		setLogAffix(request, page, dao);
		dao.setEuropeanaUri(europeanaUri);

		try {
			DocIdWindowPager idWindowPager = fullResultView.getDocIdWindowPager();
			dao.setQuery(idWindowPager.getQuery());
			dao.setStartPage(idWindowPager.getFullDocUriInt());
			dao.setNumFound(idWindowPager.getDocIdWindow().getHitCount());
		} catch (UnsupportedEncodingException e) {
			// TODO decide what to do with this error
		} catch (Exception e) {
			// TODO decide what to do with this error
		}

		Map<String, String[]> params = RequestUtils.getParameterMap(request); 
		if (params.containsKey("bt")) {
			if (request.getParameter("bt").equalsIgnoreCase("savedItem")) {
				userAction = UserAction.FULL_RESULT_FROM_SAVED_ITEM;
			} else if (request.getParameter("bt").equalsIgnoreCase("savedTag")) {
				userAction = UserAction.FULL_RESULT_FROM_SAVED_TAG;
			} else if (request.getParameter("bt").equalsIgnoreCase("bob")) {
				userAction = UserAction.FULL_RESULT_FROM_YEAR_GRID;
			} else if (request.getParameter("bt").equalsIgnoreCase("tlv")) {
				userAction = UserAction.FULL_RESULT_FROM_TIME_LINE_VIEW;
			}
		}
		dao.setUserAction(userAction.toString());
		log.info(toJson(dao));
	}

	private static void setLogAffix(HttpServletRequest request, ModelAndView page, AffixDAO dao) {
		dao.setIp(request.getRemoteAddr());
		dao.setReqUrl(getRequestUrl(request));
		User user = null;
		if (page != null) {
			PageData model = (PageData)page.getModel().get(PageData.PARAM_MODEL);
			user = (User)model.getUser();
		}
		String userId;
		if (user != null) {
			userId = user.getId().toString();
		} else {
			userId = "";
		}
		dao.setUserId(userId);
		dao.setLanguage(ControllerUtil.getLocale(request).toString());
		dao.setUserAgent(request.getHeader("User-Agent"));
		dao.setReferer(request.getHeader("referer"));
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equalsIgnoreCase("__utma")) {
					dao.setUtma(cookie.getValue());
				} else if (cookie.getName().equalsIgnoreCase("__utmb")) {
					dao.setUtmb(cookie.getValue());
				} else if (cookie.getName().equalsIgnoreCase("__utmc")) {
					dao.setUtmc(cookie.getValue());
				} else if (cookie.getName().equalsIgnoreCase("track_session")) {
					dao.setTracSession(cookie.getValue());
				}
			}
		}
	}

	private static String getRequestUrl(HttpServletRequest request) {
		String base = ControllerUtil.getFullServletUrl(request);
		String queryStringParameters = request.getQueryString();
		Map<String, String[]> postParameters = RequestUtils.getParameterMap(request); 
		StringBuilder out = new StringBuilder();
		out.append(base);
		String queryString;
		if (queryStringParameters != null) {
			out.append("?").append(queryStringParameters);
			queryString = out.toString();
		} else if (postParameters.size() > 0) {
			out.append("?");
			for (Entry<String, String[]> entry : postParameters.entrySet()) {
				String key = entry.getKey().toString();
				String[] values = (String[]) entry.getValue();
				for (String value : values) {
					out.append(key).append("=").append(value).append("&");
				}
			}
			queryString = out.toString().substring(0, out.toString().length() - 1);
		} else {
			queryString = out.toString();
		}
		return queryString;
	}

	private static String toJson(Object object) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Inclusion.NON_NULL);
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonGenerationException e) {
			log.error("Json Generation Exception: " + e.getMessage(),e);
		} catch (JsonMappingException e) {
			log.error("Json Mapping Exception: " + e.getMessage(),e);
		} catch (IOException e) {
			log.error("I/O Exception: " + e.getMessage(),e);
		}
		return null;
	}
}
