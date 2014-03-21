package eu.europeana.portal2.web.controllers.user;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
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
import eu.europeana.portal2.web.util.QueryUtil;

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
		List<SavedSearch> cleanedList = new ArrayList<SavedSearch>();
		for (SavedSearch search : savedsearches) {
			cleanedList.add(cleanSavedSearch(search));
		}
		Collections.sort(cleanedList);
		model.setSavedSearches(cleanedList);

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

	public SavedSearch cleanSavedSearch(SavedSearch search) {
		if (StringUtils.contains(search.getQuery(), " ")
			|| StringUtils.contains(search.getQuery(), "&")
			|| StringUtils.contains(search.getQuery(), "\"")) {
			if (StringUtils.contains(search.getQuery(), "&qf=")) {
				StringBuilder sb = new StringBuilder();
				List<NameValuePair> queryParams = URLEncodedUtils.parse(search.getQuery(), Charset.forName("UTF-8"));

				for (NameValuePair pair : queryParams) {
					if (sb.length() > 0) {
						sb.append("&");
					}
					if (StringUtils.isBlank(pair.getValue())) {
						sb.append(QueryUtil.encode(pair.getName()));
					} else {
						sb.append(pair.getName()).append("=").append(QueryUtil.encode(pair.getValue()));
					}
				}

				search.setQuery(sb.toString());
			} else {
				search.setQuery(QueryUtil.encode(search.getQuery()));
			}
		}
		return search;
	}
}
