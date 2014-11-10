package eu.europeana.portal2.web.presentation.model;


public class PrevSearchAwareData extends KeywordLanguagesLimitAwareData {

	private String returnToQuery;
	private String[] returnToFacets;

	public String getReturnToQuery() {
		return returnToQuery;
	}
	
	public void setReturnToQuery(String returnToQuery) {
		this.returnToQuery = returnToQuery;
	}
	
	public String[] getReturnToFacets() {
		return returnToFacets;
	}

	public void setReturnToFacets(String[] returnToFacets) {
		this.returnToFacets = returnToFacets;
	}

	
}
