package eu.europeana.portal2.web.model.mlt;

import java.util.ArrayList;
import java.util.List;

public class EuropeanaMltCategory {
	private String name;
	private String field;
	private String translationKey;
	private String query;
	private long totalResults = 0;
	private List<EuropeanaMltLink> urls;

	public EuropeanaMltCategory(String name, String field, String translationKey) {
		this.name = name;
		this.field = field;
		this.translationKey = translationKey;
		this.urls = new ArrayList<EuropeanaMltLink>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getTranslationKey() {
		return translationKey;
	}

	public void setTranslationKey(String translationKey) {
		this.translationKey = translationKey;
	}

	public List<EuropeanaMltLink> getUrls() {
		return urls;
	}

	public void setUrls(List<EuropeanaMltLink> urls) {
		this.urls = urls;
	}

	public void addUrl(EuropeanaMltLink url) {
		this.urls.add(url);
	}

	public long getTotalResults() {
		return totalResults;
	}

	public void setTotalResults(long totalResults) {
		this.totalResults = totalResults;
	}

	public void addResultSize(long totalResults) {
		this.totalResults += totalResults;
	}
}
