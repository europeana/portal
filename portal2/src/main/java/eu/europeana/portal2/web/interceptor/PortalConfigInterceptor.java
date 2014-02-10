package eu.europeana.portal2.web.interceptor;

import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

import eu.europeana.corelib.db.service.UserService;
import eu.europeana.corelib.definitions.db.entity.relational.User;
import eu.europeana.corelib.web.model.PageData;
import eu.europeana.corelib.web.support.Configuration;
import eu.europeana.portal2.web.presentation.model.PortalPageData;
import eu.europeana.portal2.web.util.ControllerUtil;

public class PortalConfigInterceptor extends HandlerInterceptorAdapter {

	@Resource
	private UserService userService;

	@Resource
	private AbstractMessageSource messageSource;
	
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
			// set locale message when required
			if (!hasCookie(request, "portalLanguage") && 			
					(request.getLocale() != null) && !locale.equals(request.getLocale())) {
				Locale browser = request.getLocale();
				model.setBrowserLocale(browser);
				String languageLabel = "language_" + browser.getLanguage() + "_t";
				StringBuilder sb = new StringBuilder();
				sb.append(messageSource.getMessage("browser_language_preference1_t", null, browser));
				sb.append(messageSource.getMessage(languageLabel, null, browser)).append(", ");
				sb.append(messageSource.getMessage("browser_language_preference2_t", null, browser));
				sb.append(messageSource.getMessage(languageLabel, null, browser)).append("?");
				model.setLocaleMessage(sb.toString());
			}
		}
	}
	
	public boolean hasCookie(HttpServletRequest request, String cookiename) {
		for (Cookie cookie: request.getCookies()) {
			if (StringUtils.equalsIgnoreCase(cookiename, cookie.getName())) {
				return true;
			}
		}
		return false;
	}

}
