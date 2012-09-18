package eu.europeana.portal2.web.controllers.user;

import java.util.Locale;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.db.service.UserService;
import eu.europeana.portal2.services.Configuration;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.AdminPage;
import eu.europeana.portal2.web.util.ControllerUtil;

/**
 * 
 * Show monitoring and administration info.
 * 
 * @author Borys Omelayenko
 */

@Controller
public class AdminController {

	// @Resource private LocaleInterceptor localeChangeInterceptor;
	@Resource(name="corelib_db_userService") private UserService userService;

	@Resource(name="configurationService") private Configuration config;

	private final Logger log = Logger.getLogger(getClass().getName());

	@RequestMapping("/admin.html")
	public ModelAndView adminHandler(
			HttpServletRequest request,
			HttpServletResponse response,
			Locale locale)
					throws Exception {
		config.registerBaseObjects(request, response, locale);
		// localeChangeInterceptor.preHandle(request, response, this);
		AdminPage model = new AdminPage();
		model.setTheme("devel");
		config.injectProperties(model);
		model.setUsers(userService.findAll());

		ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.ADMIN);
		config.postHandle(this, page);
		return page;
	}
}
