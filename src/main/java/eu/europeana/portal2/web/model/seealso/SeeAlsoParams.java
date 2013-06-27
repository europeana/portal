/*
 * Copyright 2007-2012 The Europeana Foundation
 *
 *  Licenced under the EUPL, Version 1.1 (the "Licence") and subsequent versions as approved 
 *  by the European Commission;
 *  You may not use this work except in compliance with the Licence.
 *  
 *  You may obtain a copy of the Licence at:
 *  http://joinup.ec.europa.eu/software/page/eupl
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under 
 *  the Licence is distributed on an "AS IS" basis, without warranties or conditions of 
 *  any kind, either express or implied.
 *  See the Licence for the specific language governing permissions and limitations under 
 *  the Licence.
 */

package eu.europeana.portal2.web.model.seealso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeeAlsoParams {
	
	private final Logger log = LoggerFactory.getLogger(SeeAlsoParams.class);

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
			log.warn("Unknown field in see also feature: " + fieldName);
			return null;
		}

		if (index.get(fieldName).containsKey(escapedQuery)) {
			int i = index.get(fieldName).get(escapedQuery);
			SeeAlsoSuggestion suggestion = params.get(fieldName).get(i);
			return suggestion;
		} else {
			for (SeeAlsoSuggestion suggestion : params.get(fieldName)) {
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
				index.put(metaField, map);
			}
			updated = true;
		}
	}
}
