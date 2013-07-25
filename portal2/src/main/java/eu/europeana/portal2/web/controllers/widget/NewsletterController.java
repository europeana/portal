package eu.europeana.portal2.web.controllers.widget;

import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.db.service.UserService;
import eu.europeana.portal2.services.ClickStreamLogService;
import eu.europeana.portal2.services.Configuration;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.EmptyModelPage;
import eu.europeana.portal2.web.util.ControllerUtil;

@Controller
public class NewsletterController {

	@Resource
	private UserService userService;

	@Resource
	private Configuration config;

	@Resource
	private ClickStreamLogService clickStreamLogger;

	@RequestMapping("/newsletter.html")
	public ModelAndView myEuropeanaHandler(
			@RequestParam(value = "theme", required = false, defaultValue = "") String theme,
			HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		EmptyModelPage model = new EmptyModelPage();

		model.setUser(ControllerUtil.getUser(userService));
		clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.NEWSLETTER);
		return ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.NEWSLETTER);
	}
}
