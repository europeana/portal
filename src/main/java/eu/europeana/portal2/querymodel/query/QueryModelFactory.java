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

package eu.europeana.portal2.querymodel.query;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.response.QueryResponse;

import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.solr.exceptions.EuropeanaQueryException;
import eu.europeana.portal2.web.presentation.model.BriefBeanView;
import eu.europeana.portal2.web.presentation.model.FullBeanView;

/**
 * Build Solr queries.
 * 
 * @author Borys Omelayenko
 * @author Gerald de Jong <geralddejong@gmail.com>
 * @author Sjoerd Siebinga <sjoerd.siebinga@gmail.com>
 * @author Serkan Demirel <serkan@blackbuilt.nl>
 */

public interface QueryModelFactory {

	SolrQuery createFromQueryParams(Map<String, String[]> params)
			throws EuropeanaQueryException;

	/**
	 * Create the brief view from the solr query.
	 * 
	 * @param solrQuery
	 *            The Solr query.
	 * @param requestQueryString
	 *            The HTTP query string.
	 * 
	 * @return The constructed brief view.
	 * 
	 * @throws EuropeanaQueryException
	 *             Something went wrong while talking to Solr.
	 * @throws UnsupportedEncodingException
	 *             Thrown when encoding is not supported.
	 */
	BriefBeanView getBriefResultView(SolrQuery solrQuery, String requestQueryString) 
			throws EuropeanaQueryException, UnsupportedEncodingException;

	BriefBeanView getBriefResultView(SolrQuery solrQuery,
			String requestQueryString, String[] removeFilters)
			throws EuropeanaQueryException, UnsupportedEncodingException;

	List<? extends BriefBean> getBriefDocs(SolrQuery solrQuery)
			throws EuropeanaQueryException, UnsupportedEncodingException;

	FullBeanView getFullResultView(String europeanaUri, Map<String, String[]> params) 
			throws EuropeanaQueryException, SolrServerException;

	// TermBeanView getTermBeanView(SolrQuery solrQuery, String
	// requestQueryString) throws EuropeanaQueryException,
	// UnsupportedEncodingException;

	QueryResponse getSolrResponse(SolrQuery solrQuery)
			throws EuropeanaQueryException;

	DocumentObjectBinder getBinder();
}