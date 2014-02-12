package eu.europeana.portal2.web.model.mlt;

import java.util.Arrays;
import java.util.List;

public class MltConfiguration {

	private String solrField;
	private List<String> edmFields;
	private String translationKey;
	private Double weight;

	public MltConfiguration(String solrField, List<String> edmFields,
			String translationKey, Double weight) {
		super();
		this.solrField = solrField;
		this.edmFields = edmFields;
		this.translationKey = translationKey;
		this.weight = weight;
	}

	public MltConfiguration(String solrField, String translationKey, Double weight, String... edmFields) {
		this.solrField = solrField;
		this.edmFields = Arrays.asList(edmFields);
		this.translationKey = translationKey;
		this.weight = weight;
	}

	public String getSolrField() {
		return solrField;
	}

	public void setSolrField(String solrField) {
		this.solrField = solrField;
	}

	public List<String> getEdmFields() {
		return edmFields;
	}

	public void setEdmFields(List<String> edmFields) {
		this.edmFields = edmFields;
	}

	public String getTranslationKey() {
		return translationKey;
	}

	public void setTranslationKey(String translationKey) {
		this.translationKey = translationKey;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}
}
