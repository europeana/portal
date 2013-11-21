package eu.europeana.portal2.web.presentation.model.submodel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import eu.europeana.corelib.web.model.rights.RightReusabilityCategorizer;

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

	/** The translation code for field */
	String valueCode;

	/** The human readable value */
	String label;

	private static Map<String, String> fieldCodeMap = new HashMap<String, String>();
	static {
		fieldCodeMap.put("COUNTRY", "ByCountry_t");
		fieldCodeMap.put("LANGUAGE", "ByLanguage_t");
		fieldCodeMap.put("PROVIDER", "ByProvider_t");
		fieldCodeMap.put("DATA_PROVIDER", "ByDataProvider_t");
		fieldCodeMap.put("RIGHTS", "byCopyright_t");
		fieldCodeMap.put("TYPE", "ByMediatype_t");
		fieldCodeMap.put("YEAR", "Bydate_t");
		fieldCodeMap.put("REUSABILITY", "byReusability_t");
		fieldCodeMap = Collections.unmodifiableMap(fieldCodeMap);
	}

	public SearchLabel(String field, String value) {
		this.field = field;
		this.value = value;
		this.fieldCode = fieldCodeMap.containsKey(field) ? fieldCodeMap.get(field) : null;
		// TODO: add some logic here
		this.label = value;
		createValueCode(field, value);
	}

	public SearchLabel(String field, String fieldCode, String value, String label) {
		this.field = field;
		this.fieldCode = fieldCode;
		this.value = value;
		this.label = label;
		createValueCode(field, value);
	}

	public void createValueCode(String field, String value) {
		if (StringUtils.isNotBlank(field) && field.equals("REUSABILITY")) {
			this.valueCode = RightReusabilityCategorizer.getTranslationKey(value);
		}
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

	public String getValueCode() {
		return valueCode;
	}

	public void setValueCode(String valueCode) {
		this.valueCode = valueCode;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return "SearchLabel [field=" + field 
				+ ", fieldCode=" + fieldCode 
				+ ", value=" + value 
				+ ", valueCode=" + valueCode 
				+ ", label=" + label + "]";
	}
}
