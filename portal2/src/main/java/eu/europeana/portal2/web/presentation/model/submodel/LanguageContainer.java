package eu.europeana.portal2.web.presentation.model.submodel;

import java.util.List;
import java.util.Map;

import eu.europeana.corelib.definitions.solr.model.QueryTranslation;
import eu.europeana.corelib.utils.model.LanguageVersion;

public class LanguageContainer {

	private List<String> keywordLanguages;
	private String portalLanguage;
	private String itemLanguage;
	private QueryTranslation queryTranslation;

	public LanguageContainer() {}

	public List<LanguageVersion> getQueryLanguageVersions() {
		if (queryTranslation == null) {
			return null;
		}
		return queryTranslation.getLanguageVersions();
	}

	public void setQueryTranslations(QueryTranslation queryTranslation) {
		this.queryTranslation = queryTranslation;
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

	public String getModifiedQuery() {
		return queryTranslation.getModifiedQuery();
	}

	public Map<String, List<LanguageVersion>> getLanguageVersionMap() {
		return queryTranslation.getLanguageVersionMap();
	}

	public QueryTranslation getQueryTranslation() {
		return queryTranslation;
	}

	public void setQueryTranslation(QueryTranslation queryTranslation) {
		this.queryTranslation = queryTranslation;
	}
}
