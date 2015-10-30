package eu.europeana.portal2.web.controllers.user;

import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.db.service.TokenService;
import eu.europeana.corelib.db.service.UserService;
import eu.europeana.corelib.definitions.db.entity.relational.Token;
import eu.europeana.corelib.definitions.db.entity.relational.User;
import eu.europeana.corelib.web.service.EmailService;
import eu.europeana.corelib.web.support.Configuration;
import eu.europeana.portal2.services.ClickStreamLogService;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.EmptyModelPage;
import eu.europeana.portal2.web.presentation.model.LoginPage;
import eu.europeana.portal2.web.util.ControllerUtil;

@Controller
public class LoginPageController {

	Logger log = Logger.getLogger(this.getClass());

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

	private static final String REGISTER = "Register";
	private static final String REGISTER_API = "RegisterAPI";
	private static final String REQUEST = "Request";

	@RequestMapping("/login.html")
	public ModelAndView handle(
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "requested_action", required = false) String requestedAction,
			HttpServletRequest request, Locale locale) throws Exception {
		log.info("===== login.html =======");
		LoginPage model = new LoginPage();

		model.setEmail(email);
		log.info("requestedAction: " + requestedAction);

		if (email != null) {
			//String baseUrl = config.getPortalUrl();
			String baseUrl = config.getPortalServer();
			if(baseUrl.endsWith("/")){
				baseUrl = baseUrl.substring(0, baseUrl.length()-1);
			}

			// Register
			if (REGISTER.equals(requestedAction)) {
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

			// Forgot Password
			else if (REQUEST.equals(requestedAction)) {
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
				throw new IllegalArgumentException("Expected a button press to give submit_login=[Register|Request]");
			}
		}

		// boolean register = true;
		// page.addObject("register", register);
		model.setErrorMessage("1".equals(request.getParameter("error")) ? "Invalid Credentials" : null);
		ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.MYEU_LOGIN);
		//clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.LOGIN, page);
		return page;
	}

	@RequestMapping("/logout-success.html")
	public String logoutSuccessHandler(HttpServletRequest request) throws Exception {
		//clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.LOGOUT);
		return "redirect:/login.html";
	}

	@RequestMapping("/logout.html")
	public ModelAndView logoutHandler(HttpServletRequest request, Locale locale)
			throws Exception {
		EmptyModelPage model = new EmptyModelPage();
		//clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.LOGOUT);
		return ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.MYEU_LOGOUT);
	}

	private boolean emailExists(String email) {
		User user = userService.findByEmail(email);
		return (user != null && !StringUtils.isBlank(user.getPassword()));
	}
}
