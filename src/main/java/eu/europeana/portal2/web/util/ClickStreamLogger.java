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

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.web.model.PageInfo;
import eu.europeana.portal2.web.presentation.model.BriefBeanView;
import eu.europeana.portal2.web.presentation.model.FullBeanView;

/**
 * This interface is used add custom logging information to the
 * application log. These logs are used to trace user behavior with
 * special focus for how to best provide multilingual support for the
 * Europeana Users.
 * 
 * The idea is to log 
 * // [idPhd=1, userId="123", query="sjoerd", queryType="", constrains="country=, " ip=""] 
 * logTypeId=""
 * userId=""
 * query=""
 * queryType=
 * language= 
 * date= 
 * ip= pageId= state=
 * pageNr= NrResults= LanguageFacets="en(12), de(9), fi(1)"
 * CountryFacet="de(14), ..." templateName="" -.ftl
 * 
 * actions=[languageChange, Result, staticPage, saveItem, saveSearch,
 * SaveTag, register, Error, ReturnResults]
 * 
 * @author Sjoerd Siebinga <sjoerd.siebinga@gmail.com>
 */

public interface ClickStreamLogger {

	void logUserAction(HttpServletRequest request, UserAction action);

	void logUserAction(HttpServletRequest request, UserAction action, ModelAndView model);

	void logCustomUserAction(HttpServletRequest request, UserAction action, String logString);

	void logStaticPageView(HttpServletRequest request, PageInfo pageType);

	void logLanguageChange(HttpServletRequest request, Locale oldLocale, UserAction languageChange);

	void logBriefResultView(HttpServletRequest request, BriefBeanView briefBeanView, Query query, ModelAndView page);

	void logFullResultView(HttpServletRequest request, UserAction action, FullBeanView fullResultView, ModelAndView page, String europeanaUri);

	/**
	 * Enum for different user actions that can be logged.
	 */
	public enum UserAction {
		// TODO: add descriptions
		// language specific actions
		LANGUAGE_CHANGE,

		// search related actions
		BRIEF_RESULT, BRIEF_RESULT_FROM_PACTA, BRIEF_RESULT_FROM_SAVED_SEARCH, BRIEF_RESULT_FROM_OPEN_SEARCH, FULL_RESULT_HMTL, FULL_RESULT_JSON, FULL_RESULT_SRW, FULL_RESULT_EMBEDED, FULL_RESULT_FROM_SAVED_ITEM, FULL_RESULT_FROM_SAVED_TAG, FULL_RESULT_FROM_YEAR_GRID, FULL_RESULT_FROM_TIME_LINE_VIEW, MORE_LIKE_THIS, RETURN_TO_RESULTS, REDIRECT_OUTLINK, REDIRECT_TO_SECURE, TIMELINE, MAPVIEW, WIKIPEDIA, TAG_GRID, YEAR_GRID, BROWSE_BOB, BROWSE_ALL, SITE_MAP_XML,

		SHOW_SIWA_MENU,

		// ajax related actions
		SAVE_ITEM, SAVE_SEARCH, SAVE_SOCIAL_TAG, SAVE_SEARCH_TERM, SAVE_NOTIFICATION_EMBED, REMOVE_SAVED_ITEM, REMOVE_SAVED_SEARCH, REMOVE_SOCIAL_TAG, REMOVE_SEARCH_TERM, SEND_EMAIL_TO_FRIEND, TAG_AUTOCOMPLETE,

		// navigation related actions
		DIRECT_LINK,

		// user management related actions
		REGISTER, REGISTER_SUCCESS, REGISTER_FAILURE, 
		REGISTER_API, REGISTER_API_SUCCESS, REGISTER_API_FAILURE, 
		MY_EUROPEANA, UNREGISTER, CHANGE_PASSWORD_SUCCES, CHANGE_PASSWORD_FAILURE, LOGIN, LOGOUT, LOGOUT_COOKIE_THEFT,

		// errors
		ERROR, AJAX_ERROR, ERROR_TOKEN_EXPIRED, ERROR_NO_TOKEN, EXCEPTION_CAUGHT,

		// static pages
		STATICPAGE, CONTACT_PAGE, FEEDBACK_SEND, FEEDBACK_SEND_FAILURE, INDEXPAGE,

		// other
		NEWSLETTER;

		UserAction() {
		}
	}

	public enum LogTypeId {
		PHD_JS("Juliane Stiller's custom log format"),
		PHD_MG("Maria Gade's custom log format");

		private String description;

		LogTypeId(String description) {
			this.description = description;
		}

		public String getDescription() {
			return description;
		}
	}
}
