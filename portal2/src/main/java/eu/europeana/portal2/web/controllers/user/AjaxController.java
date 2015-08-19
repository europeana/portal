package eu.europeana.portal2.web.controllers.user;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.BatchUpdateException;
import java.util.Arrays;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.db.exception.DatabaseException;
import eu.europeana.corelib.db.service.ApiKeyService;
import eu.europeana.corelib.db.service.UserService;
import eu.europeana.corelib.definitions.db.entity.RelationalDatabase;
import eu.europeana.corelib.definitions.db.entity.relational.User;
import eu.europeana.corelib.web.support.Configuration;
import eu.europeana.portal2.services.ClickStreamLogService;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.AjaxPage;
import eu.europeana.portal2.web.presentation.model.data.FieldSize;
import eu.europeana.portal2.web.util.ControllerUtil;

@Controller
public class AjaxController {

	Logger log = Logger.getLogger(this.getClass());

	@Resource
	private UserService userService;

	@Resource
	private ApiKeyService apiKeyService;

	@Resource
	private Configuration config;

	@Resource
	private ClickStreamLogService clickStreamLogger;

	@RequestMapping("/remove.ajax")
	public ModelAndView handleAjaxRemoveRequest(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AjaxPage model = new AjaxPage();
		try {
			if (!hasJavascriptInjection(request)) {
				processAjaxRemoveRequest(model, request);
			}
		} catch (Exception e) {
			handleAjaxException(model, e, response);
		}
		return createResponsePage(model);
	}

	@RequestMapping("/save.ajax")
	public ModelAndView handleAjaxSaveRequest(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AjaxPage model = new AjaxPage();
		
		boolean isSocialTag = false;
		try {
			if (!hasJavascriptInjection(request)) {
				isSocialTag = processAjaxSaveRequest(model, request);
			}
		}
		catch (Exception e) {
			handleAjaxException(model, e, response);
		}
		if(isSocialTag){
			return createResponsePageSocialTags(model, getStringParameter("europeanaUri", FieldSize.EUROPEANA_URI, request)  );
		}
		else{
			return createResponsePage(model);			
		}
	}

	private boolean processAjaxSaveRequest(AjaxPage model, HttpServletRequest request) throws Exception {
		
		User	user		= ControllerUtil.getUser(userService);
		boolean	isSocialTag	= false;
		
		String modAction = request.getParameter("modificationAction");
		if (StringUtils.isBlank(modAction)) {
			String message = "Expected 'modificationAction' parameter!";
			log.error(message);
			throw new IllegalArgumentException(message);
		}

		switch (findModifiable(modAction)) {
		case SAVED_ITEM:
			user = saveItem(request, user);
			break;
		// className=SavedSearch&query=query%3Dparish&queryString=parish
		case SAVED_SEARCH:
			user = saveSearch(request, user);
			break;
		case SOCIAL_TAG:
			isSocialTag = true;
			user = saveSocialTag(request, user);
			break;
		case API_KEY:
			saveApiKey(request, user);
			break;
		case USER_LANGUAGE_SEARCH:
			user = saveKeywordLanguages(request, user);
			break;
		case USER_LANGUAGE_ITEM:
			user = saveItemLanguage(request, user);
			break;
		case USER_LANGUAGE_SETTINGS:
			user = saveKeywordLanguages(request, user);
			user = saveItemLanguage(request, user);
			break;
		default:
			String message = "Unhandled ajax action: " + modAction;
			log.error(message);
			throw new IllegalArgumentException(message);
		}

		model.setUser(user);
		model.setSuccess(true);
		
		return isSocialTag;
	}

