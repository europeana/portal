package eu.europeana.portal2.web.presentation.utils;

import java.util.ArrayList;
import java.util.List;

import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.portal2.web.presentation.model.SearchParam;

public class SearchFilterUtil {

	/**
	 * Gets the list of values in query and query filters
	 *
	 * @param query
	 *   The object representing the Solr query
	 *   
	 * @return
	 *   The list of search parameters
	 */
	public static List<SearchParam> getExistingValues(Query query) {
		List<SearchParam> existingValues = new ArrayList<SearchParam>();
		
		existingValues.add(new SearchParam("query", query.getQuery()));
		if (query.getRefinements() != null) {
			for (String qf : query.getRefinements()) {
				existingValues.add(new SearchParam("qf", qf));
			}
		}
		return existingValues;
	}
}
