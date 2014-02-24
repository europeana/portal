package eu.europeana.portal2.web.presentation.model.data.submodel.clickstream;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonSerialize(include = Inclusion.NON_EMPTY)
public class BriefViewDAO extends AffixDAO {

	private String userAction;
	private String view;
	String query = "";
	String queryConstraints;
	String queryType;
	Integer pageNr = 1;
	Integer nrResults = 0;
	String languageFacets;
	String countryFacet;

	@JsonProperty("action")
	public String getUserAction() {
		return userAction;
	}

	public void setUserAction(String userAction) {
		this.userAction = userAction;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getQueryConstraints() {
		return queryConstraints;
	}

	public void setQueryConstraints(String queryConstraints) {
		this.queryConstraints = queryConstraints;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	@JsonProperty("page")
	public int getPageNr() {
		return pageNr;
	}

	public void setPageNr(int pageNr) {
		this.pageNr = pageNr;
	}

	@JsonProperty("numFound")
	public int getNrResults() {
		return nrResults;
	}

	public void setNrResults(int nrResults) {
		this.nrResults = nrResults;
	}

	@JsonProperty("langFacet")
	public String getLanguageFacets() {
		return languageFacets;
	}

	public void setLanguageFacets(String languageFacets) {
		this.languageFacets = languageFacets;
	}

	public String getCountryFacet() {
		return countryFacet;
	}

	public void setCountryFacet(String countryFacet) {
		this.countryFacet = countryFacet;
	}
}
