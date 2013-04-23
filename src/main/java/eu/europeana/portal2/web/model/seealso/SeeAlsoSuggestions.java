package eu.europeana.portal2.web.model.seealso;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.eclipse.jetty.util.log.Log;


public class SeeAlsoSuggestions {

	private final Logger log = Logger.getLogger(SeeAlsoSuggestions.class.getCanonicalName());

	private Map<String, String> seeAlsoTranslations;

	private Map<String, String> seeAlsoAggregations;

	private Map<String, Field> fields = new LinkedHashMap<String, Field>();

	private SeeAlsoParams seeAlsoParams;

	public SeeAlsoSuggestions(Map<String, String> seeAlsoTranslations, 
			Map<String, String> seeAlsoAggregations, 
			SeeAlsoParams seeAlsoParams
		) {
		this.seeAlsoTranslations = seeAlsoTranslations;
		this.seeAlsoAggregations = seeAlsoAggregations;
		this.seeAlsoParams = seeAlsoParams;
	}

	private SeeAlsoSuggestion findByQuery(String fieldName, String escapedQuery) {
		for (SeeAlsoSuggestion suggestion : seeAlsoParams.get(fieldName)) {
			if (suggestion.getEscapedQuery().equals(escapedQuery)) {
				return suggestion;
			}
		}
		return null;
	}

	public void add(String query, Integer count) {
		String[] parts = query.split(":", 2);
		String fieldName = parts[0];
		String escapedQuery = parts[1];
		log.info(fieldName + " -> " + escapedQuery);
		SeeAlsoSuggestion suggestion = seeAlsoParams.findByQuery(fieldName, query);
		log.info("suggestion: " + (suggestion == null ? "null" : suggestion.toString()));
		String fieldValue = suggestion.getLabel();

		String aggregatedFieldName = (seeAlsoAggregations.containsKey(fieldName))
				? seeAlsoAggregations.get(fieldName) 
				: fieldName;

		Field field;
		if (fields.containsKey(aggregatedFieldName)) {
			field = fields.get(aggregatedFieldName);
		} else {
			field = new Field(aggregatedFieldName, seeAlsoTranslations.get(aggregatedFieldName));
			fields.put(aggregatedFieldName, field);
		}

		String extendedQuery;
		if (fieldName.equals("title") || fieldName.equals("PROVIDER") || fieldName.equals("DATA_PROVIDER")) {
			extendedQuery = suggestion.getEscapedQuery(); //query;
		} else {
			extendedQuery = suggestion.getEscapedQuery(); // query; // fieldName + ":(" + ControllerUtil.clearSeeAlso(fieldValue) + ")";
		}

		field.addSuggestion(new Suggestion(extendedQuery, fieldValue, count));
	}

	public Map<String, Field> getFields() {
		return fields;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("SeeAlsoSuggestions [");
		for (Entry<String, Field> entry : fields.entrySet()) {
			sb.append(entry.getValue()).append(": ");
			sb.append(entry.getValue().toString());
			sb.append(",");
		}
		sb.append("]");
		return sb.toString();
	}

	public class Field {

		private String fieldName;
		private String translationKey;
		private List<Suggestion> suggestions = new LinkedList<Suggestion>();

		public Field(String fieldName, String translationKey) {
			this.fieldName = fieldName;
			this.translationKey = translationKey;
		}

		public void addSuggestion(Suggestion suggestion) {
			suggestions.add(suggestion);
		}

		public String getFieldName() {
			return fieldName;
		}

		public String getTranslationKey() {
			return translationKey;
		}

		public List<Suggestion> getSuggestions() {
			return suggestions;
		}

		@Override
		public String toString() {
			return "Field [fieldName=" + fieldName
					+ ", translationKey=" + translationKey
					+ ", suggestions=" + suggestions + "]";
		}
	}

	public class Suggestion {
		private String query;
		private String value;
		private Integer count;

		public Suggestion(String query, String value, Integer count) {
			this.query = query;
			this.value = value;
			this.count = count;
		}

		public String getQuery() {
			return query;
		}

		public String getValue() {
			return value;
		}

		public Integer getCount() {
			return count;
		}

		@Override
		public String toString() {
			return "Suggestion [query=" + query + ", value=" + value + ", count=" + count + "]";
		}
	}
}
