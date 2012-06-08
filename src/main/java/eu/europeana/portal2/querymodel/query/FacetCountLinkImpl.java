package eu.europeana.portal2.querymodel.query;

import eu.europeana.portal2.web.model.common.LabelFrequency;

public class FacetCountLinkImpl implements FacetCountLink {

	private LabelFrequency facetCount;
	private String url;
	private boolean remove;

	private RightsOption rightsOption = null;

	public FacetCountLinkImpl(LabelFrequency facetCount, String url, boolean remove) {
		this.facetCount = facetCount;
		this.url = url;
		this.remove = remove;
	}

	public FacetCountLinkImpl(RightsOption rightsOption,
			LabelFrequency facetCount, String url, boolean remove) {
		this(facetCount, url, remove);
		this.rightsOption = rightsOption;
	}

	public String getUrl() {
		return url;
	}

	public boolean isRemove() {
		return remove;
	}

	public String getValue() {
		return facetCount.getLabel();
	}

	public long getCount() {
		return facetCount.getCount();
	}

	/**
	 * Clustering facet links by updating the count and appending the url if
	 * needed.
	 * 
	 * @param facetCountLink
	 *            The facetCountLink to merge with.
	 */
	public void update(FacetCountLink facetCountLink) {
		facetCount.setCount(facetCount.getCount() + facetCountLink.getCount());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		if (rightsOption != null) {
			FacetCountLink that = (FacetCountLink) o;
			return rightsOption == that.getRightsOption();
		}
		LabelFrequency that = ((FacetCountLinkImpl) o).facetCount;
		return facetCount.getLabel().equals(that.getLabel());
	}

	@Override
	public RightsOption getRightsOption() {
		return rightsOption;
	}

	@Override
	public int hashCode() {
		return facetCount.getLabel().hashCode();
	}

	public String toString() {
		return "<a href='" + url + "'>" + getValue() + "</a> " + (remove ? "(remove)" : "(add)");
	}

}
