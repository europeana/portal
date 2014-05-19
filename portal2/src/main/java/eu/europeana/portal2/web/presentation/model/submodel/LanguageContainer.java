package eu.europeana.portal2.web.presentation.model.submodel;

import java.util.List;

import eu.europeana.corelib.utils.model.LanguageVersion;

public class LanguageContainer {

	private List<LanguageVersion> queryTranslations;
	private List<String> keywordLanguages;
	private String portalLanguage;
	private String itemLanguage;

	public LanguageContainer() {}

	public LanguageContainer(List<LanguageVersion> queryTranslations) {
		super();
		this.queryTranslations = queryTranslations;
	}

	public List<LanguageVersion> getQueryTranslations() {
		return queryTranslations;
	}

	public void setQueryTranslations(List<LanguageVersion> queryTranslations) {
		this.queryTranslations = queryTranslations;
	}

	public List<String> getKeywordLanguages() {
		return keywordLanguages;
	}

	public void setKeywordLanguages(List<String> keywordLanguages) {
		this.keywordLanguages = keywordLanguages;
	}

	public String getPortalLanguage() {
		return portalLanguage;
	}

	public void setPortalLanguage(String portalLanguage) {
		this.portalLanguage = portalLanguage;
	}

	public String getItemLanguage() {
		return itemLanguage;
	}

	public void setItemLanguage(String itemLanguage) {
		this.itemLanguage = itemLanguage;
	}
}
