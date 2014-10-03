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

package eu.europeana.portal2.web.presentation.model.submodel.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.response.FacetField;

import eu.europeana.corelib.definitions.ApplicationContextContainer;
import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.web.service.EuropeanaUrlService;
import eu.europeana.corelib.web.utils.UrlBuilder;
import eu.europeana.portal2.querymodel.query.FacetQueryLinks;
import eu.europeana.portal2.querymodel.query.FacetQueryLinksImpl;
import eu.europeana.portal2.web.model.facets.Facet;
import eu.europeana.portal2.web.model.spellcheck.SpellCheck;
import eu.europeana.portal2.web.presentation.model.submodel.BriefBeanView;
import eu.europeana.portal2.web.presentation.model.submodel.ResultPagination;
import eu.europeana.portal2.web.presentation.model.submodel.SearchFilter;
import eu.europeana.portal2.web.presentation.model.submodel.SearchLabel;
import eu.europeana.portal2.web.presentation.model.submodel.SearchParam;
import eu.europeana.portal2.web.util.SearchFilterUtil;

/**
 * Decorates the brief views with links.
 * 
 * @author Serkan Demirel (2nd author), <serkan@blackbuilt.nl>
 * @see eu.europeana.definitions.model.RecordModel
 * @see eu.europeana.portal2.web.presentation.model.data.decorators.BriefBeanViewDecorator
 */
public class BriefBeanViewImpl implements BriefBeanView {

	private ResultPagination pagination;
	private List<? extends BriefBean> briefBeans;
	private List<FacetQueryLinks> queryLinks;
	private List<SearchFilter> searchFilters;
	private Map<String, String> facetLogs;
	private BriefBean matchDoc;
	private SpellCheck spellcheck;

	public BriefBeanViewImpl() {};

	public void setFacetLogs(List<FacetField> facetFieldList) {
		this.facetLogs = createFacetLogs(facetFieldList);
	}

	// TODO: what are facet logs, how heavy, and is it needed?
	private Map<String, String> createFacetLogs(List<FacetField> facetFieldList) {
		Map<String, String> facetLogs = new HashMap<String, String>();
		if (facetFieldList != null) {
			for (FacetField facetField : facetFieldList) {
				// only for LANGUAGE or COUNTRY field
				if (facetField.getName().equalsIgnoreCase("LANGUAGE")
						|| facetField.getName().equalsIgnoreCase("COUNTRY")) {
					List<FacetField.Count> list = facetField.getValues();
					if (list == null) {
						break;
					}
					List<String> counts = new ArrayList<String>();
					int counter = 0;
					for (FacetField.Count count : list) {
						counter++;
						counts.add(count.toString());
						if (counter > 5) {
							break;
						}
					}
					facetLogs.put(facetField.getName(), StringUtils.join(counts, ","));
				}
			}
		}
		return facetLogs;
	}

	@Override
	public List<? extends BriefBean> getBriefBeans() {
		return briefBeans;
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

	public void setBriefBeans(List<? extends BriefBean> briefBeans) {
		this.briefBeans = briefBeans;
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

	public void makeFilters(Query query, Map<String, String[]> urlParams) {
		searchFilters = new ArrayList<SearchFilter>();
		List<SearchParam> existingValues = SearchFilterUtil.getExistingValues(query);
		Map<String, String[]> otherUrlParams = SearchFilterUtil.getOtherUrlParams(urlParams);
		EuropeanaUrlService service = ApplicationContextContainer.getBean(EuropeanaUrlService.class);

		boolean translationsRemoved = false;

		String[] qts = urlParams.get("qt");
		if(qts != null && qts.length == 1 && qts[0].equals("false") ){
			translationsRemoved = true;
		}
		
		for (SearchParam param : existingValues) {
			UrlBuilder removeLink = null;
			UrlBuilder breadcrumbLink = null;
			try {
				removeLink = service.getPortalSearch();
				breadcrumbLink = service.getPortalSearch();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			for (String key : otherUrlParams.keySet()) {
				removeLink.addParam(key, otherUrlParams.get(key));
				breadcrumbLink.addParam(key, otherUrlParams.get(key));
			}
			if(translationsRemoved){
				if(!breadcrumbLink.hasParam("qt")){
					breadcrumbLink.addParam("qt", "false");					
				}
				if(!removeLink.hasParam("qt")){
					removeLink.addParam("qt", "false");
				}
			}

			boolean addBreadcrumb = true;
			for (SearchParam otherSearchParam : existingValues) {
				if (addBreadcrumb) {
					breadcrumbLink.addMultiParam(otherSearchParam.getKey(), otherSearchParam.getValue());
				}
				if (param.equals(otherSearchParam)) {
					addBreadcrumb = false;
					continue;
				}
				removeLink.addMultiParam(otherSearchParam.getKey(), otherSearchParam.getValue());
			}

			SearchLabel label = null;
			String paramValue = param.getValue();
			if (paramValue.indexOf(":") > -1) {
				String[] parts = paramValue.split(":", 2);
				String qfField = parts[0];
				String qfValue = parts[1];
				label = new SearchLabel(qfField, qfValue);
			} else {
				label = new SearchLabel(null, paramValue);
			}
			searchFilters.add(new SearchFilter(label, removeLink.toString(), breadcrumbLink.toString()));
		}
	}

	@Override
	public List<SearchFilter> getSearchFilters() {
		return searchFilters;
	}
}