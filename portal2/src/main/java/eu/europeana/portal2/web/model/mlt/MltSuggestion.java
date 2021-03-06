package eu.europeana.portal2.web.model.mlt;

import eu.europeana.portal2.web.util.ControllerUtil;

public class MltSuggestion {

	private int id;
	private String metaField;
	private String label;
	private String query;
	private String solrEscapedQuery;
	private String escapedQuery;
	private String taggedEscapedQuery;
	private int count;

	public MltSuggestion(String metaField, String label, int id) {
		this(metaField, label, id, true);
	}

	public MltSuggestion(String metaField, String label, int id, boolean clear) {
		this.metaField = metaField;
		this.label = label;
		this.query = clear ? ControllerUtil.clearSeeAlso(label) : label;
		this.id = id;
	}

	public void makeEscapedQuery(String solrEscapedQuery) {
		this.solrEscapedQuery = '"' + solrEscapedQuery + '"';
		this.escapedQuery = String.format("%s:\"%s\"", metaField, solrEscapedQuery);
		this.taggedEscapedQuery = String.format("{!id=%d}%s", id, this.escapedQuery);
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

	public String getTaggedEscapedQuery() {
		return taggedEscapedQuery;
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

	public String getSolrEscapedQuery() {
		return solrEscapedQuery;
	}

	public void setSolrEscapedQuery(String solrEscapedQuery) {
		this.solrEscapedQuery = solrEscapedQuery;
	}

	@Override
	public String toString() {
		return "MltSuggestion [label=" + label + ", query=" + query
				+ ", escapedQuery=" + escapedQuery + ", count=" + count + "]";
	}
}
