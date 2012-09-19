package eu.europeana.portal2.web.presentation.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

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

	/**
	 * Filters parameters other than query and qf
	 *
	 * @param urlParams
	 *   The existing set of parameters
	 * @return
	 *   The filtered out parameters
	 */
	public static List<String> getOtherUrlParams(Map<String, String[]> urlParams) {
		List<String> otherUrlValues = new ArrayList<String>();
		for (Entry<String, String[]> entry : urlParams.entrySet()) {
			if (entry.getKey().equals("query") || entry.getKey().equals("qf")) {
				continue;
			}
			String key = entry.getKey();
			if (entry.getValue().length > 1) {
				key = key + "[]";
			}
			for (String value : entry.getValue()) {
				otherUrlValues.add(key + "=" + value);
			}
		}
		return otherUrlValues;
	}
}
