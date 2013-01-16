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
import eu.europeana.portal2.services.Configuration;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.LimitApiKeyPage;
import eu.europeana.portal2.web.util.ClickStreamLogger;
import eu.europeana.portal2.web.util.ControllerUtil;
import eu.europeana.portal2.web.util.Injector;

/**
 * 
 * Show monitoring and administration info.
 * 
 * @author Borys Omelayenko
 */

@Controller
@RequestMapping("/admin/limitApiKey.html")
public class AdminApiLimitController {

	@Resource(name="corelib_db_userService") private UserService userService;

	@Resource(name="configurationService") private Configuration config;

	@Resource(name="apiKeyService") private ApiKeyService apiKeyService;

	@Resource private ClickStreamLogger clickStreamLogger;

	private final Logger log = Logger.getLogger(getClass().getName());

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView limitApiKeyFormHandler(
			@RequestParam(value = "userId", required = true) long userId,
			@RequestParam(value = "apiKey", required = true) String apiKeyId,
			@ModelAttribute("model") LimitApiKeyPage model,
			HttpServletRequest request,
			HttpServletResponse response,
			Locale locale)
					throws Exception {
		log.info("==== /admin/limitApiKey.html ====");
		Injector injector = new Injector(request, response, locale);
		injector.injectProperties(model);

		ApiKey apiKey = apiKeyService.findByID(apiKeyId);
		model.setApiKey(apiKey.getId());
		model.setUsageLimit(apiKey.getUsageLimit());

		clickStreamLogger.logUserAction(request, ClickStreamLogger.UserAction.REGISTER_API);
		ModelAndView page = ControllerUtil.createModelAndViewPage(model, PortalPageInfo.ADMIN_LIMIT_APIKEY);
		injector.postHandle(this, page);

		return page;
	}

	@RequestMapping(method = RequestMethod.POST)
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
