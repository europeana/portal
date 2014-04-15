package eu.europeana.portal2.web.presentation.model;

import eu.europeana.corelib.definitions.db.entity.RelationalDatabase;
import eu.europeana.portal2.web.presentation.model.abstracts.SearchPageData;

public class KeywordLanguagesLimitAwareData extends SearchPageData {

	private int keywordLanguagesLimit;
	private String keywordLanguagesSeparator = RelationalDatabase.SEARCH_LANGUAGES_SEPARATOR;

	public int getKeywordLanguagesLimit() {
		return keywordLanguagesLimit;
	}

	public void setKeywordLanguagesLimit(int keywordLanguagesLimit) {
		this.keywordLanguagesLimit = keywordLanguagesLimit;
	}

	public String getKeywordLanguagesSeparator() {
		return keywordLanguagesSeparator;
	}
}
