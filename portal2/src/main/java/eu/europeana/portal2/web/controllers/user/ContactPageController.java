package eu.europeana.portal2.web.controllers.user;

import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.db.service.UserService;
import eu.europeana.corelib.definitions.db.entity.relational.User;
import eu.europeana.corelib.web.service.EmailService;
import eu.europeana.portal2.services.ClickStreamLogService;
import eu.europeana.portal2.services.Configuration;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.ContactPage;
import eu.europeana.portal2.web.security.Portal2UserDetails;
import eu.europeana.portal2.web.util.ControllerUtil;
import eu.europeana.portal2.web.validators.ContactPageValidator;

@Controller
@RequestMapping("/contact.html")
public class ContactPageController {

	@Resource
	private EmailService emailService;

	@Resource
	private UserService userService;

	@Resource
	private Configuration config;

	@Resource
	private ClickStreamLogService clickStreamLogger;

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
			// Principal principal = (Principal)authentication.getPrincipal();
			Object principal = (Object) authentication.getPrincipal();
			if (principal instanceof Portal2UserDetails) {
				user = userService.findByEmail(((Portal2UserDetails) principal).getUsername());
			}
		}
		if (user != null) {
			form.setEmail(user.getEmail());
		}
		return form;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView handleGet(Locale locale) {
		ContactPage model = createContactForm();
		return ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.CONTACT);
	}

	@RequestMapping(method = RequestMethod.POST)
	protected ModelAndView handlePost(
			@ModelAttribute("model") @Valid ContactPage form, 
			BindingResult result,
			HttpServletRequest request, Locale locale) throws Exception {
		if (result.hasErrors()) {
			clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.FEEDBACK_SEND_FAILURE);
		} else {
			emailService.sendFeedback(form.getEmail(), form.getFeedbackText());
			form.setSubmitMessage("Your feedback was successfully sent. Thank you!");
			clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.FEEDBACK_SEND);
		}
		clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.CONTACT_PAGE);
		return ControllerUtil.createModelAndViewPage(form, locale, PortalPageInfo.CONTACT);
	}
}
