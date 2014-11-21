package eu.europeana.portal2.web.presentation.model.data;

import eu.europeana.portal2.web.presentation.model.abstracts.SearchPageData;

public class AjaxData extends SearchPageData {

	protected boolean success;

	private String exception;
	
	private String document;
	
	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public String getException() {
		return exception;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}