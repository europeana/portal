/*
 * Copyright 2007-2013 The Europeana Foundation
 *
 *  Licenced under the EUPL, Version 1.1 (the "Licence") and subsequent versions as approved 
 *  by the European Commission;
 *  You may not use this work except in compliance with the Licence.
 *  
 *  You may obtain a copy of the Licence at:
 *  http://joinup.ec.europa.eu/software/page/eupl
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under 
 *  the Licence is distributed on an "AS IS" basis, without warranties or conditions of 
 *  any kind, either express or implied.
 *  See the Licence for the specific language governing permissions and limitations under 
 *  the Licence.
 */

package eu.europeana.portal2.web.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.response.FacetField;

import eu.europeana.corelib.definitions.edm.beans.BriefBean;
import eu.europeana.corelib.definitions.model.web.BreadCrumb;
import eu.europeana.corelib.definitions.solr.Facet;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.edm.exceptions.SolrTypeException;
import eu.europeana.corelib.edm.utils.SolrUtils;
import eu.europeana.corelib.search.SearchService;
import eu.europeana.corelib.search.model.ResultSet;
import eu.europeana.corelib.web.model.rights.RightReusabilityCategorizer;
import eu.europeana.corelib.web.utils.NavigationUtils;
import eu.europeana.corelib.web.utils.UrlBuilder;
import eu.europeana.portal2.web.model.ModelUtils;
import eu.europeana.portal2.web.presentation.SearchPageEnum;
import eu.europeana.portal2.web.presentation.model.submodel.BriefBeanView;
import eu.europeana.portal2.web.presentation.model.submodel.ResultPagination;
import eu.europeana.portal2.web.presentation.model.submodel.impl.BriefBeanViewImpl;
import eu.europeana.portal2.web.presentation.model.submodel.impl.ResultPaginationImpl;

public class SearchUtils {

	public static BriefBeanView createResults(
			SearchService searchService,
			Class<? extends BriefBean> clazz,
			String profile,
			Query query,
			int start,
			int rows,
			Map<String, String[]> params)
					throws SolrTypeException {

		BriefBeanViewImpl briefBeanView = new BriefBeanViewImpl();
		ResultSet<? extends BriefBean> resultSet = searchService.search(clazz, query);

		List<FacetField> facetFields = resultSet.getFacetFields();
		if (resultSet.getQueryFacets() != null) {
			List<FacetField> allQueryFacetsMap = eu.europeana.corelib.search.utils.SearchUtils.extractQueryFacets(resultSet.getQueryFacets());
			if (allQueryFacetsMap != null && !allQueryFacetsMap.isEmpty()) {
				facetFields.addAll(allQueryFacetsMap);
			}
		}

		briefBeanView.setBriefBeans(resultSet.getResults());

		if (StringUtils.containsIgnoreCase(profile, "facets") || StringUtils.containsIgnoreCase(profile, "portal")) {
			briefBeanView.makeQueryLinks(ModelUtils.conventFacetList(facetFields), query);
		}

		if (StringUtils.containsIgnoreCase(profile, "filters") || StringUtils.containsIgnoreCase(profile, "portal")) {
			briefBeanView.makeFilters(query, params);
		}

		List<BreadCrumb> breadCrumbs = null;
		if (StringUtils.containsIgnoreCase(profile, "breadcrumb") || StringUtils.containsIgnoreCase(profile, "portal")) {
			breadCrumbs = NavigationUtils.createBreadCrumbList(QueryUtil.escapeQuery(query));
		}

		if (StringUtils.containsIgnoreCase(profile, "spelling") || StringUtils.containsIgnoreCase(profile, "portal")) {
			briefBeanView.setSpellcheck(ModelUtils.convertSpellCheck(resultSet.getSpellcheck()));
		}

		briefBeanView.setFacetLogs(resultSet.getFacetFields());

//		if (StringUtils.containsIgnoreCase(profile, "suggestions") || StringUtils.containsIgnoreCase(profile, "portal")) {
//		}

		ResultPagination pagination = new ResultPaginationImpl(start, rows, (int)resultSet.getResultSize(), query, breadCrumbs);
		briefBeanView.setPagination(pagination);
		return briefBeanView;
	}

	public static UrlBuilder createSearchUrl(String portalname, SearchPageEnum returnTo, 
			String searchTerm, String[] qf, String start)
					throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		sb.append("/").append(portalname).append("/").append(returnTo.getPageInfo().getPageName());
		UrlBuilder url = new UrlBuilder(sb.toString());
		if (searchTerm != null) {
			url.addParam("query", searchTerm, true);
		}
		if (qf != null) {
			url.addParam("qf", qf, true);
		}
		url.addParam("start", start, true);

		return url;
	}

	public static Query createRightsFacetQuery(String q, String[] qf) {
		List<String> refinements = new ArrayList<String>();
		if (qf != null) {
			for (String value : qf) {
				if (!value.startsWith("RIGHTS:")) {
					refinements.add(value);
				}
			}
		}
		String[] filteredQf = refinements.toArray(new String[refinements.size()]);

		Query query = new Query(q)
			.setRefinements(filteredQf)
			.setValueReplacements(RightReusabilityCategorizer.mapValueReplacements(qf))
			.setPageSize(0)
			.setStart(0)
			.setParameter("facet.mincount", "1") // .setParameter("f.YEAR.facet.mincount", "1")
			.setProduceFacetUnion(false)
			.setAllowSpellcheck(false);
		query.setFacet(Facet.RIGHTS.toString());
		return query;
	}
}
