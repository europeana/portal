package eu.europeana.portal2.web.model.seealso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;


public class SeeAlsoParams {

	private final Logger log = Logger.getLogger(SeeAlsoParams.class.getCanonicalName());

	boolean updated = false;
	Map<String, List<SeeAlsoSuggestion>> params = new LinkedHashMap<String, List<SeeAlsoSuggestion>>();
	Map<String, Map<String, Integer>> index = new HashMap<String, Map<String, Integer>>();

	public void put(String metaField, List<SeeAlsoSuggestion> fieldValues) {
		params.put(metaField, fieldValues);
		updated = false;
	}

	public Set<String> getFields() {
		return params.keySet();
	}

	public List<SeeAlsoSuggestion> get(String metaField) {
		return params.get(metaField);
	}
	
	public List<String> getEscapedQueries() {
		List<String> escapedQueries = new ArrayList<String>();
		for (String metaField : getFields()) {
			for (SeeAlsoSuggestion seeAlsoSuggestion : params.get(metaField)) {
				escapedQueries.add(seeAlsoSuggestion.getEscapedQuery());
			}
		}
		return escapedQueries;
	}

	public SeeAlsoSuggestion findByQuery(String fieldName, String escapedQuery) {

		if (!updated) {
			updateIndex();
		}

		if (!index.containsKey(fieldName)) {
			log.info("no field registered: " + fieldName);
			return null;
		}

		if (index.get(fieldName).containsKey(escapedQuery)) {
			log.info("has key: " + escapedQuery);
			int i = index.get(fieldName).get(escapedQuery);
			log.info("i: " + i);
			SeeAlsoSuggestion suggestion = params.get(fieldName).get(i);
			log.info("suggestion: " + suggestion);
			return suggestion;
		} else {
			log.info("no key: " + escapedQuery);
			for (SeeAlsoSuggestion suggestion : params.get(fieldName)) {
				log.info("checking query: " + escapedQuery + " vs " + suggestion.getEscapedQuery());
				if (suggestion.getEscapedQuery().equals(escapedQuery)) {
					return suggestion;
				}
			}
		}

		return null;
	}

	public void updateIndex() {
		if (!updated) {
			for (String metaField : getFields()) {
				int i = 0;
				Map<String, Integer> map = new HashMap<String, Integer>();
				for (SeeAlsoSuggestion seeAlsoSuggestion : params.get(metaField)) {
					map.put(seeAlsoSuggestion.getEscapedQuery(), i++);
				}
				log.info("indexing " + metaField + " " + map.keySet().size());
				index.put(metaField, map);
			}
			updated = true;
		}
	}
}
