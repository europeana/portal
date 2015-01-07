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
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.db.service.TokenService;
import eu.europeana.corelib.db.service.UserService;
import eu.europeana.corelib.definitions.db.entity.relational.SavedItem;
import eu.europeana.corelib.definitions.db.entity.relational.SavedSearch;
import eu.europeana.corelib.definitions.db.entity.relational.SocialTag;
import eu.europeana.corelib.definitions.db.entity.relational.Token;
import eu.europeana.corelib.definitions.db.entity.relational.User;
import eu.europeana.corelib.utils.EuropeanaUriUtils;
import eu.europeana.corelib.web.service.EmailService;
import eu.europeana.corelib.web.support.Configuration;
import eu.europeana.portal2.services.BingTokenService;
import eu.europeana.portal2.services.ClickStreamLogService;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.LoginPage;
import eu.europeana.portal2.web.presentation.model.MyEuropeanaPage;
import eu.europeana.portal2.web.presentation.model.abstracts.SearchPageData;
import eu.europeana.portal2.web.util.ControllerUtil;

@Controller
public class MyEuropeanaController {

	private static final String REGISTER_FOR_MYEUROPEANA = "register-for-myeuropeana";
	private static final String REGISTER_API = "RegisterAPI";
	private static final String REQUEST_NEW_PASSWORD = "request-new-myeuropeana-password";
	private static final String INVALID_CREDENTIALS = "invalid_credentials_t";

	Logger log = Logger.getLogger(this.getClass());
	
	@Resource
	private UserService userService;

	@Resource
	private EmailService emailService;

	@Resource
	private TokenService tokenService;

	@Resource
	private Configuration config;

	@Resource
	private ClickStreamLogService clickStreamLogger;

	private BingTokenService bingTokenService = new BingTokenService();

	
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

	@RequestMapping(value={"/myeuropeana"}, method=RequestMethod.GET)
	public ModelAndView myEuropeanaIndexHandler(
		@RequestParam(value = "query", required = false, defaultValue="") String returnToQuery,
		@RequestParam(value = "qf", required = false) String[] returnToFacets,
		
		HttpServletRequest request,
		Locale locale
			) throws Exception {

				
		User user = ControllerUtil.getUser(userService);

		String bingToken = bingTokenService.getToken(config.getBingTranslateClientId(), config.getBingTranslateClientSecret());
		
		if (user != null) {
			MyEuropeanaPage model = new MyEuropeanaPage();
			model.setUser(user);

			model.setReturnToQuery(returnToQuery);
			model.setReturnToFacets(returnToFacets);

			List<SavedItem> savedItems = new ArrayList<SavedItem>(user.getSavedItems());
			Collections.sort(savedItems);
			model.setSavedItems(savedItems);

			List<SavedSearch> savedsearches = new ArrayList<SavedSearch>(user.getSavedSearches());
			Collections.sort(savedsearches);
			model.setSavedSearches(savedsearches);

			List<SocialTag> socialTags = new ArrayList<SocialTag>(user.getSocialTags());
			Collections.sort(socialTags);
			model.setSocialTags(socialTags);

			model.setKeywordLanguagesLimit(config.getKeywordLanguagesLimit());

			model.setBingToken(bingToken);
			
			clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.MY_EUROPEANA);
			return ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.MYEU_INDEX);
		} else {
			LoginPage model = new LoginPage();
			
			model.setReturnToQuery(returnToQuery);
			model.setReturnToFacets(returnToFacets);
			
			model.setKeywordLanguagesLimit(config.getKeywordLanguagesLimit());

			model.setErrorMessage("1".equals(request.getParameter("error")) ? INVALID_CREDENTIALS : null);
			
			model.setBingToken(bingToken);
			
			clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.LOGIN);
			return ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.MYEU_INDEX);
		}
	}

	@RequestMapping(value={"/myeuropeana"}, method=RequestMethod.POST)
	public ModelAndView handle(
		@RequestParam(value = "email", required = false) String email,
		@RequestParam(value = "requested-action", required = false) String requestedAction,
		HttpServletRequest request, Locale locale
	) throws Exception {
		log.info("===== login.html =======");
		LoginPage model = new LoginPage();
		model.setKeywordLanguagesLimit(config.getKeywordLanguagesLimit());

		model.setEmail(email);
		log.info("requestedAction: " + requestedAction);

		if (email != null) {
			String baseUrl = config.getPortalUrl();

			// Register for My Europeana
			if (REGISTER_FOR_MYEUROPEANA.equals(requestedAction)) {
				if (!ControllerUtil.validEmailAddress(email)) {
					model.setFailureFormat(true);
				} else if (emailExists(email)) {
					model.setFailureExists(true);
				} else {
					Token token = tokenService.create(email);
					String url = baseUrl + "/register.html";
					emailService.sendToken(token, url);
					model.setSuccess(true);
				}
			}

			// Register for API
			else if (REGISTER_API.equals(requestedAction)) {
				// if (!ControllerUtil.validEmailAddress(email)) {
				// model.setFailureFormat(true);
				// //} else if (emailExists(email)) {
				// // model.setFailureExists(true);
				// } else {
				Token token = tokenService.create(email);
				String url = baseUrl + "/register-api.html";
				emailService.sendToken(token, url);
				model.setSuccess(true);
				// }
			}

			// Request New Password
			else if (REQUEST_NEW_PASSWORD.equals(requestedAction)) {
				if (!ControllerUtil.validEmailAddress(email)) {
					model.setFailureForgotFormat(true);
				} else if (!emailExists(email)) {
					model.setFailureForgotDoesntExist(true);
				} else {
					Token token = tokenService.create(email);
					String url = baseUrl + "/change-password.html?token=" + token.getToken();
					if (model.getUser() != null) {
						emailService.sendForgotPassword(model.getUser(), url);
					} else {
						emailService.sendForgotPassword(email, url);
					}
					// tokenReplyEmailSender.sendEmail(email, registerUri, "forgotPassword");
					model.setForgotSuccess(true);
				}
			}

			// Unknown button
			else {
				throw new IllegalArgumentException("MyEuropeanaController: requested-action not handled");
			}
		}

		// boolean register = true;
		// page.addObject("register", register);
		//model.setErrorMessage("1".equals(request.getParameter("error")) ? "Invalid Credentials" : null);
		//ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.MYEU_INDEX);
		//clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.LOGIN, page);
		//return page;
		clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.LOGIN);
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
						sb.append(EuropeanaUriUtils.encode(pair.getName()));
					} else {
						sb.append(pair.getName()).append("=").append(EuropeanaUriUtils.encode(pair.getValue()));
					}
				}

				search.setQuery(sb.toString());
			} else {
				search.setQuery(EuropeanaUriUtils.encode(search.getQuery()));
			}
		}
		return search;
	}

	private boolean emailExists(String email) {
		User user = userService.findByEmail(email);
		return (user != null && !StringUtils.isBlank(user.getPassword()));
	}
}
