package eu.europeana.portal2.web.controllers.admin;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.db.service.ApiKeyService;
import eu.europeana.corelib.db.service.UserService;
import eu.europeana.corelib.definitions.db.entity.relational.ApiKey;
import eu.europeana.corelib.logging.Log;
import eu.europeana.corelib.logging.Logger;
import eu.europeana.corelib.web.support.Configuration;
import eu.europeana.portal2.services.ClickStreamLogService;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.LimitApiKeyPage;
import eu.europeana.portal2.web.util.ControllerUtil;

/**
 * 
 * Show monitoring and administration info.
 * 
 * @author Borys Omelayenko
 */

@Controller
@RequestMapping("/admin/limitApiKey.html")
public class AdminApiLimitController {

	
	@Resource
	private UserService userService;

	@Resource
	private Configuration config;

	@Resource
	private ApiKeyService apiKeyService;

	@Resource
	private ClickStreamLogService clickStreamLogger;
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView limitApiKeyFormHandler(
			@RequestParam(value = "apiKey", required = true) String apiKeyId,
			@ModelAttribute("model") LimitApiKeyPage model, 
			HttpServletRequest request) throws Exception {

		ApiKey apiKey = apiKeyService.findByID(apiKeyId);
		model.setApiKey(apiKey.getId());
		model.setUsageLimit(apiKey.getUsageLimit());

		clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.REGISTER_API);
		ModelAndView page = ControllerUtil.createModelAndViewPage(model, PortalPageInfo.ADMIN_LIMIT_APIKEY);

		return page;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String limitApiKeyHandler(@ModelAttribute("model") LimitApiKeyPage model) throws Exception {

		ApiKey apiKey = apiKeyService.findByID(model.getApiKey());
		apiKey.setUsageLimit(model.getUsageLimit());
		apiKeyService.store(apiKey);

		return "redirect:/admin.html";
	}

}
