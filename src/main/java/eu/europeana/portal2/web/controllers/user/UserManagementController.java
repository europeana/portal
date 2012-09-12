package eu.europeana.portal2.web.controllers.user;

import java.util.Locale;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.db.service.UserService;
import eu.europeana.corelib.definitions.db.entity.relational.User;
import eu.europeana.corelib.web.interceptor.ConfigInterceptor;
import eu.europeana.portal2.services.Configuration;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.EmptyModelPage;
import eu.europeana.portal2.web.util.ControllerUtil;

@Controller
public class UserManagementController {

	@Resource(name="corelib_db_userService") private UserService userService;

	@Resource private ConfigInterceptor corelib_web_configInterceptor;

	@Resource(name="configurationService") private Configuration config;

	private final Logger log = Logger.getLogger(getClass().getName());

	@RequestMapping("/myeuropeana.html")
	public ModelAndView myEuropeanaHandler(
			@RequestParam(value = "theme", required = false, defaultValue="") String theme,
			HttpServletRequest request,
			HttpServletResponse response,
			Locale locale) 
					throws Exception {
		EmptyModelPage model = new EmptyModelPage();
		config.injectProperties(model, request);

		User user = ControllerUtil.getUser(userService);
		model.setUser(user);
		ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.MYEU);
		// clickStreamLogger.logUserAction(request, ClickStreamLogger.UserAction.MY_EUROPEANA);

		try {
			corelib_web_configInterceptor.postHandle(request, response, this, page);
		} catch (Exception e) {
			log.severe(e.getMessage());
			e.printStackTrace();
		}

		return page;
	}

	@RequestMapping("/logout-success.html")
	public String logoutSuccessHandler(HttpServletRequest request)
			throws Exception {
		// clickStreamLogger.logUserAction(request, ClickStreamLogger.UserAction.LOGOUT);
		return "redirect:/login.html";
	}

	@RequestMapping("/logout.html")
	public ModelAndView logoutHandler(
			HttpServletRequest request, 
			Locale locale)
					throws Exception {
		// clickStreamLogger.logUserAction(request, ClickStreamLogger.UserAction.LOGOUT);
		return ControllerUtil.createModelAndViewPage(new EmptyModelPage(), locale, PortalPageInfo.MYEU_LOGOUT);
	}
}
