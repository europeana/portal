package eu.europeana.portal2.web.presentation.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Label for a search field.
 * 
 * Provides information about the field in query, the human readable label for the
 * 
 * @author peter.kiraly@kb.nl
 */
public class SearchLabel {

	/** The field part */
	String field;

	/** The translation code for field */
	String fieldCode;

	/** The field value to search for */
	String value;

	/** The human readable value */
	String label;

	private static Map<String, String> fieldCodeMap = Collections.unmodifiableMap(new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("COUNTRY", "ByCountry_t");
			// put("completeness", "?????");
			put("LANGUAGE", "ByLanguage_t");
			put("PROVIDER", "ByProvider_t");
			put("DATA_PROVIDER", "ByProvider_t");
			put("RIGHTS", "byCopyright_t");
			put("TYPE", "ByMediatype_t");
			put("YEAR", "Bydate_t");
		}
	});

	public SearchLabel(String field, String value) {
		super();
		this.field = field;
		this.value = value;
		this.fieldCode = fieldCodeMap.containsKey(field) ? fieldCodeMap.get(field) : null;
		// TODO: add some logic here
		this.label = value;
	}

	public SearchLabel(String field, String fieldCode, String value, String label) {
		super();
		this.field = field;
		this.fieldCode = fieldCode;
		this.value = value;
		this.label = label;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getFieldCode() {
		return fieldCode;
	}

	public void setFieldCode(String fieldCode) {
		this.fieldCode = fieldCode;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return "SearchLabel [field=" + field + ", fieldCode=" + fieldCode + ", value=" + value + ", label=" + label + "]";
	}
}
