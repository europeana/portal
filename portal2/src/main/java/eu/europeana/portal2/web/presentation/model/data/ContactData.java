package eu.europeana.portal2.web.presentation.model.data;

import eu.europeana.portal2.web.presentation.model.abstracts.SearchPageData;

public abstract class ContactData extends SearchPageData {

	String email = "";
	String feedbackText = "";
	String submitMessage = null;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFeedbackText() {
		return feedbackText;
	}

	public void setFeedbackText(String feedbackText) {
		this.feedbackText = feedbackText;
	}

	public String getSubmitMessage() {
		return submitMessage;
	}

	public void setSubmitMessage(String submitMessage) {
		this.submitMessage = submitMessage;
	}
}
