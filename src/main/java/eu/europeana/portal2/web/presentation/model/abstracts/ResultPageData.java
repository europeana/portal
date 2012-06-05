package eu.europeana.portal2.web.presentation.model.abstracts;

import java.util.List;

public abstract class ResultPageData<T> extends SearchPageData {

	private List<T> results;

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> list) {
		this.results = list;
	}

	public boolean isHasResults() {
		return ((results != null) && !results.isEmpty());
	}
}
