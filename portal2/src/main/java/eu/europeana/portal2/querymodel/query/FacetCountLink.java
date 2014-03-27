package eu.europeana.portal2.querymodel.query;

import eu.europeana.corelib.definitions.model.RightsOption;

public interface FacetCountLink {

	public void update(FacetCountLink facetCountLink);

	public String getUrl();

	public boolean isRemove();

	public String getValue();

	public long getCount();

	public String getParam();

	public RightsOption getRightsOption();
}