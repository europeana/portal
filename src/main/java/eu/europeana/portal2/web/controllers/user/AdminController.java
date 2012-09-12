package eu.europeana.portal2.web.controllers.user;

import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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

	@Resource(name="configurationService") private Configuration config;

	@RequestMapping("/admin.html")
	public ModelAndView adminHandler(
			HttpServletRequest request,
			HttpServletResponse response,
			Locale locale)
					throws Exception {
		AdminPage model = new AdminPage();
		model.setTheme("devel");
		config.injectProperties(model, request);
		return ControllerUtil.createModelAndViewPage(model, PortalPageInfo.ADMIN);
	}
}
