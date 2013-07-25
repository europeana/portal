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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.beans.FullBean;
import eu.europeana.corelib.definitions.solr.entity.Proxy;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.solr.exceptions.SolrTypeException;
import eu.europeana.corelib.solr.service.SearchService;

/**
 * Do not ever touch this class! It is persisted to XML and serves as document
 * cache.
 * 
 * @author Borys Omelayenko
 * @author Serkan Demirel <serkan@blackbuilt.nl>
 */
public class FullBeanViewImpl implements FullBeanView {
	private static final long serialVersionUID = -4971453940874288310L;
	
	private final Logger log = LoggerFactory.getLogger(FullBeanViewImpl.class);

	// Do not ever touch these fields as they are persisted as document cache
	private FullBean fullDoc;
	private DocIdWindowPager docIdWindowPager;
	private List<? extends BriefBean> relatedItems;

	// hierarchical view
	private List<? extends BriefBean> parents;
	private List<? extends BriefBean> children;

	public FullBeanViewImpl(FullBean fullDoc, Map<String, String[]> httpParameters,
			Query query, SearchService searchService) {
		this.fullDoc = fullDoc;
		// this.relatedItems = fullDoc.getRelatedItems();
		this.parents = findParents();
		this.children = findChildren();

		Class<? extends BriefBean> clazz = BriefBean.class;
		this.docIdWindowPager = createPager(fullDoc.getAbout(), httpParameters, query, searchService, clazz);
	}

	private List<? extends BriefBean> findParents() {
		List<String> items = new ArrayList<String>();
		if (fullDoc != null) {
			for (Proxy proxy : fullDoc.getProxies()) {
				if (proxy.getDctermsIsPartOf() == null) {
					continue;
				}
				for (Entry<String, List<String>> item : proxy.getDctermsIsPartOf().entrySet()) {
					items.addAll(item.getValue());
				}
			}
		}
		return new ArrayList<BriefBean>();
	}

	private List<? extends BriefBean> findChildren() {
		List<String> items = new ArrayList<String>();
		if (fullDoc != null) {
			for (Proxy proxy : fullDoc.getProxies()) {
				if (proxy.getDctermsHasPart() == null) {
					continue;
				}
				for (Entry<String, List<String>> item : proxy.getDctermsHasPart().entrySet()) {
					items.addAll(item.getValue());
				}
			}
		}
		return new ArrayList<BriefBean>();
	}

	public DocIdWindowPager createPager(String id, Map<String, String[]> httpParameters,
			Query query, SearchService searchService, Class<? extends BriefBean> clazz) {
		DocIdWindowPager pager = null;
		try {
			pager = DocIdWindowPagerImpl.fetchPager(id, httpParameters, query, searchService, clazz);
		} catch (SolrTypeException e) {
			log.error("SolrTypeException: " + e.getLocalizedMessage(),e);
		}
		return pager;
	}

	/*
    private void createPager(BeanQueryModelFactory factory, Map<String, String[]> params) throws SolrServerException, EuropeanaQueryException {
        docIdWindowPager = null;
        if (params.containsKey("query")) {
            try {
                docIdWindowPager = DocIdWindowPagerImpl.fetchPager(params, factory.createFromQueryParams(params), factory.getActiveSolrServer(), factory.getIdBean());
            }
            catch (Exception e) {
                log.error("Failed to create pager for " + StringUtils.join(params.values(), ";"), e);
            }
        }
    }
    */

	@Override
	public DocIdWindowPager getDocIdWindowPager() throws Exception,
			UnsupportedEncodingException {
		return docIdWindowPager;
	}

	@Override
	public List<? extends BriefBean> getRelatedItems() {
		return relatedItems;
	}

	@Override
	public List<? extends BriefBean> getChildren() {
		return children;
	}

	@Override
	public BriefBean getParent() {
		// TODO: one or more parents?
		if (parents != null && parents.size() > 0) {
			return parents.get(0);
		}
		return null;
	}

	@Override
	public List<? extends BriefBean> getParents() {
		return parents;
	}

	@Override
	public FullBean getFullDoc() {
		return fullDoc;
	}

