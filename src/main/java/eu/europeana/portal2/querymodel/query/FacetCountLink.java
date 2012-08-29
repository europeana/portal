package eu.europeana.portal2.querymodel.query;

public interface FacetCountLink {

	public void update(FacetCountLink facetCountLink);

	public String getUrl();

	public boolean isRemove();

	public String getValue();

	public long getCount();

	public RightsOption getRightsOption();
}