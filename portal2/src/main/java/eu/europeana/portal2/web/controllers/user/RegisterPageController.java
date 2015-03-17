package eu.europeana.portal2.web.controllers.user;

import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
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
import eu.europeana.corelib.edm.exceptions.EuropeanaQueryException;
import eu.europeana.corelib.web.service.EmailService;
import eu.europeana.corelib.web.support.Configuration;
import eu.europeana.portal2.services.ClickStreamLogService;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.RegisterPage;
import eu.europeana.portal2.web.util.ControllerUtil;

/**
 * During registration, users click on an email link to end up here with a "token" that allows them to proceed with
 * registration. They have to choose a user name, password etc.
 * 
 * @author Gerald de Jong <geralddejong@gmail.com>
 */
@Controller
@RequestMapping("/register.html")
public class RegisterPageController {
Logger log = Logger.getLogger(this.getClass());
	@Resource
	private UserService userService;

	@Resource
	private TokenService tokenService;

	@Resource
	private EmailService emailService;

	@Resource
	private ClickStreamLogService clickStreamLogger;

	@Resource
	private Configuration config;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new RegistrationFormValidator());
	}

	@RequestMapping(method = RequestMethod.GET)
	protected ModelAndView getRequest(
			@RequestParam("token") String tokenKey,
			@ModelAttribute("model") RegisterPage model, 
			HttpServletRequest request,
			Locale locale) throws EuropeanaQueryException, DatabaseException {
		log.info("================= /register.html GET ==================");
		log.info("Received get request, putting token into registration form model attribute: " + tokenKey);
		Token token = tokenService.findByID(tokenKey);
		// when token is null, show useful message
		if (token == null) {
			throw new EuropeanaQueryException(ProblemType.UNKNOWN_TOKEN);
		}
		model.setToken(token.getToken());
		model.setEmail(token.getEmail());
		clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.REGISTER);
		return ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.MYEU_REGISTER);
	}

	@RequestMapping(method = RequestMethod.POST)
	protected ModelAndView formSubmit(
			@Valid @ModelAttribute("model") RegisterPage model, 
			BindingResult result,
			HttpServletRequest request, Locale locale) throws EuropeanaQueryException,
			DatabaseException {
		log.info("================= /register.html POST ==================");
		if (result.hasErrors()) {
			log.info("The registration form has errors");
			clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.REGISTER_FAILURE);
			return ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.MYEU_REGISTER);
		}

		Token token = tokenService.findByID(model.getToken());
		if (token == null) {
			throw new DatabaseException(ProblemType.TOKEN_INVALID);
		}

		User user = userService.findByEmail(token.getEmail());
		if (user == null) {
			user = userService.create(model.getToken(), model.getUserName(), model.getPassword());
		} else {
			user = userService.registerApiUserForMyEuropeana(user.getId(), model.getUserName(), model.getPassword());
		}
		sendNotificationEmail(user);

		clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.REGISTER_SUCCESS);
		return ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.MYEU_REGISTERED);
	}

	private void sendNotificationEmail(User user) {
		try {
			emailService.sendRegisterNotify(user);
		} catch (Exception e) {
			log.error("Unable to send email to sys " + e.getLocalizedMessage());
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
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password2", "password2.required",
					"Repeat Password is required");

			if (!validUserName(form.getUserName())) {
				errors.rejectValue("userName", "username.invalidChars",
						"Username may only contain letters, digits, spaces and underscores.");
			}
			if (form.getUserName() != null) {

				if (!validUserName(form.getUserName()) && form.getUserName().length() > UserImpl.FIELDSIZE_USERNAME) {
					// TODO standard error code?
					errors.rejectValue("userName", "username.tooLong", String.format(
							"Username is too long, it should be max %d characters.", UserImpl.FIELDSIZE_USERNAME));
				}

				if (!validUserName(form.getUserName()) && userService.findByName(form.getUserName()) != null) {
					errors.rejectValue("userName", "username.exists", "Username already exists.");
				}
			}

			if (form.getPassword() == null || !form.getPassword().equals(form.getPassword2())) {
				errors.rejectValue("password", "password.mismatch", "Passwords do not match.");
			}
			if (form.getPassword() != null) {

				if (form.getPassword().length() < PASS_MIN) {
					errors.rejectValue("password", "password.length",
							String.format("Password is too short, it should be %d-%d character.", PASS_MIN, PASS_MAX));
				}

				if (form.getPassword().length() > PASS_MAX) {
					errors.rejectValue("password", "password.length",
							String.format("Password is too long, it should be %d-%d character.", PASS_MIN, PASS_MAX));
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