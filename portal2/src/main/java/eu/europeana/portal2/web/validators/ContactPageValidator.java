package eu.europeana.portal2.web.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import eu.europeana.portal2.web.presentation.model.ContactPage;
import eu.europeana.portal2.web.util.ControllerUtil;

public class ContactPageValidator implements Validator {

	@Override
	public boolean supports(Class<?> aClass) {
		return ContactPage.class.equals(aClass);
	}

	@Override
	public void validate(Object o, Errors errors) {
		ContactPage form = (ContactPage) o;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, 
			"email", "email.required", "Email is required"
		);

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, 
			"feedbackText", "feedbackText.required", "Please enter some feedback text"
		);

		if (!ControllerUtil.validEmailAddress(form.getEmail())) {
			errors.rejectValue("email", "email.invalidEmail", "Please enter a valid email address");
		}
	}
}