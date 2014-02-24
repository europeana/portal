package eu.europeana.portal2.web.presentation.model.data.submodel.clickstream;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonSerialize(include = Inclusion.NON_EMPTY)
public class FullViewDAO extends AffixDAO {

	private String userAction;
	private String europeanaUri;
	String query;
	Integer startPage;
	Integer numFound;

	@JsonProperty("action")
	public String getUserAction() {
		return userAction;
	}

	public void setUserAction(String userAction) {
		this.userAction = userAction;
	}

	@JsonProperty("europeana_uri")
	public String getEuropeanaUri() {
		return europeanaUri;
	}

	public void setEuropeanaUri(String europeanaUri) {
		this.europeanaUri = europeanaUri;
	}

	@JsonProperty("query")
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	@JsonProperty("start")
	public Integer getStartPage() {
		return startPage;
	}

	public void setStartPage(Integer startPage) {
		this.startPage = startPage;
	}

	public Integer getNumFound() {
		return numFound;
	}

	public void setNumFound(Integer numFound) {
		this.numFound = numFound;
	}
}
