package eu.europeana.portal2.web.controllers.user;

import java.util.Locale;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.db.entity.relational.UserImpl;
import eu.europeana.corelib.db.exception.DatabaseException;
import eu.europeana.corelib.db.service.TokenService;
import eu.europeana.corelib.db.service.UserService;
import eu.europeana.corelib.definitions.db.entity.relational.Token;
import eu.europeana.corelib.definitions.db.entity.relational.User;
import eu.europeana.corelib.definitions.exception.ProblemType;
import eu.europeana.corelib.solr.exceptions.EuropeanaQueryException;
import eu.europeana.corelib.web.service.EmailService;
import eu.europeana.portal2.services.Configuration;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.RegisterPage;
import eu.europeana.portal2.web.util.ClickStreamLogger;
import eu.europeana.portal2.web.util.ControllerUtil;

/**
 * During registration, users click on an email link to end up here with a
 * "token" that allows them to proceed with registration. They have to choose a
 * user name, password etc.
 * 
 * @author Gerald de Jong <geralddejong@gmail.com>
 */

@Controller
@RequestMapping("/register.html")
public class RegisterPageController {

	@Resource(name="corelib_db_userService") private UserService userService;

	@Resource(name="corelib_db_tokenService") private TokenService tokenService;

	@Resource(name="corelib_web_emailService") private EmailService emailService;

	@Resource private ClickStreamLogger clickStreamLogger;

	@Resource(name="configurationService") private Configuration config;

	private final Logger log = Logger.getLogger(getClass().getName());

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new RegistrationFormValidator());
	}

	@RequestMapping(method = RequestMethod.GET)
	protected ModelAndView getRequest(
			@RequestParam("token") String tokenKey,
			@ModelAttribute("model") RegisterPage model,
			HttpServletRequest request,
			HttpServletResponse response,
			Locale locale)
					throws EuropeanaQueryException, DatabaseException {
		log.info("================= /register.html GET ==================");
		config.registerBaseObjects(request, response, locale);
		config.injectProperties(model);

		log.info("Received get request, putting token into registration form model attribute: " + tokenKey);
		Token token = tokenService.findByID(tokenKey);
		// when token is null, show useful message
		if (token == null) {
			throw new EuropeanaQueryException(ProblemType.UNKNOWN_TOKEN);
		}
		model.setToken(token.getToken());
		model.setEmail(token.getEmail());
		clickStreamLogger.logUserAction(request, ClickStreamLogger.UserAction.REGISTER);
		ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.MYEU_REGISTER);
		config.postHandle(this, page);

		return page;
	}

	@RequestMapping(method = RequestMethod.POST)
	protected ModelAndView formSubmit(
			@Valid @ModelAttribute("model") RegisterPage model,
			BindingResult result, 
			HttpServletRequest request, 
			HttpServletResponse response, 
			Locale locale)
					throws EuropeanaQueryException, DatabaseException {
		log.info("================= /register.html POST ==================");
		config.registerBaseObjects(request, response, locale);
		config.injectProperties(model);
		if (result.hasErrors()) {
			log.info("The registration form has errors");
			clickStreamLogger.logUserAction(request, ClickStreamLogger.UserAction.REGISTER_FAILURE);
			ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.MYEU_REGISTER);
			config.postHandle(this, page);
			return page;
		}

		User user = userService.create(model.getToken(), model.getUserName(), model.getPassword());
		sendNotificationEmail(user);

		clickStreamLogger.logUserAction(request, ClickStreamLogger.UserAction.REGISTER_SUCCESS);
		ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.MYEU_REGISTERED);
		config.postHandle(this, page);

		return page;
	}

	private void sendNotificationEmail(User user) {
		try {
			emailService.sendRegisterNotify(user);
		} catch (Exception e) {
			log.severe("Unable to send email to sys " + e.getLocalizedMessage());
		}
	}

	public class RegistrationFormValidator implements Validator {

		@Override
		public boolean supports(Class<?> aClass) {
			return RegisterPage.class.equals(aClass);
		}

		@Override
		public void validate(Object o, Errors errors) {
			RegisterPage form = (RegisterPage) o;

			final int PASS_MIN = 6;
			final int PASS_MAX = 30;

			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", "username.required", "Username is required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.required", "Password is required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password2", "password2.required", "Repeat Password is required");

			if (!validUserName(form.getUserName())) {
				errors.rejectValue("userName", "username.invalidChars", "Username may only contain letters, digits, spaces and underscores.");
			}
			if (form.getUserName() != null) {

				if (!validUserName(form.getUserName()) && form.getUserName().length() > UserImpl.FIELDSIZE_USERNAME) {
					// TODO standard error code?
					errors.rejectValue("userName", "username.tooLong", String.format("Username is too long, it should be max %d characters.", UserImpl.FIELDSIZE_USERNAME)); 
				}

				if (!validUserName(form.getUserName()) && userService.findByName(form.getUserName()) != null) {
					errors.rejectValue("userName", "username.exists", "Username already exists.");
				}
			}

			if (form.getPassword() == null
					|| !form.getPassword().equals(form.getPassword2())) {
				errors.rejectValue("password", "password.mismatch", "Passwords do not match.");
			}
			if (form.getPassword() != null) {

				if (form.getPassword().length() < PASS_MIN) {
					errors.rejectValue("password", "password.length", String.format("Password is too short, it should be %d-%d character.", PASS_MIN, PASS_MAX));
				}

				if (form.getPassword().length() > PASS_MAX) {
					errors.rejectValue("password", "password.length", String.format("Password is too long, it should be %d-%d character.", PASS_MIN, PASS_MAX));
				}
			}

			if (!form.getDisclaimer()) {
				errors.rejectValue("disclaimer", "disclaimer.unchecked", "Disclaimer must be accepted.");
			}
		}

		private boolean validUserName(String userName) {
			if (userName == null) {
				return false;
			}
			// may only contain alphanumeric, spaces and underscore.
			for (int i = 0; i < userName.length(); i++) {
				char c = userName.charAt(i);
				if (!(Character.isLetterOrDigit(c) || c == ' ' || c == '_')) {
					return false;
				}
			}
			return true;
		}
	}
}