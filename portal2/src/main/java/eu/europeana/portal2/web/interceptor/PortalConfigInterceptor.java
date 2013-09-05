package eu.europeana.portal2.web.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

import eu.europeana.corelib.db.service.UserService;
import eu.europeana.corelib.definitions.db.entity.relational.User;
import eu.europeana.corelib.web.model.PageData;
import eu.europeana.portal2.services.Configuration;
import eu.europeana.portal2.web.presentation.model.PortalPageData;
import eu.europeana.portal2.web.util.ControllerUtil;

public class PortalConfigInterceptor extends HandlerInterceptorAdapter {

	@Resource
	private UserService userService;
	
	@Resource
	private Configuration configuration;

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView)
			throws Exception {
		super.postHandle(request, response, o, modelAndView);
		if (modelAndView != null && !modelAndView.getViewName().startsWith("redirect:")
				&& modelAndView.getModel().containsKey(PageData.PARAM_MODEL)) {
			PortalPageData model = (PortalPageData) modelAndView.getModel().get(PageData.PARAM_MODEL);

			model.setGooglePlusPublisherId(StringUtils.trimToEmpty(configuration.getPortalGooglePlusPublisherId()));
			model.setTheme(ControllerUtil.getSessionManagedTheme(request, configuration.getDefaultTheme()));
			model.setLocale(RequestContextUtils.getLocaleResolver(request).resolveLocale(request));
			// model.setMinify(false);
			User user = ControllerUtil.getUser(userService);
			model.setUser(user);
			model.setPortalUrl(configuration.getPortalUrl());
			model.setBlogFeedUrl(configuration.getBlogUrl());
		}
	}

}
