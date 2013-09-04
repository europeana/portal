package eu.europeana.portal2.web.controllers.user;

import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
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

import eu.europeana.corelib.db.exception.DatabaseException;
import eu.europeana.corelib.db.service.ApiKeyService;
import eu.europeana.corelib.db.service.TokenService;
import eu.europeana.corelib.db.service.UserService;
import eu.europeana.corelib.definitions.db.entity.relational.ApiKey;
import eu.europeana.corelib.definitions.db.entity.relational.Token;
import eu.europeana.corelib.definitions.db.entity.relational.User;
import eu.europeana.corelib.definitions.exception.ProblemType;
import eu.europeana.corelib.logging.Log;
import eu.europeana.corelib.solr.exceptions.EuropeanaQueryException;
import eu.europeana.corelib.web.exception.EmailServiceException;
import eu.europeana.corelib.web.model.PageInfo;
import eu.europeana.corelib.web.service.EmailService;
import eu.europeana.portal2.services.ClickStreamLogService;
import eu.europeana.portal2.services.Configuration;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.RegisterApiPage;
import eu.europeana.portal2.web.util.ControllerUtil;

/**
 * During registration, users click on an email link to end up here with a "token" that allows them to proceed with
 * registration. They have to choose a user name, password etc.
 * 
 */
@Controller
@RequestMapping("/api/registration.html")
public class RegisterApiPageController {

	@Log
	private Logger log;

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

	@Resource
	private ApiKeyService apiKeyService;

	private static final String REQUEST_API = "RequestAPI";

