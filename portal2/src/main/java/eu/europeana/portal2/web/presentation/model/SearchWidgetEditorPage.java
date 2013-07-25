package eu.europeana.portal2.web.presentation.model;

import java.util.List;

import eu.europeana.portal2.web.presentation.model.data.submodel.ContributorItem;

public class SearchWidgetEditorPage extends SearchPage {

	private List<ContributorItem> providers;

	public List<ContributorItem> getProviders() {
		return providers;
	}

	public void setProviders(List<ContributorItem> list) {
		this.providers = list;
	}
}