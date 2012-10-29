package eu.europeana.portal2.querymodel.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.portal2.web.model.facets.Facet;
import eu.europeana.portal2.web.model.facets.LabelFrequency;
import eu.europeana.portal2.web.util.QueryUtil;

public class FacetQueryLinksImpl implements FacetQueryLinks {

	private final Logger log = Logger.getLogger(getClass().getName());

	private static final String FACET_PROMPT = "&qf=";
	private static final String RIGHTS_FACET = "RIGHTS";
	private String type;
	private boolean facetSelected = false;
	private List<FacetCountLink> links = new ArrayList<FacetCountLink>() {

		private static final long serialVersionUID = 8698647778113603995L;

		@Override
		public boolean add(FacetCountLink newFacetCountLink) {
			if (contains(newFacetCountLink) && RIGHTS_FACET.equals(type)) {
				FacetCountLink existing = get(indexOf(newFacetCountLink));
				existing.update(newFacetCountLink);
				return false;
			}
			return super.add(newFacetCountLink);
		}
	};

	public static List<FacetQueryLinks> createDecoratedFacets(List<Facet> facetFields, Query solrQuery) {
		List<FacetQueryLinks> list = new ArrayList<FacetQueryLinks>();
		if (facetFields != null) {
			for (Facet facetField : facetFields) {
				list.add(new FacetQueryLinksImpl(facetField, solrQuery));
			}
		}
		return list;
	}

	private FacetQueryLinksImpl(Facet facetField, Query query) {
		this.type = facetField.getName();
		if (facetField.getFields().isEmpty()) {
			return;
		}
		Map<String, List<String>> refinements = QueryUtil.getFilterQueriesWithoutPhrases(query);
		StringBuilder baseUrl = new StringBuilder();
		if (refinements != null) {
			for (String term : refinements.get(QueryUtil.REFINEMENTS)) {
				baseUrl.append(FACET_PROMPT).append(term);
			}
		}

		for (LabelFrequency item : facetField.getFields()) {
			if (isTemporarilyPreventYear0000(this.type, item.getLabel())) {
				continue;
			}

			boolean remove = false;
			StringBuilder url = new StringBuilder(baseUrl.toString());
			// iterating over actual qf values
			if (query.getRefinements() != null) {
				for (String qfTerm : refinements.get(QueryUtil.FACETS)) {
					if (!qfTerm.equals("-TYPE:Wikipedia")) {
						String[] parts = qfTerm.split(":", 2);
						String qfField = parts[0];
						String qfValue = parts[1];
						if (isTemporarilyPreventYear0000(qfField, qfValue)) {
							continue;
						}
						boolean doAppend = true;
						String comparableQf = qfValue;
						if (qfField.equals(RIGHTS_FACET)) {
							qfValue = '"' + qfValue + '"';
						}
						if (qfField.equalsIgnoreCase(facetField.getName())) {
							if (QueryUtil.escapeValue(item.getLabel()).equalsIgnoreCase(comparableQf)
								|| comparableQf.equals(EuropeanaRightsConverter.convertCc(item.getLabel()))) {
								remove = true;
								facetSelected = true;
								doAppend = false;
							}
						}
						if (doAppend) {
							url.append(FACET_PROMPT).append(qfField).append(':')
								.append(QueryUtil.createPhraseValue(qfField, qfValue));
						}
					}
				}
			}

			// adding the current facet to the query link
			if (!remove) {
				url.append(FACET_PROMPT);
				url.append(facetField.getName());
				url.append(":");
				if (RIGHTS_FACET.equals(type)) {
					EuropeanaRightsConverter.License license = EuropeanaRightsConverter.convert(item.getLabel());
					String value = (license.getModifiedURI() != null) ? license.getModifiedURI() : license.getOriginalURI();
					url.append('"').append(value).append('"');
				} else {
					// escape Solr special chars in item.label
					url.append(
						QueryUtil.createPhraseValue(
							facetField.getName(), 
							QueryUtil.escapeValue(item.getLabel())
						)
					);
				}
			}

			if (RIGHTS_FACET.equals(type)) {
				EuropeanaRightsConverter.License license = EuropeanaRightsConverter.convert(item.getLabel());
				item.setLabel(license.getOriginalURI());
				links.add(new FacetCountLinkImpl(
						RightsOption.safeValueByUrl(license.getOriginalURI()), 
						item, url.toString(), remove));
			} else {
				links.add(new FacetCountLinkImpl(item, url.toString(), remove));
			}
		}
	}

	/**
	 * At one point the normalizer was letting unparseable values of year, coded
	 * ast 0000, through to the index. This bandage solution prevents them from
	 * being presented.
	 * 
	 * @param facetName
	 *            year
	 * @param facetValue
	 *            0000
	 * 
	 * @return true if it matches
	 */

	private static boolean isTemporarilyPreventYear0000(String facetName,
			String facetValue) {
		return "YEAR".equals(facetName) && "0000".equals(facetValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.core.querymodel.query.FacetQueryLinks#getType()
	 */
	@Override
	public String getType() {
		return type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.core.querymodel.query.FacetQueryLinks#getLinks()
	 */
	@Override
	public List<FacetCountLink> getLinks() {
		return links;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.core.querymodel.query.FacetQueryLinks#isSelected()
	 */
	@Override
	public boolean isSelected() {
		return facetSelected;
	}

	@Override
	public String toString() {
		return type + " (" + links +")";
	}
}