	/**
	 * The default daily usage limit of API
	 */
	private static final long DEFAULT_USAGE_LIMIT = 10000;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new RegistrationFormValidator());
	}

	@RequestMapping(method = RequestMethod.GET)
	protected ModelAndView getRequest(
			@RequestParam(value = "token", required = false, defaultValue = "") String tokenKey,
			@ModelAttribute("model") RegisterApiPage model, 
			HttpServletRequest request,
			Locale locale) throws EuropeanaQueryException, DatabaseException {

		log.info("================= /api/registration.html GET ==================");

		ModelAndView page;
		if (StringUtils.isBlank(tokenKey)) {
			// request API form
			page = ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.API_REQUEST);
		} else {
			log.info("Received get request, putting token into registration form model attribute: " + tokenKey);
			Token token = tokenService.findByID(tokenKey);
			// when token is null, show useful message
			if (token == null) {
				throw new EuropeanaQueryException(ProblemType.UNKNOWN_TOKEN);
			}
			model.setToken(token.getToken());
			model.setEmail(token.getEmail());
			String apiKey = null;
			do {
				apiKey = generatePassPhrase(9);
			} while (!isUnique(apiKey));
			model.setApiKey(apiKey);
			model.setPrivateKey(generatePassPhrase(9));
			page = ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.API_REGISTER_FORM);
		}
		clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.REGISTER_API);
		return page;
	}

	@RequestMapping(method = RequestMethod.POST)
	protected ModelAndView formSubmit(
			@Valid @ModelAttribute("model") RegisterApiPage model, 
			BindingResult result,
			HttpServletRequest request, Locale locale) throws EuropeanaQueryException,
			DatabaseException {
		log.info("================= /api/registration.html POST ==================");

		if (result.hasErrors()) {
			log.info("The registration form has errors " + model.getFirstName());

			clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.REGISTER_API_FAILURE);
			return ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.API_REGISTER_FORM);
		}

		String baseUrl = config.getPortalUrl();

		log.info("requestedAction: " + model.getRequestedAction());
		// Register for API
		PageInfo target = null;
		if (model.getRequestedAction().equals(REQUEST_API)) {

			if (!ControllerUtil.validEmailAddress(model.getEmail())) {
				model.setFailureFormat(true);
			} else {
				log.info("Sending API registration email");
				Token token = tokenService.create(model.getEmail());
				String url = baseUrl + "/api/registration.html";
				log.info("token: " + token);
				log.info("registerUri: " + url);
				try {
					emailService.sendApiToken(token, url);
				} catch (EmailServiceException e) {
					log.error("Error during API token sending. " + e.getLocalizedMessage());
					e.printStackTrace();
				}
				model.setSuccess(true);
				target = PortalPageInfo.API_REQUEST;
			}
		} else {
			log.info("Creating API user");
			String token = model.getToken();
			log.info("token: " + token);
			apiKeyService.createApiKey(token, model.getEmail(), model.getApiKey(), model.getPrivateKey(),
					DEFAULT_USAGE_LIMIT,
					model.getEmail(), // use email for username
					model.getCompany(), model.getCountry(), model.getFirstName(), model.getLastName(),
					model.getWebsite(), model.getAddress(), model.getPhone(), model.getFieldOfWork());
			ApiKey apiKey = apiKeyService.findByID(model.getApiKey());

			log.info("User: " + apiKey.getUser());
			log.info("ApiKey: " + apiKey);
			tokenService.remove(tokenService.findByID(token));
			log.info("token is removed: " + model.getToken());
			sendNotificationEmails(apiKey.getUser(), apiKey);
			target = PortalPageInfo.API_REGISTER_SUCCESS;
		}

		clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.REGISTER_API_SUCCESS);
		return ControllerUtil.createModelAndViewPage(model, locale, target);
	}

	private void sendNotificationEmails(User user, ApiKey apiKey) {
		log.info("Sending emails about the successfull API registration");
		try {
			emailService.sendRegisterApiNotifyUser(apiKey);
			emailService.sendRegisterApiNotifyAdmin(user);
		} catch (Exception e) {
			log.error("Unable to send email to sys: " + e.getLocalizedMessage(),e);
		}
	}

	private String generatePassPhrase(int length) {
		// This variable contains the list of allowable characters for the
		// pass phrase. Note that the number 0 and the letter 'O' have been
		// removed to avoid confusion between the two. The same is true
		// of 'I', 1, and 'l'.
		final char[] allowableCharacters = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'm',
				'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
				'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4',
				'5', '6', '7', '8', '9' };

		final int max = allowableCharacters.length - 1;

		StringBuilder pass = new StringBuilder();

		for (int i = 0; i < length; i++) {
			pass.append(allowableCharacters[RandomUtils.nextInt(max)]);
		}

		return pass.toString();
	}

	private boolean isUnique(String apiKey) {
		try {
			return (apiKeyService.findByID(apiKey) == null);
		} catch (DatabaseException e) {
			log.error("DatabaseException while finding apikey: " + e.getLocalizedMessage(),e);
		}
		return false;
	}

	public class RegistrationFormValidator implements Validator {

		@Override
		public boolean supports(Class<?> aClass) {
			return RegisterApiPage.class.equals(aClass);
		}

		@Override
		public void validate(Object o, Errors errors) {
			RegisterApiPage form = (RegisterApiPage) o;
			final int PASS_MIN = 6;
			final int PASS_MAX = 30;

			if (form.getRequestedAction().equals(REQUEST_API)) {
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "apiKey.required",
						"Email address is required");
				if (!ControllerUtil.validEmailAddress(form.getEmail())) {
					errors.rejectValue("email", "email.invalid", "Invalid email address.");
				}
				return;
			}

			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "apiKey", "apiKey.required", "API key is required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "privateKey", "privateKey.required",
					"Private key is required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "firstName.required",
					"First name is required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "lastName.required", "Last name is required");
			if (!validApiKey(form.getApiKey())) {
				errors.rejectValue("apiKey", "apiKey.invalidChars", "API key may only contain letters, and digits.");
			}

			if (form.getApiKey() != null) {
				if (form.getApiKey().length() > PASS_MAX) {
					errors.rejectValue("apiKey", "apiKey.tooLong",
							String.format("API Key is too long, it should be max %d characters.", PASS_MAX));
				}

				if (!isUnique(form.getApiKey())) {
					errors.rejectValue("apiKey", "apiKey.exists", "API Key already exists.");
				}
			}

			if (form.getPrivateKey() != null) {
				if (form.getPrivateKey().length() < PASS_MIN) {
					errors.rejectValue("privateKey", "privateKey.length", String.format(
							"Private key is too short, it should be %d-%d character.", PASS_MIN, PASS_MAX));
				}

				if (form.getPrivateKey().length() > PASS_MAX) {
					errors.rejectValue("privateKey", "privateKey.length",
							String.format("Private key is too long, it should be %d-%d character.", PASS_MIN, PASS_MAX));
				}
			}

			if (!form.getDisclaimer()) {
				errors.rejectValue("disclaimer", "disclaimer.unchecked", "Disclaimer must be accepted.");
			}
		}

		private boolean validApiKey(String apiKey) {
			if (apiKey == null) {
				return false;
			}
			// may only contain alphanumeric, spaces and underscore.
			for (int i = 0; i < apiKey.length(); i++) {
				char c = apiKey.charAt(i);
				if (!Character.isLetterOrDigit(c)) {
					return false;
				}
			}
			return true;
		}
	}
}