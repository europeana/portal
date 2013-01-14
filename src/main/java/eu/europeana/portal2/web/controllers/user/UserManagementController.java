package eu.europeana.portal2.web.controllers.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import eu.europeana.portal2.services.Configuration;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.EmptyModelPage;
import eu.europeana.portal2.web.presentation.model.MyEuropeanaPage;
import eu.europeana.portal2.web.util.ClickStreamLogger;
import eu.europeana.portal2.web.util.ControllerUtil;
import eu.europeana.portal2.web.util.Injector;

@Controller
public class UserManagementController {

	@Resource(name="corelib_db_userService") private UserService userService;

	@Resource(name="configurationService") private Configuration config;

	@Resource private ClickStreamLogger clickStreamLogger;

	private final Logger log = Logger.getLogger(getClass().getName());

	@RequestMapping("/myeuropeana.html")
	public ModelAndView myEuropeanaHandler(
			@RequestParam(value = "theme", required = false, defaultValue="") String theme,
			HttpServletRequest request,
			HttpServletResponse response,
			Locale locale) 
					throws Exception {
		log.info("===== myeuropeana.html =======");
		Injector injector = new Injector(request, response, locale);
		MyEuropeanaPage model = new MyEuropeanaPage();
		injector.injectProperties(model);

		User user = ControllerUtil.getUser(userService);
		model.setUser(user);

		List savedItems = new ArrayList(user.getSavedItems());
		Collections.sort(savedItems);
		model.setSavedItems(savedItems);

		List savedsearches = new ArrayList(user.getSavedSearches());
		Collections.sort(savedsearches);
		model.setSavedSearches(savedsearches);

		List socialTags = new ArrayList(user.getSocialTags());
		Collections.sort(socialTags);
		model.setSocialTags(socialTags);

		ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.MYEU);
		injector.postHandle(this, page);
		clickStreamLogger.logUserAction(request, ClickStreamLogger.UserAction.MY_EUROPEANA);

		return page;
	}

	@RequestMapping("/logout-success.html")
	public String logoutSuccessHandler(HttpServletRequest request)
			throws Exception {
		clickStreamLogger.logUserAction(request, ClickStreamLogger.UserAction.LOGOUT);
		return "redirect:/login.html";
	}

	@RequestMapping("/logout.html")
	public ModelAndView logoutHandler(
			HttpServletRequest request,
			HttpServletResponse response,
			Locale locale)
					throws Exception {
		Injector injector = new Injector(request, response, locale);
		EmptyModelPage model = new EmptyModelPage();
		injector.injectProperties(model);
		ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.MYEU_LOGOUT);
		injector.postHandle(this, page);
		clickStreamLogger.logUserAction(request, ClickStreamLogger.UserAction.LOGOUT);

		return page;
	}
}
