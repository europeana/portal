package eu.europeana.portal2.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import eu.europeana.corelib.definitions.solr.model.Query;

public class QueryUtil {

	public static final Pattern SQUARE_BRACKET_PATTERN = Pattern.compile("^\\[(.*?)\\]$");

	private static final Logger log = Logger.getLogger(QueryUtil.class.getName());

	public static String[] getFilterQueriesWithoutPhrases(Query solrQuery) {
		String[] filterQueries = solrQuery.getRefinements();
		if (filterQueries == null) {
			return null;
		}
		List<String> nonPhraseFilterQueries = new ArrayList<String>(
				filterQueries.length);
		for (String facetTerm : filterQueries) {
			if (facetTerm.contains(":")) {
				int colon = facetTerm.indexOf(":");
				String facetName = facetTerm.substring(0, colon);
				if (facetName.contains("!tag")) {
					facetName = facetName.replaceFirst("\\{!tag=.*?\\}", "");
				}
				String facetValue = facetTerm.substring(colon + 1);
				if (facetValue.length() >= 2 && facetValue.startsWith("\"")
						&& facetValue.endsWith("\"")) {
					facetValue = facetValue.substring(1,
							facetValue.length() - 1);
				}
				nonPhraseFilterQueries.add(MessageFormat.format("{0}:{1}",
						facetName, facetValue));
			}
		}
		return nonPhraseFilterQueries.toArray(new String[nonPhraseFilterQueries
				.size()]);
	}

	public static Query escapeQuery(Query originalQuery) {
		// Query query = new Query(originalQuery); //(Query)originalQuery.clone();// 
		Query query;
		try {
			query = originalQuery.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			log.severe(e.getMessage());
			return originalQuery;
		}// 
		String[] refinements = new String[query.getRefinements().length];
		int i = 0;
		for (String refinement : query.getRefinements()) {
			if (refinement.contains(":")) {
				String[] parts = StringUtils.split(refinement, ":", 2);
				if (!parts[0].equals("TYPE") && parts[1].contains(" ")) {
					refinement = parts[0] + ":" + encode(parts[1]);
				}
			}
			refinements[i++] = refinement;
		}
		query.setRefinements(refinements);
		return query;
	}

	public static String removeQuotes(String display) {
		if (display.contains(":")) {
			String[] parts = StringUtils.split(display, ":", 2);
			if (!parts[0].equals("TYPE") && parts[1].contains("%")) {
				String value = decode(parts[1]);
				if (value.startsWith("\"") && value.endsWith("\"")) {
					value = value.substring(1, value.length()-1);
				}
				display = parts[0] + ":" + value;
			}
		}
		return display;
	}

	/**
	 * Creates a phrase part of facet query.
	 * 
	 * @param fieldName
	 *            The field name
	 * @param value
	 *            The field value
	 * @return The URL encoded phrase
	 */
	public static String createPhraseValue(String fieldName, String value) {
		if (fieldName.equals("TYPE") || !value.contains(" ")) {
			return value;
		} else {
			return encode('"' + value + '"');
		}
	}
	
	public static String encode(String value) {
		try {
			value = URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.severe(e.getMessage());
		}
		return value;
	}

	public static String decode(String value) {
		try {
			value = URLDecoder.decode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.severe(e.getMessage());
		}
		return value;
	}

	public static String escapeValue(String text) {
		return escapeQuote(
				escapeSquareBrackets(text));
	}

	public static String escapeQuote(String text) {
		return text.replaceAll("\"", "\\\\\"");
	}

	public static String escapeSquareBrackets(String text) {
		Matcher matcher = QueryUtil.SQUARE_BRACKET_PATTERN.matcher(text);
		if (matcher.find()) {
			text = "\\[" + matcher.group(1) + "\\]";
		}
		return text;
	}
}
