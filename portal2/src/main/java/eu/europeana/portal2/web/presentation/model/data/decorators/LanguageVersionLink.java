package eu.europeana.portal2.web.presentation.model.data.decorators;

import eu.europeana.corelib.utils.model.LanguageVersion;

public class LanguageVersionLink extends LanguageVersion {

	private String queryLink;
	private String removeLink;

	public LanguageVersionLink(LanguageVersion languageVersion, String queryLink, String removeLink) {
		super(languageVersion.getText(), languageVersion.getLanguageCode());
		this.queryLink = queryLink;
		this.removeLink = removeLink;
	}

	public String getQueryLink() {
		return queryLink;
	}

	public void setQueryLink(String queryLink) {
		this.queryLink = queryLink;
	}

	public String getRemoveLink() {
		return removeLink;
	}

	public void setRemoveLink(String removeLink) {
		this.removeLink = removeLink;
	}
}
