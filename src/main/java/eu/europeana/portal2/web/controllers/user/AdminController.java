package eu.europeana.portal2.web.controllers.user;

import java.util.Locale;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.db.service.ApiKeyService;
import eu.europeana.corelib.db.service.UserService;
import eu.europeana.corelib.definitions.db.entity.relational.ApiKey;
import eu.europeana.corelib.definitions.db.entity.relational.User;
import eu.europeana.portal2.services.Configuration;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.AdminPage;
import eu.europeana.portal2.web.presentation.model.LimitApiKeyPage;
import eu.europeana.portal2.web.presentation.model.RegisterApiPage;
import eu.europeana.portal2.web.util.ClickStreamLogger;
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

	@Resource(name="apiKeyService") private ApiKeyService apiKeyService;

	@Resource private ClickStreamLogger clickStreamLogger;

	private final Logger log = Logger.getLogger(getClass().getName());

	@RequestMapping("/admin.html")
	public ModelAndView adminHandler(
			HttpServletRequest request,
			HttpServletResponse response,
			Locale locale)
					throws Exception {
		log.info("==== admin.html ====");
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
	
	@RequestMapping("/admin/removeUser.html")
	public String removeUserHandler(
			@RequestParam(value = "id", required = true) int id,
			HttpServletRequest request,
			HttpServletResponse response,
			Locale locale)
					throws Exception {
		log.info("==== admin.html ====");

		User user = userService.findByID(id);
		userService.remove(user);

		return "redirect:/admin.html";
	}

	/*
	@RequestMapping("/admin/blockUser.html")
	public String blockUserHandler(
			@RequestParam(value = "id", required = true) int id,
			HttpServletRequest request,
			HttpServletResponse response,
			Locale locale)
					throws Exception {
		log.info("==== admin.html ====");

		User user = userService.findByID(id);
		user.setEnabled(false);
		userService.store(user);

		return "redirect:/admin.html";
	}
	*/

	@RequestMapping("/admin/removeApiKey.html")
	public String removeApiKeyHandler(
			@RequestParam(value = "userId", required = true) long userId,
			@RequestParam(value = "apiKey", required = true) String apiKey,
			HttpServletRequest request,
			HttpServletResponse response,
			Locale locale)
					throws Exception {
		log.info("==== admin/removeApiKey.html ====");
		log.info(String.format("%s, %s", userId, apiKey));

		userService.removeApiKey(userId, apiKey);

		return "redirect:/admin.html";
	}

	@RequestMapping(value = "/admin/limitApiKey.html", method = RequestMethod.GET)
	public ModelAndView limitApiKeyHandler(
			@RequestParam(value = "userId", required = true) long userId,
			@RequestParam(value = "apiKey", required = true) String apiKeyId,
			@ModelAttribute("model") LimitApiKeyPage model,
			HttpServletRequest request,
			HttpServletResponse response,
			Locale locale)
					throws Exception {
		log.info("==== /admin/limitApiKey.html ====");
		config.registerBaseObjects(request, response, locale);
		config.injectProperties(model);

		ApiKey apiKey = apiKeyService.findByID(apiKeyId);
		model.setApiKey(apiKey.getId());
		model.setUsageLimit(apiKey.getUsageLimit());

		clickStreamLogger.logUserAction(request, ClickStreamLogger.UserAction.REGISTER_API);
		ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.ADMIN_LIMIT_APIKEY);
		config.postHandle(this, page);

		return page;
	}

	@RequestMapping(value = "/admin/limitApiKey.html", method = RequestMethod.POST)
	public String limitApiKeyHandler(
			@ModelAttribute("model") LimitApiKeyPage model,
			HttpServletRequest request,
			HttpServletResponse response,
			Locale locale)
					throws Exception {
		log.info("==== admin.html ====");

		ApiKey apiKey = apiKeyService.findByID(model.getApiKey());
		apiKey.setUsageLimit(model.getUsageLimit());
		apiKeyService.store(apiKey);

		return "redirect:/admin.html";
	}

}
