package eu.europeana.portal2.web.presentation.model;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import eu.europeana.portal2.web.util.ControllerUtil;

public class SeeAlsoSuggestions {

	private Map<String, String> seeAlsoTranslations;

	private Map<String, String> seeAlsoAggregations;

	private Map<String, Field> fields = new LinkedHashMap<String, Field>();

	public SeeAlsoSuggestions(Map<String, String> seeAlsoTranslations, Map<String, String> seeAlsoAggregations) {
		this.seeAlsoTranslations = seeAlsoTranslations;
		this.seeAlsoAggregations = seeAlsoAggregations;
	}

	public void add(String query, Integer count) {
		String[] parts = query.split(":", 2);
		String fieldName = parts[0];
		String fieldValue = parts[1]
				.replaceAll("^\"", "")
				.replaceAll("\"$", "");

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
		if (fieldName.equals("PROVIDER") || fieldName.equals("DATA_PROVIDER")) {
			extendedQuery = query;
		} else {
			extendedQuery = fieldName + ":(" + ControllerUtil.clearSeeAlso(fieldValue) + ")";
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
