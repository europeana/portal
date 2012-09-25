/*
 * Copyright 2007-2012 The Europeana Foundation
 *
 *  Licenced under the EUPL, Version 1.1 (the "Licence") and subsequent versions as approved 
 *  by the European Commission;
 *  You may not use this work except in compliance with the Licence.
 *  
 *  You may obtain a copy of the Licence at:
 *  http://joinup.ec.europa.eu/software/page/eupl
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under 
 *  the Licence is distributed on an "AS IS" basis, without warranties or conditions of 
 *  any kind, either express or implied.
 *  See the Licence for the specific language governing permissions and limitations under 
 *  the Licence.
 */

package eu.europeana.portal2.web.presentation.model.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import eu.europeana.portal2.web.presentation.model.ChangePasswordPage;

public class ChangePasswordPageValidator implements Validator {

	@Override
	public boolean supports(Class<?> aClass) {
		return ChangePasswordPage.class.equals(aClass);
	}

	@Override
	public void validate(Object o, Errors errors) {
		ChangePasswordPage form = (ChangePasswordPage) o;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.required", "Password is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password2", "password2.required", "Repeat Password is required");

		if (!form.getPassword().equals(form.getPassword2())) {
			errors.rejectValue("password", "password.mismatch", "Passwords do not match");
		}
	}
}