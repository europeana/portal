package eu.europeana.portal2.web.controllers.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.db.service.UserService;
import eu.europeana.corelib.definitions.db.entity.relational.SavedItem;
import eu.europeana.corelib.definitions.db.entity.relational.SavedSearch;
import eu.europeana.corelib.definitions.db.entity.relational.SocialTag;
import eu.europeana.corelib.definitions.db.entity.relational.User;
import eu.europeana.corelib.web.support.Configuration;
import eu.europeana.portal2.services.ClickStreamLogService;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.MyEuropeanaPage;
import eu.europeana.portal2.web.util.ControllerUtil;

@Controller
public class MyEuropeanaController {

	@Resource
	private UserService userService;

	@Resource
	private Configuration config;

	@Resource
	private ClickStreamLogService clickStreamLogger;

	@RequestMapping("/myeuropeana.html")
	public ModelAndView myEuropeanaHandler(
			HttpServletRequest request, Locale locale) throws Exception {
		MyEuropeanaPage model = new MyEuropeanaPage();

		User user = ControllerUtil.getUser(userService);
		model.setUser(user);

		List<SavedItem> savedItems = new ArrayList<SavedItem>(user.getSavedItems());
		Collections.sort(savedItems);
		model.setSavedItems(savedItems);

		List<SavedSearch> savedsearches = new ArrayList<SavedSearch>(user.getSavedSearches());
		Collections.sort(savedsearches);
		model.setSavedSearches(savedsearches);

		List<SocialTag> socialTags = new ArrayList<SocialTag>(user.getSocialTags());
		Collections.sort(socialTags);
		model.setSocialTags(socialTags);

		clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.MY_EUROPEANA);
		return ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.MYEU);
	}

	@RequestMapping({"/myeuropeana"})
	public ModelAndView myEuropeanaIndexHandler(HttpServletRequest request, Locale locale) throws Exception {
		MyEuropeanaPage model = new MyEuropeanaPage();

		User user = ControllerUtil.getUser(userService);
		model.setUser(user);

		if ( user != null ) {
			List<SavedItem> savedItems = new ArrayList<SavedItem>(user.getSavedItems());
			Collections.sort(savedItems);
			model.setSavedItems(savedItems);

			List<SavedSearch> savedsearches = new ArrayList<SavedSearch>(user.getSavedSearches());
			Collections.sort(savedsearches);
			model.setSavedSearches(savedsearches);

			List<SocialTag> socialTags = new ArrayList<SocialTag>(user.getSocialTags());
			Collections.sort(socialTags);
			model.setSocialTags(socialTags);
		}

		clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.MY_EUROPEANA);
		return ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.MYEU_INDEX);
	}
}
