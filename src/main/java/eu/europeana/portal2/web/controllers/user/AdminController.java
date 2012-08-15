package eu.europeana.portal2.web.controllers.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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

	@RequestMapping("/admin.html")
	public ModelAndView adminHandler() throws Exception {
		return ControllerUtil.createModelAndViewPage(new AdminPage(), PortalPageInfo.ADMIN);
	}
}
