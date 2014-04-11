package eu.europeana.portal2.web.presentation.model.data.decorators;

import eu.europeana.corelib.utils.model.LanguageVersion;

public class LanguageVersionLink extends LanguageVersion {

	private String url;

	public LanguageVersionLink(String text, String languageCode, String url) {
		super(text, languageCode);
		this.url = url;
	}

	public LanguageVersionLink(LanguageVersion languageVersion, String url) {
		this(languageVersion.getText(), languageVersion.getLanguageCode(), url);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
