package eu.europeana.portal2.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import eu.europeana.corelib.definitions.solr.model.Query;

public class QueryUtil {

	private static final Logger log = Logger.getLogger(QueryUtil.class.getName());

    public static String[] getFilterQueriesWithoutPhrases(Query solrQuery) {
        String[] filterQueries = solrQuery.getRefinements();
        if (filterQueries == null) {
            return null;
        }
        List<String> nonPhraseFilterQueries = new ArrayList<String>(filterQueries.length);
        for (String facetTerm : filterQueries) {
            if (facetTerm.contains(":")) {
                int colon = facetTerm.indexOf(":");
                String facetName = facetTerm.substring(0, colon);
                if (facetName.contains("!tag")) {
                    facetName = facetName.replaceFirst("\\{!tag=.*?\\}", "");
                }
                String facetValue = facetTerm.substring(colon + 1);
                if (facetValue.length() >= 2 && facetValue.startsWith("\"") && facetValue.endsWith("\"")) {
                    facetValue = facetValue.substring(1, facetValue.length() - 1);
                }
                nonPhraseFilterQueries.add(MessageFormat.format("{0}:{1}", facetName, facetValue));
            }
        }
        return nonPhraseFilterQueries.toArray(new String[nonPhraseFilterQueries.size()]);
    }

	/**
	 * Creates a phrase part of facet query.
	 * 
	 * @param fieldName 
	 *   The field name
	 * @param value
	 *   The field value
	 * @return
	 *   The URL encoded phrase
	 */
	public static String createPhraseValue(String fieldName, String value) {
		if (fieldName.equals("TYPE") || !value.contains(" ")) {
			return value;
		}
		else {
			String encoded = '"' + value + '"';
			try {
				encoded = URLEncoder.encode(encoded, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				log.severe(e.getMessage());
			}
			return encoded;
		}
	}

}
