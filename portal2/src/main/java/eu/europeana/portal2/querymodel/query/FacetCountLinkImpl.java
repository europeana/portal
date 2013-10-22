package eu.europeana.portal2.querymodel.query;

import eu.europeana.corelib.definitions.model.RightsOption;
import eu.europeana.portal2.web.model.facets.LabelFrequency;

public class FacetCountLinkImpl implements FacetCountLink {

	private LabelFrequency facetCount;
	private String url;
	private boolean remove;

	private RightsOption rightsOption = null;

	/**
	 * Create a facet link
	 * 
	 * @param facetCount
	 *   A pair of label and frequency
	 * @param url
	 *   The URL
	 * @param remove
	 *   Whether to remove or not
	 */
	public FacetCountLinkImpl(LabelFrequency facetCount, String url, boolean remove) {
		this.facetCount = facetCount;
		this.url = url;
		this.remove = remove;
	}

	/**
	 * Create a facet link
	 * 
	 * @param rightsOption
	 * @param facetCount
	 *   A pair of label and frequency
	 * @param url
	 *   The URL
	 * @param remove
	 *   Whether to remove or not
	 */
	public FacetCountLinkImpl(RightsOption rightsOption,
			LabelFrequency facetCount, String url, boolean remove) {
		this(facetCount, url, remove);
		this.rightsOption = rightsOption;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public boolean isRemove() {
		return remove;
	}

	@Override
	public String getValue() {
		return facetCount.getLabel();
	}

	@Override
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
	@Override
	public void update(FacetCountLink facetCountLink) {
		facetCount.setCount(facetCount.getCount() + facetCountLink.getCount());
	}

	/**
	 * It prevents adding this into an ArrayList if its value is already contained.
	 */
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

	@Override
	public String toString() {
		return "<a href='" + url + "'>" + getValue() + "</a> " + (remove ? "(remove)" : "(add)");
	}
}
