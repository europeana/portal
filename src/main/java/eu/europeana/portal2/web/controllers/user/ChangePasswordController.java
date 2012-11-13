package eu.europeana.portal2.web.controllers.user;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.db.service.TokenService;
import eu.europeana.corelib.db.service.UserService;
import eu.europeana.corelib.definitions.db.entity.relational.Token;
import eu.europeana.corelib.definitions.db.entity.relational.User;
import eu.europeana.corelib.definitions.exception.ProblemType;
import eu.europeana.corelib.solr.exceptions.EuropeanaQueryException;
import eu.europeana.corelib.web.service.EmailService;
import eu.europeana.portal2.services.Configuration;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.ChangePasswordPage;
import eu.europeana.portal2.web.presentation.model.validation.ChangePasswordPageValidator;
import eu.europeana.portal2.web.util.ClickStreamLogger;
import eu.europeana.portal2.web.util.ControllerUtil;
import eu.europeana.portal2.web.util.Injector;


/**
 * This Controller allows people to change their passwords
 * 
 * @author Gerald de Jong <geralddejong@gmail.com>
 */

@Controller
@RequestMapping("/change-password.html")
public class ChangePasswordController {

	@Resource(name="corelib_web_emailService") private EmailService emailService;

	@Resource(name="corelib_db_userService") private UserService userService;

	@Resource(name="configurationService") private Configuration config;

	@Resource(name="corelib_db_tokenService") private TokenService tokenService;

	@Resource private ClickStreamLogger clickStreamLogger;

	private final Logger log = Logger.getLogger(getClass().getName());

	// @Autowired @Qualifier("emailSenderForPasswordChangeNotify") private EmailSender notifyEmailSender;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new ChangePasswordPageValidator());
	}

	@RequestMapping(method = RequestMethod.GET)
	protected ModelAndView getMethod(
			@RequestParam("token") String tokenKey,
			@ModelAttribute("model") ChangePasswordPage model,
			HttpServletRequest request, 
			HttpServletResponse response, 
			Locale locale
				) throws Exception {
		Injector injector = new Injector(request, response, locale);
		log.info("=========== change-password.html POST =================");
		injector.injectProperties(model);

		if (tokenKey == null) {
			clickStreamLogger.logUserAction(request, ClickStreamLogger.UserAction.ERROR_NO_TOKEN);
			throw new EuropeanaQueryException(ProblemType.UNKNOWN_TOKEN);
		}
		Token token = tokenService.findByID(tokenKey);
		if (token == null) {
			clickStreamLogger.logUserAction(request, ClickStreamLogger.UserAction.ERROR_TOKEN_EXPIRED);
			// FIXME: This is forwarding to a non-existing view???
			return ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.MYEU_TOKEN);
		}
		model.setToken(token.getToken());
		model.setEmail(token.getEmail());
		clickStreamLogger.logUserAction(request, ClickStreamLogger.UserAction.CHANGE_PASSWORD_SUCCES);
		ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.MYEU_PASS_CHANGE);
		injector.postHandle(this, page);

		return page;
	}

	@RequestMapping(method = RequestMethod.POST)
	protected ModelAndView post(
			@Valid @ModelAttribute("model") ChangePasswordPage model,
			BindingResult result,
			HttpServletRequest request, 
			HttpServletResponse response, 
			Locale locale)
					throws Exception {
		Injector injector = new Injector(request, response, locale);
		log.info("=========== change-password.html POST =================");
		injector.injectProperties(model);
		if (result.hasErrors()) {
			log.info("The change password form has errors");
			clickStreamLogger.logUserAction(request, ClickStreamLogger.UserAction.CHANGE_PASSWORD_FAILURE);
			return ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.MYEU_PASS_CHANGE);
		}
		// token is validated in handleRequestInternal
		Token token = tokenService.findByID(model.getToken()); 
		// don't use email from the form. use token.
		User user = userService.findByEmail(token.getEmail()); 
		if (user == null) {
			clickStreamLogger.logUserAction(request, ClickStreamLogger.UserAction.REGISTER_FAILURE);
			throw new RuntimeException("Expected to find user for " + token.getEmail());
		}
		user.setPassword(model.getPassword());
		// remove token. it can not be used any more.
		tokenService.remove(token);
		// now update the user
		userService.changePassword(user.getId(), user.getPassword(), model.getPassword());
		// userService.store(user); 
		sendNotificationEmail(user);

		clickStreamLogger.logUserAction(request, ClickStreamLogger.UserAction.REGISTER_SUCCESS);
		ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.MYEU_PASS_CHANGED);
		injector.postHandle(this, page);

		return page;
	}

	private void sendNotificationEmail(User user) {
		try {
			Map<String, Object> model = new TreeMap<String, Object>();
			model.put("user", user);
			// notifyEmailSender.sendEmail(model);
			// emailService.sendFeedback(email, feedback)
		} catch (Exception e) {
			log.warn("Unable to send email to " + user.getEmail() + ": " + e.getLocalizedMessage());
		}
	}
}