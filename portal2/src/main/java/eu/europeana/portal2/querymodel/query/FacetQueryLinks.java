package eu.europeana.portal2.querymodel.query;

import java.util.List;

public interface FacetQueryLinks {

	public abstract String getType();

	public abstract List<FacetCountLink> getLinks();

	public abstract boolean isSelected();
}