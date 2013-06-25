package eu.europeana.portal2.web.presentation.model;

import java.util.List;

import eu.europeana.portal2.web.presentation.model.abstracts.SearchPageData;

public class SearchWidgetEditorPage<T> extends SearchPageData {

	private List<T> results;

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> list) {
		this.results = list;
	}

}
