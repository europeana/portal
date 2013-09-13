package eu.europeana.portal2.web.model.seealso;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.europeana.corelib.logging.Log;

public class SeeAlsoSuggestions {

	Logger log = Logger.getLogger(SeeAlsoSuggestions.class.getCanonicalName());

	private Map<String, String> seeAlsoTranslations;

	private Map<String, String> seeAlsoAggregations;

	private Map<String, Field> fields = new LinkedHashMap<String, Field>();
	
	private static final Pattern ID_PATTERN = Pattern.compile("\\{!id=([0-9]+)\\}");

	private SeeAlsoParams seeAlsoParams;

	public SeeAlsoSuggestions(Map<String, String> seeAlsoTranslations, 
			Map<String, String> seeAlsoAggregations, 
			SeeAlsoParams seeAlsoParams
		) {
		this.seeAlsoTranslations = seeAlsoTranslations;
		this.seeAlsoAggregations = seeAlsoAggregations;
		this.seeAlsoParams = seeAlsoParams;
	}

	public void add(String query, Integer count) {
		log.info(String.format("query: %s,  count %d", query, count));
		String[] parts = query.split(":", 2);
		Matcher matcher = ID_PATTERN.matcher(parts[0]);
		int id = -1;
		if (matcher.find()) {
			log.info(matcher.group(1));
			id = Integer.parseInt(matcher.group(1));
		} else {
			return;
		}
		String fieldName = parts[0].replaceAll(ID_PATTERN.pattern(), "");
		SeeAlsoSuggestion suggestion = seeAlsoParams.findByQuery(fieldName, id);
		suggestion.setCount(count);

		String aggregatedFieldName = (seeAlsoAggregations.containsKey(suggestion.getMetaField()))
				? seeAlsoAggregations.get(suggestion.getMetaField()) 
				: suggestion.getMetaField();

		Field field;
		if (fields.containsKey(aggregatedFieldName)) {
			field = fields.get(aggregatedFieldName);
		} else {
			field = new Field(aggregatedFieldName, seeAlsoTranslations.get(aggregatedFieldName));
			fields.put(aggregatedFieldName, field);
		}

		field.addSuggestion(suggestion);
	}

	public Map<String, Field> getFields() {
		return fields;
	}

	@Override
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
		private List<SeeAlsoSuggestion> suggestions = new LinkedList<SeeAlsoSuggestion>();

		public Field(String fieldName, String translationKey) {
			this.fieldName = fieldName;
			this.translationKey = translationKey;
		}

		public void addSuggestion(SeeAlsoSuggestion suggestion) {
			suggestions.add(suggestion);
		}

		public String getFieldName() {
			return fieldName;
		}

		public String getTranslationKey() {
			return translationKey;
		}

		public List<SeeAlsoSuggestion> getSuggestions() {
			return suggestions;
		}

		@Override
		public String toString() {
			return "Field [fieldName=" + fieldName
					+ ", translationKey=" + translationKey
					+ ", suggestions=" + suggestions + "]";
		}
	}
}