	private User saveItem(HttpServletRequest request, User user)
			throws DatabaseException {
		String uri;
		uri = getStringParameter("europeanaUri", FieldSize.EUROPEANA_URI, request);
		user = userService.createSavedItem(user.getId(), uri);
		clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.SAVE_ITEM);
		return user;
	}

	private User saveSearch(HttpServletRequest request, User user)
			throws UnsupportedEncodingException, DatabaseException {
		String query = getStringParameter("query", FieldSize.QUERY, request);
		String queryString = URLDecoder.decode(getStringParameter("queryString", FieldSize.QUERY_STRING, request),
				"utf-8");
		user = userService.createSavedSearch(user.getId(), query, queryString);
		clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.SAVE_SEARCH);
		return user;
	}

	private User saveSocialTag(HttpServletRequest request, User user)
			throws UnsupportedEncodingException, DatabaseException {
		String uri;
		uri = getStringParameter("europeanaUri", FieldSize.EUROPEANA_URI, request);
		String tag = URLDecoder.decode(getStringParameter("tag", FieldSize.TAG, request), "utf-8");
		user = userService.createSocialTag(user.getId(), uri, tag);
		clickStreamLogger.logCustomUserAction(request, ClickStreamLogService.UserAction.SAVE_SOCIAL_TAG, "tag=" + tag);
		return user;
	}

	private void saveApiKey(HttpServletRequest request, User user)
			throws UnsupportedEncodingException, DatabaseException {
		String key = URLDecoder.decode(getStringParameter("apikey", FieldSize.TAG, request), "utf-8");
		String appName = URLDecoder.decode(getStringParameter("appName", FieldSize.TAG, request), "utf-8");
		apiKeyService.updateApplicationName(key, appName);
	}

	private User saveItemLanguage(HttpServletRequest request, User user)
			throws DatabaseException {
		String langCode = getStringParameter("itemLanguage", FieldSize.LANGUAGE_ITEM, request);
		user = userService.updateUserLanguageItem(user.getId(), langCode);
		return user;
	}

	private User saveKeywordLanguages(HttpServletRequest request, User user)
			throws DatabaseException {
		String languageCode = getStringParameter("keywordLanguages", FieldSize.LANGUAGE_SEARCH, request);
		languageCode = normalizeLanguageCodes(languageCode);
		user = userService.updateUserLanguageSearch(user.getId(), languageCode);
		return user;
	}

	private String normalizeLanguageCodes(String langCodeString) {
		String[] langCodes = StringUtils.split(langCodeString, RelationalDatabase.SEARCH_LANGUAGES_SEPARATOR);
		if (langCodes.length > config.getKeywordLanguagesLimit()) {
			langCodes = Arrays.copyOfRange(langCodes, 0, config.getKeywordLanguagesLimit());
			langCodeString = StringUtils.join(langCodes, RelationalDatabase.SEARCH_LANGUAGES_SEPARATOR);
		}
		return langCodeString;
	}

	private void processAjaxRemoveRequest(AjaxPage model, HttpServletRequest request) throws Exception {
		User user = ControllerUtil.getUser(userService);
		String modAction = request.getParameter("modificationAction");
		String idString = request.getParameter("id");
		if (StringUtils.isBlank(modAction) || StringUtils.isBlank(idString)) {
			throw new IllegalArgumentException("Expected 'className' and 'id' parameters!");
		}
		Long id = Long.valueOf(idString);

		switch (findModifiable(modAction)) {
		case SAVED_ITEM:
			userService.removeSavedItem(user.getId(), id);
			clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.REMOVE_SAVED_ITEM);
			break;
		case SAVED_SEARCH:
			userService.removeSavedSearch(user.getId(), id);
			clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.REMOVE_SAVED_SEARCH);
			break;
		case SOCIAL_TAG:
			userService.removeSocialTag(user.getId(), id);
			clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.REMOVE_SOCIAL_TAG);
			break;
		default:
			throw new IllegalArgumentException("Unhandled removable");
		}
		model.setUser(user);
		model.setSuccess(true);
	}

	private Modifiable findModifiable(String modificationName) {
		Modifiable mod = Modifiable.valueOf(StringUtils.upperCase(modificationName));
		if (mod == null) {
			throw new IllegalArgumentException("Unable to find modify action with name " + modificationName);
		}
		return mod;
	}

	private enum Modifiable {
		SAVED_ITEM, SAVED_SEARCH, SOCIAL_TAG, API_KEY,
		USER_LANGUAGE_PORTAL, USER_LANGUAGE_ITEM, USER_LANGUAGE_SEARCH, USER_LANGUAGE_SETTINGS;
	}

	private boolean hasJavascriptInjection(HttpServletRequest request) {
		boolean hasJavascript = false;
		for (Object o : request.getParameterMap().keySet()) {
			// log.info(String.valueOf(o));
			// TODO: rething this. Using "<" is a little bit cryptic
			if (request.getParameter(String.valueOf(o)).contains("<")) {
				hasJavascript = true;
				log.warn("The request contains javascript so do not process this request");
				break;
			}
		}
		return hasJavascript;
	}

	/**
	 * Returns a HTTP parameter up to a given length
	 *
	 * @param parameterName
	 * @param maximumLength
	 * @param request
	 * @return
	 */
	protected static String getStringParameter(String parameterName, int maximumLength, HttpServletRequest request) {
		String stringValue = request.getParameter(parameterName);
		if (stringValue == null) {
			throw new IllegalArgumentException("Missing parameter: " + parameterName);
		}
		if (stringValue.length() >= maximumLength) {
			stringValue = stringValue.substring(0, maximumLength);
		}
		stringValue = stringValue.trim();
		return stringValue;
	}

	private void handleAjaxException(AjaxPage model, Exception e, HttpServletResponse response) {
		model.setSuccess(false);
		model.setException(getStackTrace(e));
		response.setStatus(400);
		// clickStreamLogger.logUserAction(request, ClickStreamLogger.UserAction.AJAX_ERROR);
		log.error("Problem handling AJAX request: " + e);
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement el : e.getStackTrace()) {
			sb.append(el.toString() + "\n");
		}
		log.error(sb.toString());
	}

	private String getStackTrace(Exception exception) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		for (Throwable e = exception.getCause(); e != null; e = e.getCause()) {
			if (e instanceof BatchUpdateException) {
				BatchUpdateException bue = (BatchUpdateException) e;
				Exception next = bue.getNextException();
				log.warn("Next exception in batch: " + next);
				next.printStackTrace(printWriter);
			}
		}
		exception.printStackTrace(printWriter);
		return stringWriter.toString();
	}

	private static ModelAndView createResponsePage(AjaxPage model) {
		return ControllerUtil.createModelAndViewPage(model, PortalPageInfo.AJAX);
	}

    private static ModelAndView createResponsePageSocialTags(AjaxPage model, String eUri) {
        model.setDocument(eUri);
        return ControllerUtil.createModelAndViewPage(model, PortalPageInfo.AJAX_TAGS);
    }

}
