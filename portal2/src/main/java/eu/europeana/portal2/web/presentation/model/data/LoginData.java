package eu.europeana.portal2.web.presentation.model.data;

import eu.europeana.portal2.web.presentation.model.abstracts.SearchPageData;

public abstract class LoginData extends SearchPageData {

	protected boolean failureFormat = false;
	protected boolean failureExists = false;
	protected boolean success = false;

	protected boolean failureForgotFormat = false;
	protected boolean failureForgotDoesntExist = false;
	protected boolean forgotSuccess = false;

	private String email;
	private String errorMessage;

	public boolean isFailureFormat() {
		return failureFormat;
	}

	public void setFailureFormat(boolean failureFormat) {
		this.failureFormat = failureFormat;
	}

	public boolean isFailureExists() {
		return failureExists;
	}

	public void setFailureExists(boolean failureExists) {
		this.failureExists = failureExists;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean isFailureForgotFormat() {
		return failureForgotFormat;
	}

	public void setFailureForgotFormat(boolean failureForgotFormat) {
		this.failureForgotFormat = failureForgotFormat;
	}

	public boolean isFailureForgotDoesntExist() {
		return failureForgotDoesntExist;
	}

	public void setFailureForgotDoesntExist(boolean failureForgotDoesntExist) {
		this.failureForgotDoesntExist = failureForgotDoesntExist;
	}

	public boolean isForgotSuccess() {
		return forgotSuccess;
	}

	public void setForgotSuccess(boolean forgotSuccess) {
		this.forgotSuccess = forgotSuccess;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
