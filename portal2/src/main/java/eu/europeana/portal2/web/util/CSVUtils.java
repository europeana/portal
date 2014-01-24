package eu.europeana.portal2.web.util;

import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Utility class for creating CSV values
 *
 * @author peter.kiraly@kb.nl
 */
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
		return String.format("\"%s\"", field);
	}

	/**
	 * Encode a record (list of fields) for usage in CSV
	 */
	public static String encodeRecords(List<List<String>> rows) {
		StringBuilder sb = new StringBuilder();
		for (List<String> fields : rows) {
			sb.append(encodeRecord(fields));
		}
		return sb.toString();
	}

	/**
	 * Encode a record (list of fields) for usage in CSV
	 */
	public static String encodeRecord(List<String> fields) {
		return String.format("%s%s", StringUtils.join(fields, FIELD_SEPARATOR), RECORD_SEPARATOR);
	}
}
