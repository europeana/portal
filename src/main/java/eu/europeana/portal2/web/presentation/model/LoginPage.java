package eu.europeana.portal2.web.presentation.model;

import eu.europeana.portal2.web.presentation.model.data.LoginData;

public class LoginPage extends LoginData {

	public boolean isFailure() {
		return (failureFormat || failureExists || failureForgotFormat || failureForgotDoesntExist);
	}
}
