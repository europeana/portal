package eu.europeana.portal2.web.controllers.user;

import java.util.Locale;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.db.service.UserService;
import eu.europeana.corelib.definitions.db.entity.relational.User;
import eu.europeana.corelib.web.interceptor.ConfigInterceptor;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.EmptyModelPage;
import eu.europeana.portal2.web.util.ControllerUtil;

@Controller
public class UserManagementController {

	private final Logger log = Logger.getLogger(getClass().getName());

	@Resource
	private ConfigInterceptor corelib_web_configInterceptor;

	@Value("#{europeanaProperties['portal.theme']}")
	private String defaultTheme;

	@Resource(name="corelib_db_userService")
	private UserService userService;

	@RequestMapping("/myeuropeana.html")
	public ModelAndView myEuropeanaHandler(
			@RequestParam(value = "theme", required = false, defaultValue="") String theme,
			HttpServletRequest request,
			HttpServletResponse response,
			Locale locale) 
					throws Exception {
		EmptyModelPage model = new EmptyModelPage();
		model.setTheme(ControllerUtil.getSessionManagedTheme(request, theme, defaultTheme));

		User user = ControllerUtil.getUser(userService);
		model.setUser(user);
		/*
		if (user != null) {
			ControllerUtil.setUser(userDao.updateUser(user));
		}
		*/
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
