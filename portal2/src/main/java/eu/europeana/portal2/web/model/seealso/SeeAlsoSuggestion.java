package eu.europeana.portal2.web.model.seealso;

import eu.europeana.portal2.web.util.ControllerUtil;

public class SeeAlsoSuggestion {

	private String metaField;
	private String label;
	private String query;
	private String escapedQuery;
	private int count;
	private int id;

	public SeeAlsoSuggestion(String metaField, String label, int id) {
		this.metaField = metaField;
		this.label = label;
		this.query = ControllerUtil.clearSeeAlso(label);
		this.id = id;
	}

	public void makeEscapedQuery(String solrEscapedQuery) {
		this.escapedQuery = String.format("{!id=%d}%s:\"%s\"", id, metaField, solrEscapedQuery);
	}

	public String getMetaField() {
		return metaField;
	}

	public void setMetaField(String metaField) {
		this.metaField = metaField;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getEscapedQuery() {
		return escapedQuery;
	}

	public void setEscapedQuery(String escapedQuery) {
		this.escapedQuery = escapedQuery;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "SeeAlsoSuggestion [label=" + label + ", query=" + query
				+ ", escapedQuery=" + escapedQuery + ", count=" + count + "]";
	}
}
