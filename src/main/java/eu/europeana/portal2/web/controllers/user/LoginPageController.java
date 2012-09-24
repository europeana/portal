package eu.europeana.portal2.web.controllers.user;

import java.util.Locale;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.db.service.TokenService;
import eu.europeana.corelib.db.service.UserService;
import eu.europeana.corelib.definitions.db.entity.relational.Token;
import eu.europeana.corelib.web.service.EmailService;
import eu.europeana.portal2.services.Configuration;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.LoginPage;
import eu.europeana.portal2.web.util.ClickStreamLogger;
import eu.europeana.portal2.web.util.ControllerUtil;

@Controller
public class LoginPageController {

	@Resource(name="corelib_web_emailService") private EmailService emailService;

	@Resource(name="corelib_db_userService") private UserService userService;

	@Resource(name="configurationService") private Configuration config;

	@Resource(name="corelib_db_tokenService") private TokenService tokenService;

	@Resource private ClickStreamLogger clickStreamLogger;

	private final Logger log = Logger.getLogger(getClass().getName());

	@RequestMapping("/login.html")
	public ModelAndView handle(
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "submit_login", required = false) String buttonPressed,
			@RequestParam(value = "theme", required = false, defaultValue="") String theme,
			HttpServletRequest request, 
			HttpServletResponse response, 
			Locale locale) 
					throws Exception {
		config.registerBaseObjects(request, response, locale);
		log.info("===== login.html =======");
		LoginPage model = new LoginPage();
		config.injectProperties(model);

		model.setEmail(email);
		log.info("email: " + email);
		log.info("buttonPressed: " + buttonPressed);

		if (email != null) {
			String registerUri = config.getPortalServer() + "/" + config.getPortalName(); //request.getRequestURL().toString();

			// Register
			// TODO this value is internationalized in the template
			if ("Register".equals(buttonPressed)) {
				if (!ControllerUtil.validEmailAddress(email)) {
					model.setFailureFormat(true);
				} else if (emailExists(email)) {
					model.setFailureExists(true);
				} else {
					Token token = tokenService.create(email);
					registerUri = registerUri + "/register.html";
					log.info("token: " + token);
					log.info("registerUri: " + registerUri);
					emailService.sendToken(token, registerUri);
					model.setSuccess(true);
				}
			}

			// Forgot Password
			else if ("Request".equals(buttonPressed)) {
				if (!ControllerUtil.validEmailAddress(email)) {
					model.setFailureForgotFormat(true);
				} else if (!emailExists(email)) {
					model.setFailureForgotDoesntExist(true);
				} else {
					registerUri = registerUri + "/change-password.html";
					log.info("registerUri: " + registerUri);
					// tokenReplyEmailSender.sendEmail(email, registerUri, "forgotPassword");
					model.setForgotSuccess(true);
				}
			}

			// Unknown button
			else {
				throw new IllegalArgumentException("Expected a button press to give submit_login=[Register|Request]");
			}
		}

		// boolean register = true;
		// page.addObject("register", register);
		model.setErrorMessage("1".equals(request.getParameter("error")) ? "Invalid Credentials" : null);
		ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.MYEU_LOGIN);
		config.postHandle(this, page);
		clickStreamLogger.logUserAction(request, ClickStreamLogger.UserAction.LOGIN, page);

		return page;
	}

	private boolean emailExists(String email) {
		return userService.findByEmail(email) != null;
	}
}
