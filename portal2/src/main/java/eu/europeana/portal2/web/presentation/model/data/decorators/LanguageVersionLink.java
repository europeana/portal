package eu.europeana.portal2.web.presentation.model.data.decorators;

import eu.europeana.corelib.utils.model.LanguageVersion;

public class LanguageVersionLink extends LanguageVersion {

	private String queryLink;
	private String removeLink;

	/**
	 * Instantiate a new LanguageVersionLink object
	 * 
	 * @param languageVersion
	 *   A LanguageVersion object (containing a text, and its language code)
	 * @param queryLink
	 *   A link which queries against only this particular language version
	 * @param removeLink
	 *   A link which removes this particular version from the list of language versions
	 */
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
