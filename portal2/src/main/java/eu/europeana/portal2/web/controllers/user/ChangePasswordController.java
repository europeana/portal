package eu.europeana.portal2.web.controllers.user;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
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
import eu.europeana.corelib.logging.Log;
import eu.europeana.corelib.solr.exceptions.EuropeanaQueryException;
import eu.europeana.corelib.web.service.EmailService;
import eu.europeana.corelib.web.support.Configuration;
import eu.europeana.portal2.services.ClickStreamLogService;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.ChangePasswordPage;
import eu.europeana.portal2.web.util.ControllerUtil;
import eu.europeana.portal2.web.validators.ChangePasswordPageValidator;

/**
 * This Controller allows people to change their passwords
 * 
 */

@Controller
@RequestMapping("/change-password.html")
public class ChangePasswordController {

	@Log
	private Logger log;

	@Resource
	private EmailService emailService;

	@Resource
	private UserService userService;

	@Resource
	private Configuration config;

	@Resource
	private TokenService tokenService;

	@Resource
	private ClickStreamLogService clickStreamLogger;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new ChangePasswordPageValidator());
	}

	@RequestMapping(method = RequestMethod.GET)
	protected ModelAndView getMethod(
			@RequestParam("token") String tokenKey,
			@ModelAttribute("model") ChangePasswordPage model, 
			HttpServletRequest request,
			Locale locale) throws Exception {
		log.info("=========== change-password.html POST =================");

		if (tokenKey == null) {
			clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.ERROR_NO_TOKEN);
			throw new EuropeanaQueryException(ProblemType.UNKNOWN_TOKEN);
		}
		Token token = tokenService.findByID(tokenKey);
		if (token == null) {
			clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.ERROR_TOKEN_EXPIRED);
			// FIXME: This is forwarding to a non-existing view???
			return ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.MYEU_TOKEN);
		}
		model.setToken(token.getToken());
		model.setEmail(token.getEmail());
		clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.CHANGE_PASSWORD_SUCCES);
		return ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.MYEU_PASS_CHANGE);
	}

	@RequestMapping(method = RequestMethod.POST)
	protected ModelAndView post(
			@Valid @ModelAttribute("model") ChangePasswordPage model, 
			BindingResult result,
			HttpServletRequest request, 
			Locale locale) throws Exception {
		log.info("=========== change-password.html POST =================");
		if (result.hasErrors()) {
			log.error("The change password form has errors");
			clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.CHANGE_PASSWORD_FAILURE);
			return ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.MYEU_PASS_CHANGE);
		}

		// token is validated in handleRequestInternal
		Token token = tokenService.findByID(model.getToken());
		if (token == null) {
			log.error("Expected to find token.");
			clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.REGISTER_FAILURE);
			throw new RuntimeException("Expected to find token.");
		}

		// don't use email from the form. use token.
		User user = userService.findByEmail(token.getEmail());
		if (user == null) {
			log.error("Expected to find the user.");
			clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.REGISTER_FAILURE);
			throw new RuntimeException("Expected to find user for " + token.getEmail());
		}

		// remove token. it can not be used any more.
		tokenService.remove(token);
		// now update the user
		String oldPw = (StringUtils.isBlank(user.getPassword())) ? model.getPassword() : user.getPassword();
		log.info("pw: " + oldPw);
		userService.changePassword(user.getId(), oldPw, model.getPassword());
		// userService.store(user);
		sendNotificationEmail(user);

		clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.REGISTER_SUCCESS);
		return ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.MYEU_PASS_CHANGED);
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