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

package eu.europeana.corelib.solr.service.mock;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField.Count;

import eu.europeana.corelib.definitions.edm.beans.BriefBean;
import eu.europeana.corelib.definitions.edm.beans.FullBean;
import eu.europeana.corelib.definitions.edm.beans.IdBean;
import eu.europeana.corelib.definitions.edm.entity.Aggregation;
import eu.europeana.corelib.definitions.solr.DocType;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.definitions.solr.model.Term;
import eu.europeana.corelib.edm.exceptions.SolrTypeException;
import eu.europeana.corelib.neo4j.entity.Neo4jBean;
import eu.europeana.corelib.neo4j.entity.Neo4jStructBean;
import eu.europeana.corelib.search.SearchService;
import eu.europeana.corelib.search.model.ResultSet;
import eu.europeana.corelib.solr.entity.AggregationImpl;
import eu.europeana.corelib.solr.service.mock.bean.BriefBeanMock;
import eu.europeana.corelib.solr.service.mock.bean.FullBeanMock;
import eu.europeana.corelib.utils.EuropeanaUriUtils;

/**
 * @author Willem-Jan Boogerd <www.eledge.net/contact>
 * 
 * @see eu.europeana.corelib.edm.service.SearchService
 */
public class SearchServiceMockSelenium implements SearchService {

	public static final String[] TITLE = new String[]{"Mock Title"};
	public static final String[] AUTHOR = new String[]{"Mock Author"};
	public static final String[] THUMBNAIL = new String[]{"MockThumbnail.jpg"};
	public static final List<? extends Aggregation> aggregations2 = new ArrayList<AggregationImpl>();

	private final Map<String, BriefBean> briefBeans;

	public SearchServiceMockSelenium(){
		super();

		this.briefBeans = new TreeMap<String, BriefBean>();

		for(int i=1; i<=200; i++){
			String id = "/selenium1/" + StringUtils.leftPad( String.valueOf(i) , 3, "0");
			this.briefBeans.put(id, new BriefBeanMock(id, DocType.IMAGE, "Test Title " + i));
		}
	}

	@Override
	public FullBean findById(String europeanaObjectId,boolean similarItems) {

		System.err.println("look up briefbean on " + europeanaObjectId );

		FullBean mockBean = new FullBeanMock(briefBeans.get(europeanaObjectId));
		return mockBean;
	}

	@Override
	public FullBean findById(String collectionId, String recordId, boolean similarItems) {
		return findById(EuropeanaUriUtils.createEuropeanaId(collectionId, recordId),similarItems);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IdBean> ResultSet<T> search(Class<T> beanClazz, Query query) {

		System.err.println("SearchServiceMock (Selenium) - search()");

		ResultSet<T> resultSet = new ResultSet<T>();

		query.getPageSize();
		query.getStart();

		List<T> fullResults = new ArrayList<T>((Collection<T>)this.briefBeans.values());

		int lastResult = query.getStart() + query.getPageSize();
		if(lastResult > fullResults.size()){
			lastResult = fullResults.size();
		}

		List<T> subResults = fullResults.subList(query.getStart(), lastResult);

		resultSet.setResults( subResults ) ;
		resultSet.setResultSize(fullResults.size());

		return resultSet;
	}

	@Override
	public List<Term> suggestions(String query, int pageSize) {
		return null;
	}

	@Override
	public List<Count> createCollections(String facetFieldName, String queryString, String... refinements)
			throws SolrTypeException {
		return null;
	}

	@Override
	public FullBean resolve(String collectionId, String recordId,boolean similarItems)
			throws SolrTypeException {
		return null;
	}

	@Override
	public FullBean resolve(String europeanaObjectId,boolean similarItems) throws SolrTypeException {
		return null;
	}

	@Override
	public List<BriefBean> findMoreLikeThis(String europeanaObjectId)
			throws SolrServerException {
		return null;
	}

	@Override
	public List<Term> suggestions(String query, int pageSize, String field)
			throws SolrTypeException {
		return null;
	}

	@Override
	public Map<String, Integer> seeAlso(List<String> params) {
		return null;
	}

	@Override
	public List<BriefBean> findMoreLikeThis(String europeanaObjectId, int count)
			throws SolrServerException {
		return null;
	}

	@Override
	public <T extends IdBean> ResultSet<T> sitemap(Class<T> beanInterface,
			Query query) throws SolrTypeException {
		return null;
	}

	@Override
	public Date getLastSolrUpdate() throws SolrServerException, IOException {
		return null;
	}

	@Override
	public String resolveId(String europeanaObjectId) {
		return null;
	}

	@Override
	public String resolveId(String collectionId, String recordId) {
		return null;
	}

	@Override
	public Map<String, Integer> queryFacetSearch(String query, String[] qf,
			List<String> queries) {
		return null;
	}

	@Override
	public Neo4jBean getHierarchicalBean(String nodeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Neo4jBean> getChildren(String nodeId, int offset, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Neo4jBean> getChildren(String nodeId, int offset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Neo4jBean> getChildren(String nodeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Neo4jBean> getPreceedingSiblings(String nodeId, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Neo4jBean> getPreceedingSiblings(String nodeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Neo4jBean> getFollowingSiblings(String nodeId, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Neo4jBean> getFollowingSiblings(String nodeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getChildrenCount(String nodeId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Neo4jStructBean getInitialStruct(String nodeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isHierarchy(String nodeId) {
		// TODO Auto-generated method stub
		return false;
	}

    @Override
    public Integer search(Integer integer, String s, String s2, Boolean aBoolean, Boolean aBoolean2, String s3, String s4, Boolean aBoolean3, String s5, Boolean aBoolean4, String s6) {
        return null;
    }
}
