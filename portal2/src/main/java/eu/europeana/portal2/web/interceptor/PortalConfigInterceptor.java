package eu.europeana.portal2.web.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

import eu.europeana.corelib.db.service.UserService;
import eu.europeana.corelib.definitions.db.entity.relational.User;
import eu.europeana.corelib.web.model.PageData;
import eu.europeana.portal2.web.presentation.model.PortalPageData;
import eu.europeana.portal2.web.util.ControllerUtil;

public class PortalConfigInterceptor extends HandlerInterceptorAdapter {

	@Value("#{europeanaProperties['portal.name']}")
	private String portalName;

	@Value("#{europeanaProperties['portal.server']}")
	private String portalServer;

	@Value("#{europeanaProperties['portal.theme']}")
	private String defaultTheme;

	@Value("#{europeanaProperties['portal.google.plus.publisher.id']}")
	private static String portalGooglePlusPublisherId;
	
	@Resource
	private UserService userService;

	private String portalUrl;

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView)
			throws Exception {
		super.postHandle(request, response, o, modelAndView);
		if (modelAndView != null && !modelAndView.getViewName().startsWith("redirect:")
				&& modelAndView.getModel().containsKey(PageData.PARAM_MODEL)) {
			PortalPageData model = (PortalPageData) modelAndView.getModel().get(PageData.PARAM_MODEL);

			model.setGooglePlusPublisherId(StringUtils.trimToEmpty(portalGooglePlusPublisherId));
			model.setTheme(getTheme(request));
			model.setLocale(RequestContextUtils.getLocaleResolver(request).resolveLocale(request));
			// model.setMinify(false);
			User user = ControllerUtil.getUser(userService);
			model.setUser(user);
			model.setPortalUrl(getPortalUrl());
		}
	}

	private String getTheme(HttpServletRequest request) {
		return ControllerUtil.getSessionManagedTheme(request, defaultTheme);
	}

	public String getPortalUrl() {
		if (portalUrl == null) {
			StringBuilder sb = new StringBuilder(portalServer);
			if (!portalServer.endsWith("/") && !portalName.startsWith("/")) {
				sb.append("/");
			}
			sb.append(portalName);
			portalUrl = sb.toString();
		}
		return portalUrl;
	}

}
