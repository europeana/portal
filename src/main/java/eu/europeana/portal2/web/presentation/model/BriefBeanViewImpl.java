/*
 * Copyright 2007 EDL FOUNDATION
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */

package eu.europeana.portal2.web.presentation.model;

import java.util.List;
import java.util.Map;

import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.portal2.querymodel.query.FacetQueryLinks;
import eu.europeana.portal2.querymodel.query.FacetQueryLinksImpl;
import eu.europeana.portal2.web.model.facets.Facet;
import eu.europeana.portal2.web.model.spellcheck.SpellCheck;

/**
 * Decorates the brief views with links.
 * 
 * @author Serkan Demirel (2nd author), <serkan@blackbuilt.nl>
 * @see eu.europeana.definitions.model.RecordModel
 * @see eu.europeana.portal2.web.presentation.model.data.decorators.BriefBeanViewDecorator
 */
public class BriefBeanViewImpl implements BriefBeanView {

	private ResultPagination pagination;
	private List<? extends BriefBean> briefDocs;
	private List<FacetQueryLinks> queryLinks;
	private Map<String, String> facetLogs;
	private BriefBean matchDoc;
	private SpellCheck spellcheck;

	public BriefBeanViewImpl() {};
	
	/*
	public BriefBeanViewImpl(BeanQueryModelFactory factory,
			SolrQuery solrQuery, QueryResponse solrResponse,
			String requestQueryString) 
		throws UnsupportedEncodingException, EuropeanaQueryException 
	{
		pagination = createPagination(solrResponse, solrQuery, requestQueryString);
		briefDocs = factory.addIndexToBriefDocList(solrQuery,
				solrBinder.bindBriefDoc(solrResponse.getResults()));
		queryLinks = FacetQueryLinksImpl.createDecoratedFacets(solrQuery,
				solrResponse.getFacetFields());
		facetLogs = createFacetLogs(solrResponse);
		matchDoc = createBriefDoc(factory, solrResponse);
		spellcheck = solrResponse.getSpellCheckResponse();
	}
	*/

	/*
	private BriefBean createBriefDoc(BeanQueryModelFactory factory,
			QueryResponse solrResponse) {
		BriefBean briefDoc = null;
		SolrDocumentList matchDoc = (SolrDocumentList) solrResponse
				.getResponse().get("match");
		if (matchDoc != null) {
			List<? extends BriefBean> briefBeanList = solrBinder.bindBriefDoc(matchDoc);
			if (briefBeanList.size() > 0) {
				briefDoc = briefBeanList.get(0);
				String europeanaId = factory.createFullDocUrl(briefDoc.getId());
				briefDoc.setFullDocUrl(europeanaId);
			}
		}
		return briefDoc;
	}
	*/

	/*
	// todo: what are facet logs, how heavy, and is it needed?
	private Map<String, String> createFacetLogs(QueryResponse solrResponse) {
		Map<String, String> facetLogs = new HashMap<String, String>();
		List<FacetField> facetFieldList = solrResponse.getFacetFields();
		if (facetFieldList != null) {
			for (FacetField facetField : facetFieldList) {
				// only for LANGUAGE or COUNTRY field
				if (facetField.getName().equalsIgnoreCase("LANGUAGE")
						|| facetField.getName().equalsIgnoreCase("COUNTRY")) {
					StringBuilder out = new StringBuilder();
					List<FacetField.Count> list = facetField.getValues();
					if (list == null) {
						break;
					}
					int counter = 0;
					for (FacetField.Count count : list) {
						counter++;
						out.append(count.toString()).append(",");
						if (counter > 5) {
							break;
						}
					}
					// StringUtlis.join()!!
					facetLogs.put(facetField.getName(), out.toString()
							.substring(0, out.toString().length() - 1));
				}
			}
		}
		return facetLogs;
	}
	*/

	/*
	// createPagination(solrResponse, solrQuery, requestQueryString);
	public static ResultPagination createPagination(QueryResponse response, 
			SolrQuery query, String requestQueryString)
			throws EuropeanaQueryException {
		int numFound = (int) response.getResults().getNumFound();
		Boolean debug = query.getBool("debugQuery");
		String parsedQuery = "Information not available";
		if (debug != null && debug) {
			parsedQuery = String.valueOf(response.getDebugMap().get(
					"parsedquery_toString"));
		}
		return new ResultPaginationImpl(query, numFound, requestQueryString, parsedQuery);
	}
	*/

	@Override
	public List<? extends BriefBean> getBriefDocs() {
		return briefDocs;
	}

	@Override
	public List<FacetQueryLinks> getFacetQueryLinks() {
		return queryLinks;
	}

	@Override
	public ResultPagination getPagination() {
		return pagination;
	}

	@Override
	public Map<String, String> getFacetLogs() {
		return facetLogs;
	}

	@Override
	public BriefBean getMatchDoc() {
		return matchDoc;
	}

	@Override
	public SpellCheck getSpellCheck() {
		return spellcheck;
	}

	public void setSpellcheck(SpellCheck spellcheck) {
		this.spellcheck = spellcheck;
	}

	public void setBriefDocs(List<? extends BriefBean> briefDocs) {
		this.briefDocs = briefDocs;
	}

	public void setPagination(ResultPagination pagination) {
		this.pagination = pagination;
	}

	public void setQueryLinks(List<FacetQueryLinks> queryLinks) {
		this.queryLinks = queryLinks;
	}

	public void makeQueryLinks(List<Facet> facets, Query query) {
		this.queryLinks = FacetQueryLinksImpl.createDecoratedFacets(facets, query);
	}
}