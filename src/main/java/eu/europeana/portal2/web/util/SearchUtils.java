/*
 * Copyright 2007-2012 The Europeana Foundation
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
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import eu.europeana.corelib.definitions.model.web.BreadCrumb;
import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.solr.exceptions.SolrTypeException;
import eu.europeana.corelib.solr.model.ResultSet;
import eu.europeana.corelib.solr.service.SearchService;
import eu.europeana.corelib.web.utils.NavigationUtils;
import eu.europeana.portal2.web.model.ModelUtils;
import eu.europeana.portal2.web.presentation.SearchPageEnum;
import eu.europeana.portal2.web.presentation.model.BriefBeanView;
import eu.europeana.portal2.web.presentation.model.BriefBeanViewImpl;
import eu.europeana.portal2.web.presentation.model.ResultPagination;
import eu.europeana.portal2.web.presentation.model.ResultPaginationImpl;
import eu.europeana.portal2.web.presentation.utils.UrlBuilder;

public class SearchUtils {

	private static final Logger log = Logger.getLogger(SearchUtils.class.getName());

	public static BriefBeanView createResults(SearchService searchService,
			Class<? extends BriefBean> clazz, String profile, Query query,
			int start, int rows,
			Map<String, String[]> params) 
					throws SolrTypeException {
		BriefBeanViewImpl briefBeanView = new BriefBeanViewImpl();

		ResultSet<? extends BriefBean> resultSet = searchService.search(clazz, query);
		briefBeanView.setBriefBeans(resultSet.getResults());

		if (StringUtils.containsIgnoreCase(profile, "facets") || StringUtils.containsIgnoreCase(profile, "portal")) {
			briefBeanView.makeQueryLinks(ModelUtils.conventFacetList(resultSet.getFacetFields()), query);
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
		url.addParam("query", URLEncoder.encode(searchTerm, "UTF-8"), true);
		if (qf != null) {
			url.addParam("qf", qf, true);
		}
		url.addParam("start", start, true);

		return url;
	}
}
