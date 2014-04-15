package eu.europeana.portal2.web.presentation.model;

import eu.europeana.portal2.web.presentation.model.abstracts.SearchPageData;

public class KeywordLanguagesLimitAwareData extends SearchPageData {

	private int keywordLanguagesLimit;

	public int getKeywordLanguagesLimit() {
		return keywordLanguagesLimit;
	}

	public void setKeywordLanguagesLimit(int keywordLanguagesLimit) {
		this.keywordLanguagesLimit = keywordLanguagesLimit;
	}

}
