package eu.europeana.portal2.web.presentation.model;

import java.util.List;

import eu.europeana.corelib.definitions.solr.model.Term;
import eu.europeana.portal2.web.presentation.model.abstracts.ResultPageData;

public class SuggestionsPage extends ResultPageData<Term> {

	private List<Term> results;

	@Override
	public List<Term> getResults() {
		return results;
	}

	@Override
	public void setResults(List<Term> list) {
		this.results = list;
	}

	@Override
	public boolean isHasResults() {
		return ((results != null) && !results.isEmpty());
	}
}
