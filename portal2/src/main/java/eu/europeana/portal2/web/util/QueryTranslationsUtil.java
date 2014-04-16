package eu.europeana.portal2.web.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.WebUtils;

import eu.europeana.corelib.db.service.UserService;
import eu.europeana.corelib.definitions.db.entity.RelationalDatabase;
import eu.europeana.corelib.definitions.db.entity.relational.User;
import eu.europeana.corelib.solr.utils.SolrUtils;
import eu.europeana.corelib.utils.StringArrayUtils;
import eu.europeana.corelib.utils.model.LanguageVersion;

public class QueryTranslationsUtil {

	private UserService userService;

	private static final String PORTAL_LANGUAGE_COOKIE = "portalLanguage";
	private static final String SEARCH_LANGUAGES_COOKIE = "keywordLanguages";

	private HttpServletRequest request;
	private String query;
	private String[] qt;

	public QueryTranslationsUtil() {}

	public QueryTranslationsUtil(UserService userService, HttpServletRequest request, String query, String[] qt) {
		this.userService = userService;
		this.request = request;
		this.query = query;
		this.qt = qt;
	}

	public List<LanguageVersion> createQueryTranslations() {
		List<LanguageVersion> queryTranslations = null;
		if (StringArrayUtils.isNotBlank(qt)) {
			queryTranslations = createQueryTranslationsFromParams();
		} else {
			queryTranslations = translateQuery();
		}
		return queryTranslations;
	}

	private List<LanguageVersion> translateQuery() {
		List<String> translatableLanguages = getTranslatableLanguages();
		if (translatableLanguages != null) {
			return SolrUtils.translateQuery(query, translatableLanguages);
		}
		return null;
	}

	public List<LanguageVersion> createQueryTranslationsFromParams() {
		if (!preventQueryTranslation()) {
			return parseQueryTranslations();
		}
		return null;
	}

	private boolean preventQueryTranslation() {
		return qt.length == 1 && StringUtils.equals(qt[0], "false");
	}

	private List<LanguageVersion> parseQueryTranslations() {
		List<LanguageVersion> queryTranslations = new ArrayList<LanguageVersion>();
		for (String term : qt) {
			String[] parts = term.split(":", 2);
			queryTranslations.add(new LanguageVersion(parts[1], parts[0]));
		}
		return queryTranslations;
	}

	private List<String> getTranslatableLanguages() {
		User user = ControllerUtil.getUser(userService);
		List<String> languageCodes = getKeywordLanguages(user);
		String portalLanguage = getPortalLanguage(user);
		if (!languageCodes.contains(portalLanguage)) {
			languageCodes.add(portalLanguage);
		}
		return languageCodes;
	}

	private List<String> getKeywordLanguages(User user) {
		List<String> languageCodes = new ArrayList<String>();
		String rawLanguageCodes = null;
		if (user != null) {
			rawLanguageCodes = StringUtils.join(
					user.getLanguageSearch(), RelationalDatabase.SEARCH_LANGUAGES_SEPARATOR);
		} else {
			Cookie cookie = WebUtils.getCookie(request, SEARCH_LANGUAGES_COOKIE);
			if (cookie != null) {
				rawLanguageCodes = cookie.getValue();
			}
		}
		if (rawLanguageCodes != null) {
			languageCodes.addAll(Arrays.asList(
					StringUtils.split(rawLanguageCodes.trim(), RelationalDatabase.SEARCH_LANGUAGES_SEPARATOR)));
		}
		return languageCodes;
	}

	private String getPortalLanguage(User user) {
		String languageCode = null;
		if (user != null) {
			languageCode = user.getLanguagePortal();
		} else {
			Cookie cookie = WebUtils.getCookie(request, PORTAL_LANGUAGE_COOKIE);
			if (cookie != null) {
				languageCode = cookie.getValue();
			}
		}
		return languageCode;
	}
}
