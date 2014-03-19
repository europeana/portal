package eu.europeana.portal2.web.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.portal2.web.presentation.model.submodel.SearchParam;

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

	/**
	 * Filters parameters other than query and qf
	 *
	 * @param urlParams
	 *   The existing set of parameters
	 * @return
	 *   The filtered out parameters
	 */
	public static Map<String, String[]> getOtherUrlParams(Map<String, String[]> urlParams) {
		Map<String, String[]> otherUrlValues = new HashMap<String, String[]>();
		if (urlParams != null && urlParams.entrySet().size() > 0) {
			for (Entry<String, String[]> entry : urlParams.entrySet()) {
				if (entry.getKey().equals("query") || entry.getKey().equals("qf")) {
					continue;
				}
				otherUrlValues.put(entry.getKey(), entry.getValue());
			}
		}
		return otherUrlValues;
	}
}
