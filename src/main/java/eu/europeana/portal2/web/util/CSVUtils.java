package eu.europeana.portal2.web.util;

import java.util.List;

import org.apache.commons.lang.StringUtils;

public class CSVUtils {

	private static final String RECORD_SEPARATOR = "\n";
	private static final String FIELD_SEPARATOR = ",";

	/**
	 * Encode a field for usage in CSV
	 * @param field
	 * @return
	 */
	public static String encodeField(String field) {
		if (StringUtils.isBlank(field)) {
			return "";
		}
		if (field.indexOf('"') > -1) {
			field = field.replaceAll("\"", "\"\"");
		}
		field = '"' + field + '"';
		return field;
	}

	/**
	 * Encode a record (list of fields) for usage in CSV
	 */
	public static String encodeRecord(List<String> fields) {
		return StringUtils.join(fields, FIELD_SEPARATOR) + RECORD_SEPARATOR;
	}
}
