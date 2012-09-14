package eu.europeana.portal2.web.controllers.user;

import java.security.Principal;
import java.util.Locale;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.db.service.UserService;
import eu.europeana.corelib.definitions.db.entity.relational.User;
import eu.europeana.corelib.web.service.EmailService;
import eu.europeana.portal2.services.Configuration;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.ContactPage;
import eu.europeana.portal2.web.presentation.model.validation.ContactPageValidator;
import eu.europeana.portal2.web.util.ControllerUtil;

@Controller
@RequestMapping("/contact.html")
public class ContactPageController {

	@Resource(name="corelib_web_emailService") private EmailService emailService;
	
	@Resource(name="corelib_db_userService") private UserService userService;

	@Resource(name="configurationService") private Configuration config;

	private final Logger log = Logger.getLogger(getClass().getName());

	// @Autowired
	// private ClickStreamLogger clickStreamLogger;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new ContactPageValidator());
	}

	@ModelAttribute("model")
	public ContactPage createContactForm() {
		ContactPage form = new ContactPage();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = null;
		if (authentication != null) {
			Principal principal = (Principal)authentication.getPrincipal();
			user = userService.findByEmail(principal.getName());
		}
		if (user != null) {
			form.setEmail(user.getEmail());
		}
		return form;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView handleGet(
			@RequestParam(value = "theme", required = false, defaultValue="") String theme,
			HttpServletRequest request, 
			HttpServletResponse response, 
			Locale locale) {
		config.registerBaseObjects(request, response, locale);
		ContactPage model = createContactForm();
		config.injectProperties(model);
		ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.CONTACT);
		config.postHandle(this, page);
		return page;
	}

	@RequestMapping(method = RequestMethod.POST)
	protected ModelAndView handlePost(
			@ModelAttribute("model") @Valid ContactPage form,
			BindingResult result,
			@RequestParam(value = "theme", required = false, defaultValue="") String theme,
			HttpServletRequest request, 
			HttpServletResponse response, 
			Locale locale)
					throws Exception {
		config.registerBaseObjects(request, response, locale);
		if (result.hasErrors()) {
			// clickStreamLogger.logUserAction(request, ClickStreamLogger.UserAction.FEEDBACK_SEND_FAILURE);
		} else {
			// Map<String, Object> model = new TreeMap<String, Object>();
			// model.put("email", form.getEmail());
			// model.put("feedback", form.getFeedbackText());
			// model.put(EmailSender.TO_EMAIL, form.getEmail());
			// userFeedbackConfirmSender.sendEmail(model);
			emailService.sendFeedback(form.getEmail(), form.getFeedbackText());
			form.setSubmitMessage("Your feedback was successfully sent. Thank you!");
			// clickStreamLogger.logUserAction(request, ClickStreamLogger.UserAction.FEEDBACK_SEND);
		}
		// clickStreamLogger.logUserAction(request, ClickStreamLogger.UserAction.CONTACT_PAGE);
		config.injectProperties(form);
		ModelAndView page = ControllerUtil.createModelAndViewPage(form, locale, PortalPageInfo.CONTACT);
		config.postHandle(this, page);

		return page;
	}

}
