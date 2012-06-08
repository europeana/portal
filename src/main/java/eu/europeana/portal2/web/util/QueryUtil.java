package eu.europeana.portal2.web.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import eu.europeana.corelib.definitions.solr.model.Query;

public class QueryUtil {

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

}
