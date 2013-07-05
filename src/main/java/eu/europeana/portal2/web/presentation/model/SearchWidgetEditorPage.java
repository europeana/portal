package eu.europeana.portal2.web.presentation.model;

import java.util.List;

import org.apache.solr.client.solrj.response.FacetField.Count;

import eu.europeana.portal2.web.presentation.model.abstracts.SearchPageData;

public class SearchWidgetEditorPage<T> extends SearchPageData {

	private List<T> providers;
	private List<Count> rights;
	private List<Count> types;
	private List<Count> languages;

	public List<T> getProviders() {
		return providers;
	}

	public void setProviders(List<T> list) {
		this.providers = list;
	}

	public List<Count> getRights() {
		return rights;
	}

	public void setRights(List<Count> rights) {
		this.rights = rights;
	}

	public List<Count> getTypes() {
		return types;
	}

	public void setTypes(List<Count> types) {
		this.types = types;
	}

	public List<Count> getLanguages() {
		return languages;
	}

	public void setLanguages(List<Count> languages) {
		this.languages = languages;
	}
}