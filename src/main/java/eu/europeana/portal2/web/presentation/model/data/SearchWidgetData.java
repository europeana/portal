package eu.europeana.portal2.web.presentation.model.data;

import java.util.List;

import eu.europeana.portal2.web.presentation.model.PortalPageData;

public abstract class SearchWidgetData extends PortalPageData {

	private List<String> providers;

	public void setProviders(List<String> providers) {
		this.providers = providers;
	}

	public List<String> getProviders() {
		return providers;
	}
}