	/*
	public FullBeanViewImpl(BeanQueryModelFactory factory, SolrQuery solrQuery,
			QueryResponse solrResponse, Map<String, String[]> params)
			throws EuropeanaQueryException, SolrServerException {
		fullDoc = createFullDoc(factory, solrResponse, params);
		// todo: also from SolrBinder.bind
		relatedItems = filterRelatedDocs(factory.addIndexToBriefDocList(
				solrQuery, solrBinder.bindBriefDoc(solrResponse.getResults())));
		createPager(factory, params);
		// disabled as this option is not in use yet...
		// if ( StringArrayUtils.isNotBlank(fullDoc.getDcTermsIsPartOf()) ) {
		// parents = new ArrayList<BriefDoc>();
		// fetchParent(factory, fullDoc.getDcTermsIsPartOf()[0]);
		// }
		// if ( StringArrayUtils.isNotBlank(fullDoc.getDcTermsHasPart()) ) {
		// fetchChildren(factory, fullDoc.getDcIdentifier()[0]);
		// }
	}

	private void createPager(BeanQueryModelFactory factory,
			Map<String, String[]> params) throws SolrServerException,
			EuropeanaQueryException {
		docIdWindowPager = null;
		if (params.containsKey("query")) {
			try {
				docIdWindowPager = DocIdWindowPagerImpl.fetchPager(params,
						factory.createFromQueryParams(params),
						factory.getActiveSolrServer(), factory.getIdBean());
			} catch (Exception e) {
				log.severe(
						"Failed to create pager for "
								+ StringUtils.join(params.values(), ";"), e);
			}
		}
	}

	private void fetchParent(BeanQueryModelFactory factory, String id) {
		try {
			Map<String, String[]> parameterMap = new HashMap<String, String[]>();
			parameterMap.put("query", new String[]{"dc_identifier:" + id});
			parameterMap.put("rows", new String[]{Integer.toString(1)});
			SolrQuery solrQuery = factory.createFromQueryParams(parameterMap);
			List<? extends BriefBean> results = factory.getBriefDocs(solrQuery);
			if ((results != null) && (results.size() == 1)) {
				BriefBean parent = results.get(0);
				if (StringArrayUtils.isNotBlank(parent.getDctermsIsPartOf())) {
					fetchParent(factory, parent.getDctermsIsPartOf()[0]);
				}
				parents.add(parent);
			}
		} catch (Exception e) {
			// only get parent if successfully, don't let process of showing
			// fulldoc fail on this
		}
	}

	private void fetchChildren(BeanQueryModelFactory factory, String id) {
		try {
			Map<String, String[]> parameterMap = new HashMap<String, String[]>();
			parameterMap
					.put("query", new String[] { "dcterms_isPartOf:" + id });
			SolrQuery solrQuery = factory.createFromQueryParams(parameterMap);
			List<? extends BriefBean> results = factory.getBriefDocs(solrQuery);
			if ((results != null) && (results.size() > 0)) {
				children = results;
			}
		} catch (Exception e) {
			// only get children if successfully, don't let process of showing
			// fulldoc fail on this
		}
	}

	@Override
	public DocIdWindowPager getDocIdWindowPager() throws Exception {
		return docIdWindowPager;
	}

	@Override
	public List<? extends BriefBean> getRelatedItems() {
		return relatedItems;
	}

	@Override
	public FullBean getFullDoc() throws EuropeanaQueryException {
		return fullDoc;
	}

	@Override
	public BriefBean getParent() {
		if (parents != null) {
			if (!parents.isEmpty()) {
				return parents.get(parents.size() - 1);
			}
		}
		return null;
	}

	@Override
	public List<BriefBean> getParents() {
		return parents;
	}

	@Override
	public List<? extends BriefBean> getChildren() {
		return children;
	}

	private FullBean createFullDoc(BeanQueryModelFactory factory,
			QueryResponse solrResponse, Map<String, String[]> params)
				throws EuropeanaQueryException {
		SolrDocumentList matchDoc = (SolrDocumentList) solrResponse
				.getResponse().get("match");
		// TODO: populate from RecordModel
		List<? extends FullBean> fullBean = solrBinder.bindFullDoc(matchDoc);
		// List<FullDoc> fullBean = factory.getBinder().getBeans(FullDoc.class,
		// matchDoc);

		// if the record is not found give useful error message
		// if fact, as EuropeanaId is abandoned, this message is always the same
		if (fullBean.size() == 0) {
			ProblemType problem = ProblemType.RECORD_NOT_FOUND;
			throw new EuropeanaQueryException(problem.toString());
		}
		return fullBean.get(0);
	}

	private static final int MAX_RELATED_DOCS = 10;

	private List<? extends BriefBean> filterRelatedDocs(
			List<? extends BriefBean> briefDocList) {
		if ((briefDocList != null) && (briefDocList.size() > MAX_RELATED_DOCS)) {
			return briefDocList.subList(0, MAX_RELATED_DOCS);
		}
		return briefDocList;
	}
	*/
}
