package eu.europeana.portal2.querymodel.query;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eu.europeana.corelib.definitions.model.RightsOption;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.web.service.impl.EuropeanaUrlServiceImpl;
import eu.europeana.corelib.web.utils.UrlBuilder;
import eu.europeana.portal2.web.model.facets.Facet;
import eu.europeana.portal2.web.model.facets.LabelFrequency;
import eu.europeana.portal2.web.util.QueryUtil;

public class FacetQueryLinksImpl implements FacetQueryLinks {

	private static final String NON_WIKIPEDIA = "-TYPE:Wikipedia";
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

		String[] queryRefinements = query.getRefinements(false);

		for (LabelFrequency item : facetField.getFields()) {
			if (isTemporarilyPreventYear0000(this.type, item.getLabel())) {
				continue;
			}

			boolean remove = false;
			UrlBuilder url = null;
			try {
				url = EuropeanaUrlServiceImpl.getBeanInstance().getPortalSearch(true, query.getQuery(), String.valueOf(query.getPageSize()));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			// iterating over actual qf values
			if (queryRefinements != null) {
				for (String qfTerm : refinements.get(QueryUtil.FACETS)) {
					if (!qfTerm.equals(NON_WIKIPEDIA)) {
						String[] parts = qfTerm.split(":", 2);
						String qfField = parts[0];
						String qfValue = parts[1];
						if (isTemporarilyPreventYear0000(qfField, qfValue)) {
							continue;
						}

						boolean doAppend = true;
						if (qfField.equalsIgnoreCase(facetField.getName())) {
							String comparable = qfField.equals(RIGHTS_FACET) ? QueryUtil.removeTruncation(qfValue) : qfValue;
							if (QueryUtil.escapeValue(item.getLabel().trim()).equalsIgnoreCase(comparable)
								|| (qfField.equals(RIGHTS_FACET) 
									&& comparable.equals(
										QueryUtil.removeTruncation(
											EuropeanaRightsConverter.convertCc(
												item.getLabel().trim()))))) {
								remove = true;
								facetSelected = true;
								doAppend = false;
							}
						}

						if (doAppend) {
							if (qfField.equals(RIGHTS_FACET)) {
								if (!qfValue.endsWith("*") && !qfValue.endsWith("\"")) {
									qfValue = '"' + qfValue + '"';
								}
							} else {
								qfValue = QueryUtil.createPhraseValue(qfField, qfValue);
							}
							url.addMultiParam("qf", String.format("%s:%s", qfField, qfValue));
						}
					}
				}
			}

			// adding the current facet to the query link
			if (!remove) {
				String value;
				if (RIGHTS_FACET.equals(type)) {
					EuropeanaRightsConverter.License license = EuropeanaRightsConverter.convert(item.getLabel().trim());
					value = (license.isModified()) ? license.getModifiedURI() : license.getOriginalURI() + "*";
				} else {
					// escape Solr special chars in item.label
					value = QueryUtil.createPhraseValue(facetField.getName(), QueryUtil.escapeValue(item.getLabel()));
				}
				url.addMultiParam("qf", String.format("%s:%s", facetField.getName(), value));
			}

			if (RIGHTS_FACET.equals(type)) {
				EuropeanaRightsConverter.License license = EuropeanaRightsConverter.convert(item.getLabel());
				item.setLabel(license.getOriginalURI());
				links.add(
					new FacetCountLinkImpl(
						RightsOption.safeValueByUrl(license.getOriginalURI()),
						item,
						url.toString(),
						remove
					)
				);
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
