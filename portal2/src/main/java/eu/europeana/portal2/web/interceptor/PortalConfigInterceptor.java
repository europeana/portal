package eu.europeana.portal2.web.interceptor;

import eu.europeana.corelib.db.service.UserService;
import eu.europeana.corelib.definitions.db.entity.relational.User;
import eu.europeana.corelib.web.model.PageData;
import eu.europeana.corelib.web.support.Configuration;
import eu.europeana.portal2.web.presentation.model.PortalPageData;
import eu.europeana.portal2.web.util.ControllerUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class PortalConfigInterceptor extends HandlerInterceptorAdapter {

	@Resource
	private UserService userService;

	@Resource
	private MessageSource messageSource;

	@Resource
	private Configuration configuration;

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView)
			throws Exception {
		super.postHandle(request, response, o, modelAndView);
		if (modelAndView != null && !modelAndView.getViewName().startsWith("redirect:")
				&& modelAndView.getModel().containsKey(PageData.PARAM_MODEL)) {
			Locale locale = RequestContextUtils.getLocaleResolver(request).resolveLocale(request); 
			PortalPageData model = (PortalPageData) modelAndView.getModel().get(PageData.PARAM_MODEL);

			model.setGooglePlusPublisherId(StringUtils.trimToEmpty(configuration.getPortalGooglePlusPublisherId()));
			model.setTheme(ControllerUtil.getSessionManagedTheme(request, configuration.getDefaultTheme()));
			model.setLocale(locale);
			// model.setMinify(false);
			User user = ControllerUtil.getUser(userService);
			model.setUser(user);

			model.setPortalUrl(configuration.getPortalUrl());
			model.setBlogFeedUrl(configuration.getBlogUrl());
			model.setMyEuropeanaUrl(configuration.getMyEuropeanaUrl());
			model.setDoTranslation(ControllerUtil.getBooleanBundleValue("notranslate_do_translations", messageSource, locale));

			// set locale message when required
			Locale browser = request.getLocale();
			String language = ((browser != null) && !StringUtils.equalsIgnoreCase(browser.getLanguage(), "undefined") ? StringUtils
					.left(browser.getLanguage(), 2) : null);
			if (!hasCookie(request, "portalLanguage") && language != null
					&& !StringUtils.equalsIgnoreCase(language, StringUtils.left(locale.getLanguage(), 2))) {
				String languageLabel = "language_" + language + "_t";
				if (hasMessage(languageLabel)) {
					model.setBrowserLanguage(language);
					StringBuilder sb = new StringBuilder();
					sb.append(messageSource.getMessage("browser_language_preference3_t", null, browser)).append("?");
					String[] messages = new String[3];
					messages[0] = sb.toString();
					messages[1] = messageSource.getMessage("yes_t", null, browser);
					messages[2] = messageSource.getMessage("no_t", null, browser);
					model.setLocaleMessages(messages);
				}
			}
		}
	}

	private boolean hasMessage(String label) {
		try {
			String m = messageSource.getMessage(label, null, Locale.ENGLISH);
			return StringUtils.isNotBlank(m) && !StringUtils.equals(m, label);
		} catch (NoSuchMessageException e) {
		}
		return false;
	}

	private boolean hasCookie(HttpServletRequest request, String cookiename) {
		if ( (request != null) && (request.getCookies() != null)) {
			for (Cookie cookie: request.getCookies()) {
				if (StringUtils.equalsIgnoreCase(cookiename, cookie.getName())) {
					return true;
				}
			}
		}
		return false;
	}

}
